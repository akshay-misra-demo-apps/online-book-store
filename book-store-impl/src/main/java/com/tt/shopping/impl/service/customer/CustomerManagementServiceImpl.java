package com.tt.shopping.impl.service.customer;

import com.tt.shopping.api.exceptions.BusinessValidationException;
import com.tt.shopping.api.exceptions.IncorrectRequestException;
import com.tt.shopping.api.exceptions.ResourceNotFoundException;
import com.tt.shopping.api.model.customer.Address;
import com.tt.shopping.api.model.customer.ContactMedium;
import com.tt.shopping.api.model.customer.Customer;
import com.tt.shopping.api.model.customer.contants.CustomerStatus;
import com.tt.shopping.api.service.CustomerManagementService;
import com.tt.shopping.impl.repositories.CustomerRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class CustomerManagementServiceImpl implements CustomerManagementService {

    private final CustomerRepository customerRepository;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public CustomerManagementServiceImpl(CustomerRepository customerRepository,
                                         MongoTemplate mongoTemplate) {
        this.customerRepository = customerRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Customer> getCustomers() {
        System.out.println("CustomerManagementServiceImpl getCustomers.");
        final Sort sortByCreatedAtDesc =  Sort.by(Sort.Direction.DESC, "createdAt");
        Query q = new Query();
        //TODO:: q.fields().include()  try this in loop
        mongoTemplate.find(q, Customer.class);
        return this.customerRepository.findAll(sortByCreatedAtDesc);
    }

    @Override
    public Customer getCustomerById(final String id) {
        return this.customerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Customer with id '" +  id + "' not found."));
    }

    @Override
    public boolean exists(String customerId) {
        return this.customerRepository.existsById(customerId);
    }

    @Transactional
    @Override
    public Customer createCustomer(final Customer customer) {
        System.out.println("**** createCustomer, ContactMedium: " + customer.getContactMedium());

        if (!CollectionUtils.isEmpty(customer.getContactMedium())) {
            customer.getContactMedium()
                    .stream()
                    .forEach(contactMedium -> contactMedium.setId(new ObjectId().toString()));
        }

        if (!CollectionUtils.isEmpty(customer.getAddress())) {
            customer.getAddress()
                    .stream()
                    .forEach(contactMedium -> contactMedium.setId(new ObjectId().toString()));
        }

        if (!CollectionUtils.isEmpty(customer.getPaymentMethod())) {
            customer.getPaymentMethod()
                    .stream()
                    .filter(paymentMethod -> paymentMethod.getId() == null)
                    .forEach(paymentMethod -> paymentMethod.setId(new ObjectId().toString()));
        }

        customer.setId(null);
        customer.setStatus(CustomerStatus.Approved);
        customer.setStatusReason("Customer created and validated.");

        return this.customerRepository.save(customer);
    }

    @Transactional
    @Override
    public Customer updateCustomer(final String id,
                                   final Customer customer) {
        customer.setId(id);
        this.validateCustomer(customer);
        final Customer mergedCustomer = this.merge(customer);
        this.customerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Customer with id '" +  id + "' not found."));

        if (!CollectionUtils.isEmpty(mergedCustomer.getContactMedium())) {
            mergedCustomer.getContactMedium()
                    .stream()
                    .filter(contactMedium -> contactMedium.getId() == null)
                    .forEach(contactMedium -> contactMedium.setId(new ObjectId().toString()));
        }

        if (!CollectionUtils.isEmpty(mergedCustomer.getAddress())) {
            mergedCustomer.getAddress()
                    .stream()
                    .filter(address -> address.getId() == null)
                    .forEach(address -> address.setId(new ObjectId().toString()));
        }

        if (!CollectionUtils.isEmpty(mergedCustomer.getPaymentMethod())) {
            mergedCustomer.getPaymentMethod()
                    .stream()
                    .filter(paymentMethod -> paymentMethod.getId() == null)
                    .forEach(paymentMethod -> paymentMethod.setId(new ObjectId().toString()));
        }

        return this.customerRepository.save(mergedCustomer);
    }

    private Customer merge(final Customer customer) {
        Customer customerDB = this.getCustomerById(customer.getId());
        if (customer.getName() != null) {
            customerDB.setName(customer.getName());
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
            List<ContactMedium> contactsFromRequest = customer.getContactMedium();
            System.out.println("********* contactsFromRequest: " + contactsFromRequest);
            List<ContactMedium> contactsDb = customerDB.getContactMedium();
            System.out.println("********* contactsDb: " + contactsDb);
            contactsDb.removeAll(contactsFromRequest);
            contactsDb.addAll(contactsFromRequest);
            System.out.println("********* contactsDb after merge: " + contactsDb);
        }

        if (!CollectionUtils.isEmpty(customer.getAddress())) {
            List<Address> addressDb = customerDB.getAddress();
            System.out.println("********* addressDb: " + addressDb);
            List<Address> addressFromRequest = customer.getAddress();
            System.out.println("********* addressFromRequest: " + addressFromRequest);
            addressDb.removeAll(addressFromRequest);
            addressDb.addAll(addressFromRequest);
            System.out.println("********* addressDb after merge: " + addressDb);
        }

        if (!CollectionUtils.isEmpty(customer.getPaymentMethod())) {
            customerDB.getPaymentMethod().addAll(customer.getPaymentMethod());
        }

        return customerDB;
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

    @Override
    public void validateCustomer(final Customer customer) {
        this.validateCustomer(customer.getId());
        if (!CollectionUtils.isEmpty(customer.getPaymentMethod())) {
            final boolean unsupported = customer.getPaymentMethod().stream()
                    .anyMatch(paymentMethod -> paymentMethod.getId() != null);
            if (unsupported) {
                throw new BusinessValidationException("Updating existing payment method is not supported." +
                        " Only addition of new payment method is allowed." +
                        " Remove 'id' from request for payment method objects.");
            }
        }
    }
}
