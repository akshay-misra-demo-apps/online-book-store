package com.tt.shopping.impl.service.customer;

import com.tt.shopping.api.exceptions.BusinessValidationException;
import com.tt.shopping.api.exceptions.IncorrectRequestException;
import com.tt.shopping.api.exceptions.ResourceNotFoundException;
import com.tt.shopping.api.model.HasHref;
import com.tt.shopping.api.model.customer.*;
import com.tt.shopping.api.model.customer.contants.CustomerStatus;
import com.tt.shopping.api.service.CustomerManagementService;
import com.tt.shopping.impl.repositories.AddressRepository;
import com.tt.shopping.impl.repositories.BillingAccountRepository;
import com.tt.shopping.impl.repositories.CustomerRepository;
import com.tt.shopping.impl.utils.SequenceGenerator;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerManagementServiceImpl implements CustomerManagementService {

    private final CustomerRepository customerRepository;

    private final MongoTemplate mongoTemplate;

    private final SequenceGenerator sequenceGenerator;

    private final AddressRepository addressRepository;

    private final BillingAccountRepository billingAccountRepository;

    @Autowired
    public CustomerManagementServiceImpl(CustomerRepository customerRepository,
                                         MongoTemplate mongoTemplate,
                                         SequenceGenerator sequenceGenerator,
                                         AddressRepository addressRepository,
                                         BillingAccountRepository billingAccountRepository) {
        this.customerRepository = customerRepository;
        this.mongoTemplate = mongoTemplate;
        this.sequenceGenerator = sequenceGenerator;
        this.addressRepository = addressRepository;
        this.billingAccountRepository = billingAccountRepository;
    }

    @Override
    public List<Customer> getCustomers(final List<String> fields) {
        System.out.println("CustomerManagementServiceImpl getCustomers.");
        List<Customer> customers = this.customerRepository.findAll();
        if (fields != null) {
            if (fields.contains("address")) {
                customers.stream()
                        .forEach(customer -> {
                            customer.setAddress(this.addressRepository.findAllByCustomerId(customer.getId()));
                        });
            }

            if (fields.contains("billingAccount")) {
                customers.stream()
                        .forEach(customer -> {
                            customer.setBillingAccount(this.billingAccountRepository
                                    .findAllByCustomerId(customer.getId()));
                        });
            }
        }

        return customers;
    }

    @Override
    public Customer getCustomerById(final String id, final List<String> fields) {
        final Customer customer = this.customerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Customer with id '" +  id + "' not found."));

        if (fields != null) {
            if (fields.contains("address")) {
                customer.setAddress(this.addressRepository.findAllByCustomerId(customer.getId()));
            }

            if (fields.contains("billingAccount")) {
                customer.setBillingAccount(this.billingAccountRepository.findAllByCustomerId(customer.getId()));
            }
        }

        return customer;
    }

    @Override
    public boolean exists(final String customerId) {
        return this.customerRepository.existsById(customerId);
    }

    @Transactional
    @Override
    public Customer createCustomer(final Customer customer) {
        System.out.println("**** createCustomer, ContactMedium: " + customer.getContactMedium());
        this.validateCustomerCreation(customer);
        customer.setAccountNumber(this.sequenceGenerator.generate());
        customer.setName(customer.getFirstName() + " " + customer.getLastName());
        if (!CollectionUtils.isEmpty(customer.getContactMedium())) {
            customer.getContactMedium()
                    .stream()
                    .forEach(contactMedium -> contactMedium.setId(new ObjectId().toString()));
        }

        customer.setId(null);
        customer.setStatus(CustomerStatus.Approved);
        customer.setStatusReason("Customer created and validated.");
        final List<Address> addressToPersist = customer.getAddress();

        if (!CollectionUtils.isEmpty(customer.getBillingAccount())) {
            final boolean[] first = {true};
            customer.getBillingAccount()
                    .stream()
                    .forEach(billingAccount -> {
                        billingAccount.setId(new ObjectId().toString());
                        billingAccount.setAccountNumber(this.sequenceGenerator.generate());
                        billingAccount.setName("BA - " + billingAccount.getAccountNumber());
                        billingAccount.setActualStartDate(LocalDateTime.now());
                        if (first[0]) {
                            billingAccount.setDefaultBillingAccount(Boolean.TRUE);
                            first[0] = false;
                        } else {
                            billingAccount.setDefaultBillingAccount(Boolean.FALSE);
                        }
                        billingAccount.getPaymentMethod().stream()
                                .filter(paymentMethod -> paymentMethod.getId() == null)
                                .forEach(paymentMethod -> paymentMethod.setId(new ObjectId().toString()));
                    });
        }

        final List<BillingAccount> billingAccountsToPersist = customer.getBillingAccount();
        customer.setAddress(null);
        customer.setBillingAccount(null);
        final Customer customerDb = this.customerRepository.save(customer);

        addressToPersist.stream()
                .forEach(address -> {
                        address.setCustomerId(customerDb.getId());
                });

        billingAccountsToPersist.stream()
                .forEach(billingAccount -> {
                    billingAccount.setCustomerId(customerDb.getId());
                });
        this.addressRepository.saveAll(addressToPersist);
        this.billingAccountRepository.saveAll(billingAccountsToPersist);

        return customerDb;
    }

    @Transactional
    @Override
    public Customer updateCustomer(final Customer customer) {
        this.validateCustomerUpdate(customer);
        final Customer mergedCustomer = this.merge(customer);
        this.validatePreferredContact(mergedCustomer);
        mergedCustomer.setName(mergedCustomer.getFirstName() + " " + mergedCustomer.getLastName());
        if (!CollectionUtils.isEmpty(mergedCustomer.getContactMedium())) {
            mergedCustomer.getContactMedium()
                    .stream()
                    .filter(contactMedium -> contactMedium.getId() == null)
                    .forEach(contactMedium -> contactMedium.setId(new ObjectId().toString()));
        }

        if (!CollectionUtils.isEmpty(mergedCustomer.getBillingAccount())) {
            mergedCustomer.getBillingAccount()
                    .stream()
                    .forEach(billingAccount -> {
                        if (billingAccount.getId() == null) {
                            billingAccount.setId(new ObjectId().toString());
                            billingAccount.setAccountNumber(this.sequenceGenerator.generate());
                            billingAccount.setName("BA - " + billingAccount.getAccountNumber());
                            billingAccount.setActualStartDate(LocalDateTime.now());
                        }
                        billingAccount.getPaymentMethod().stream()
                                .filter(paymentMethod -> paymentMethod.getId() == null)
                                .forEach(paymentMethod -> paymentMethod.setId(new ObjectId().toString()));
                    });
        }

        final List<Address> addressToPersist = mergedCustomer.getAddress();
        if (addressToPersist != null) {
            addressToPersist.stream()
                    .forEach(address -> {
                        address.setCustomerId(mergedCustomer.getId());
                    });
            mergedCustomer.setAddress(null);
            this.addressRepository.saveAll(addressToPersist);
        }

        final List<BillingAccount> billingAccountsToPersist = mergedCustomer.getBillingAccount();
        if (billingAccountsToPersist != null) {
            billingAccountsToPersist.stream()
                    .forEach(billingAccount -> {
                        billingAccount.setCustomerId(mergedCustomer.getId());
                    });
            mergedCustomer.setBillingAccount(null);
            this.billingAccountRepository.saveAll(billingAccountsToPersist);
        }

        return this.customerRepository.save(mergedCustomer);
    }

    private Customer merge(final Customer customer) {
        final Customer customerDB = this.getCustomerById(customer.getId(), null);
        if (customer.getFirstName() != null) {
            customerDB.setFirstName(customer.getFirstName());
        }
        if (customer.getLastName() != null) {
            customerDB.setLastName(customer.getLastName());
        }

        if (customer.getPrimaryEmail() != null) {
            customerDB.setPrimaryEmail(customer.getPrimaryEmail());
        }
        if (customer.getSecondaryEmail() != null) {
            customerDB.setSecondaryEmail(customer.getSecondaryEmail());
        }

        if (!CollectionUtils.isEmpty(customer.getCharacteristic())) {
            customer.getCharacteristic()
                    .stream()
                    .forEach(characteristic -> {
                        if (customerDB.getCharacteristic().contains(characteristic)) {
                            customerDB.getCharacteristic().remove(characteristic);
                            customerDB.getCharacteristic().add(characteristic);
                        } else {
                            customerDB.getCharacteristic().add(characteristic);
                        }
                    });
        }

        if (!CollectionUtils.isEmpty(customer.getContactMedium())) {
            final List<ContactMedium> existingContacts = customer.getContactMedium().stream()
                    .filter(contactMedium -> contactMedium.getId() != null)
                    .collect(Collectors.toList());
            System.out.println("********* existingContacts: " + existingContacts);

            customerDB.getContactMedium()
                    .stream()
                    .forEach(contactMediumDb -> {
                        final ContactMedium fromRequest = (ContactMedium) this.findById(existingContacts,
                                contactMediumDb.getId());
                        if (fromRequest != null) {
                            if (fromRequest.getType() != null) {
                                contactMediumDb.setType(fromRequest.getType());
                            }
                            if (fromRequest.isPreferred()) {
                                contactMediumDb.setPreferred(true);
                            }
                        }
                    });
        }

        if (!CollectionUtils.isEmpty(customer.getAddress())) {
            final List<Address> existingAddresses = customer.getAddress().stream()
                    .filter(address -> address.getId() != null)
                    .collect(Collectors.toList());
            System.out.println("********* existingAddresses: " + existingAddresses);

            customerDB.setAddress(this.addressRepository.findAllByCustomerId(customer.getId()));
            customerDB.getAddress().stream()
                    .forEach(addressDb -> {
                        final Address fromRequest = (Address) this.findById(existingAddresses, addressDb.getId());
                        if (fromRequest != null) {
                            if (fromRequest.getStreet1() != null) {
                                addressDb.setStreet1(fromRequest.getStreet1());
                            }
                            if (fromRequest.getStreet2() != null) {
                                addressDb.setStreet2(fromRequest.getStreet2());
                            }
                            if (fromRequest.getCity() != null) {
                                addressDb.setCity(fromRequest.getCity());
                            }
                            if (fromRequest.getStateOrProvince() != null) {
                                addressDb.setStateOrProvince(fromRequest.getStateOrProvince());
                            }
                            if (fromRequest.getCountry() != null) {
                                addressDb.setCountry(fromRequest.getCountry());
                            }
                            if (fromRequest.getPostCode() != null) {
                                addressDb.setPostCode(fromRequest.getPostCode());
                            }
                            if (fromRequest.getPhoneNumber() != null) {
                                addressDb.setPhoneNumber(fromRequest.getPhoneNumber());
                            }
                            if (fromRequest.getAddressType() != null) {
                                addressDb.setAddressType(fromRequest.getAddressType());
                            }
                        }
                    });

            final List<Address> newAddresses = customer.getAddress().stream()
                    .filter(address -> address.getId() == null)
                    .collect(Collectors.toList());
            System.out.println("********* newAddresses: " + newAddresses);
            customerDB.getAddress().addAll(newAddresses);
        }

        if (!CollectionUtils.isEmpty(customer.getBillingAccount())) {
            final List<BillingAccount> existingBillingAccounts = customer.getBillingAccount()
                    .stream()
                    .filter(billingAccount -> billingAccount.getId() != null)
                    .collect(Collectors.toList());

            customerDB.setBillingAccount(this.billingAccountRepository
                    .findAllByCustomerId(customer.getId()));
            customerDB.getBillingAccount().stream()
                    .forEach(billingAccountDb -> {
                        final BillingAccount fromRequest =
                                (BillingAccount) this.findById(existingBillingAccounts, billingAccountDb.getId());
                        if (fromRequest != null) {
                            if (fromRequest.getCurrency() != null) {
                                billingAccountDb.setCurrency(fromRequest.getCurrency());
                            }
                            if (fromRequest.getDefaultBillingAccount() != null) {
                                billingAccountDb.setDefaultBillingAccount(fromRequest.getDefaultBillingAccount());
                            }
                            List<PaymentMethod> newPaymentMethods = fromRequest.getPaymentMethod()
                                    .stream()
                                    .filter(paymentMethod -> paymentMethod.getId() == null)
                                    .collect(Collectors.toList());
                            billingAccountDb.getPaymentMethod().addAll(newPaymentMethods);
                        }
                    });

            final List<BillingAccount> newBillingAccounts = customer.getBillingAccount()
                    .stream()
                    .filter(billingAccount -> billingAccount.getId() == null)
                    .collect(Collectors.toList());
            customerDB.getBillingAccount().addAll(newBillingAccounts);
        }

        return customerDB;
    }

    private HasHref findById(final List existingObjects, final String id) {
        return (HasHref) existingObjects.stream()
                .map(HasHref.class::cast)
                .filter(o ->  id.equals(((HasHref) o).getId()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void validateCustomer(final String customerId) {
        if (customerId == null) {
            throw new IncorrectRequestException("Incorrect request, 'customerId'" +
                    "is missing.");
        }
        boolean exists = this.exists(customerId);
        if (!exists) {
            throw new ResourceNotFoundException("Customer with id '" +  customerId + "' not found.");
        }
    }

    private void validateCustomerCreation(final Customer customer) {
        if (customer.getCustomerCategory() == null) {
            throw new IncorrectRequestException("Incorrect request, 'customerCategory'" +
                    "is missing. Available values are: 'ENTERPRISE' or 'RESIDENTIAL'.");
        }

        if (customer.getFirstName() == null || customer.getLastName() == null) {
            throw new IncorrectRequestException("Incorrect request, both 'firstName'" +
                    "and 'lastName' are required.");
        }

        if (customer.getPrimaryEmail() == null) {
            throw new IncorrectRequestException("Incorrect request, 'primaryEmail' is required.");
        }

        this.validatePreferredContact(customer);

        if (!CollectionUtils.isEmpty(customer.getAddress())) {
            customer.getAddress().stream()
                    .forEach(address -> {
                        if (address.getCity() == null
                                || address.getCountry() == null
                                || address.getPostCode() == null
                                || address.getStreet1() == null
                                || address.getStateOrProvince() == null
                                || address.getPhoneNumber() == null
                                || address.getAddressType() == null) {
                            throw new IncorrectRequestException("Incorrect request, one of the required field is" +
                                    " missing from following list: 'street1', 'city', 'stateOrProvince'" +
                                    ", 'country', 'postCode', 'phoneNumber' or 'addressType'.");
                        }
                    });
        }

        customer.getBillingAccount()
                .stream()
                .forEach(billingAccount -> {
                    if (billingAccount.getCurrency() == null) {
                        throw new IncorrectRequestException("Incorrect request, 'currency' is required " +
                                "in 'billingAccount'.");
                    }
                });

        this.validateMissingPaymentMethod(customer);
    }

    private void validatePreferredContact(final Customer customer) {
        if (!CollectionUtils.isEmpty(customer.getContactMedium())) {
            customer.getContactMedium().stream()
                    .forEach(contactMedium -> {
                        if (contactMedium.getType() == null) {
                            throw new IncorrectRequestException("Incorrect request, 'type' is required under " +
                                    "'contactMedium'.");
                        }
                    });
            long preferredContacts = customer.getContactMedium().stream()
                    .filter(contactMedium -> contactMedium.isPreferred())
                    .count();
            if (preferredContacts > 1L) {
                throw new IncorrectRequestException("Incorrect request, only one 'contactMedium' can be " +
                        "marked as 'preferred'.");
            }
        }

        if (!CollectionUtils.isEmpty(customer.getBillingAccount())) {
            long defaultBillingAccounts = customer.getBillingAccount().stream()
                    .filter(billingAccount -> Boolean.TRUE.equals(billingAccount.getDefaultBillingAccount()))
                    .count();

            if (defaultBillingAccounts > 1L) {
                throw new IncorrectRequestException("Incorrect request, maximum one 'billingAccount' can be " +
                        "marked as 'default'.");
            }
        }
    }

    private void validateCustomerUpdate(final Customer customer) {
        this.validateCustomer(customer.getId());
        if (!CollectionUtils.isEmpty(customer.getBillingAccount())) {
            final boolean pmUpdateNotAllowed = customer.getBillingAccount().stream()
                    .map(billingAccount -> billingAccount.getPaymentMethod())
                    .flatMap(Collection::stream)
                    .anyMatch(paymentMethod -> paymentMethod.getId() != null);
            if (pmUpdateNotAllowed) {
                throw new BusinessValidationException("Updating existing payment method is not supported." +
                        " Only addition of new payment method is allowed." +
                        " Remove 'id' from request for payment method objects.");
            }

            this.validateMissingPaymentMethod(customer);
        }
    }

    private void validateMissingPaymentMethod(final Customer customer) {
        final boolean pmMissing = customer.getBillingAccount()
                .stream()
                .filter(billingAccount -> billingAccount.getId() == null)
                .anyMatch(billingAccount -> CollectionUtils.isEmpty(billingAccount.getPaymentMethod()));
        if (pmMissing) {
            throw new IncorrectRequestException("Incorrect request, at least one 'paymentMethod' should be added" +
                    " while adding a new billing account.");
        }

        boolean pmTypeMissing = customer.getBillingAccount().stream()
                .map(billingAccount -> billingAccount.getPaymentMethod())
                .flatMap(Collection::stream)
                .anyMatch(paymentMethod -> paymentMethod.getPaymentMethodType() == null);

        if (pmTypeMissing) {
            throw new IncorrectRequestException("Incorrect request, 'paymentMethodType' is required " +
                    "in 'paymentMethod'.");
        }
    }
}
