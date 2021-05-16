package com.tt.shopping.customer.impl.processor;

import com.tt.shopping.common.api.model.HasHref;
import com.tt.shopping.common.api.processor.UpdateActionProcessor;
import com.tt.shopping.common.api.utils.SequenceGenerator;
import com.tt.shopping.common.api.validator.factory.ValidatorFactory;
import com.tt.shopping.customer.api.model.Address;
import com.tt.shopping.customer.api.model.BillingAccount;
import com.tt.shopping.customer.api.model.Customer;
import com.tt.shopping.customer.api.model.ContactMedium;
import com.tt.shopping.customer.api.model.PaymentMethod;
import com.tt.shopping.customer.impl.repositories.AddressRepository;
import com.tt.shopping.customer.impl.repositories.BillingAccountRepository;
import com.tt.shopping.customer.impl.repositories.CustomerRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UpdateCustomerProcessor <T extends Customer> extends UpdateActionProcessor<T> {

    private final ValidatorFactory validatorFactory;

    private final CustomerRepository customerRepository;

    private final SequenceGenerator sequenceGenerator;

    private final AddressRepository addressRepository;

    private final BillingAccountRepository billingAccountRepository;

    @Autowired
    public UpdateCustomerProcessor(ValidatorFactory validatorFactory,
                                   CustomerRepository customerRepository,
                                   SequenceGenerator sequenceGenerator,
                                   AddressRepository addressRepository,
                                   BillingAccountRepository billingAccountRepository) {
        this.validatorFactory = validatorFactory;
        this.customerRepository = customerRepository;
        this.sequenceGenerator = sequenceGenerator;
        this.addressRepository = addressRepository;
        this.billingAccountRepository = billingAccountRepository;
    }

    @Override
    public Customer doAction(final Customer customer) {
        customer.setName(customer.getFirstName() + " " + customer.getLastName());
        if (!CollectionUtils.isEmpty(customer.getContactMedium())) {
            customer.getContactMedium()
                    .stream()
                    .filter(contactMedium -> contactMedium.getId() == null)
                    .forEach(contactMedium -> contactMedium.setId(new ObjectId().toString()));
        }

        if (!CollectionUtils.isEmpty(customer.getBillingAccount())) {
            customer.getBillingAccount()
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

        final List<Address> addressToPersist = customer.getAddress();
        if (addressToPersist != null) {
            addressToPersist.stream()
                    .forEach(address -> {
                        address.setCustomerId(customer.getId());
                    });
            customer.setAddress(null);
            this.addressRepository.saveAll(addressToPersist);
        }

        final List<BillingAccount> billingAccountsToPersist = customer.getBillingAccount();
        if (billingAccountsToPersist != null) {
            billingAccountsToPersist.stream()
                    .forEach(billingAccount -> {
                        billingAccount.setCustomerId(customer.getId());
                    });
            customer.setBillingAccount(null);
            this.billingAccountRepository.saveAll(billingAccountsToPersist);
        }

        return this.customerRepository.save(customer);
    }

    @Override
    public void preValidation(final Customer customer) {
        this.validatorFactory.getPreValidators("UPDATE")
                .stream()
                .forEach(validator -> {
                    validator.validate(customer);
                });
    }

    @Override
    public Customer merge(final Customer customer) {
        final Customer customerDB = this.getCustomerById(customer.getId());

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
                            final List<PaymentMethod> newPaymentMethods = fromRequest.getPaymentMethod()
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

    @Override
    public void postValidation(final Customer customer) {
        this.validatorFactory.getPostValidators("UPDATE")
                .stream()
                .forEach(validator -> {
                    validator.validate(customer);
                });
    }

    private HasHref findById(final List existingObjects, final String id) {
        return (HasHref) existingObjects.stream()
                .map(HasHref.class::cast)
                .filter(o ->  id.equals(((HasHref) o).getId()))
                .findFirst()
                .orElse(null);
    }

    private Customer getCustomerById(final String id) {
        return this.customerRepository.findById(id).orElse(null);
    }
}