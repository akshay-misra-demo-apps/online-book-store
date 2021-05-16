package com.tt.shopping.customer.impl.service;

import com.tt.shopping.common.api.exceptions.IncorrectRequestException;
import com.tt.shopping.common.api.exceptions.ResourceNotFoundException;
import com.tt.shopping.customer.api.model.Customer;
import com.tt.shopping.customer.api.model.Address;
import com.tt.shopping.customer.api.model.BillingAccount;
import com.tt.shopping.customer.api.service.CustomerManagementService;
import com.tt.shopping.customer.impl.processor.CreateCustomerProcessor;
import com.tt.shopping.customer.impl.processor.UpdateCustomerProcessor;
import com.tt.shopping.customer.impl.repositories.AddressRepository;
import com.tt.shopping.customer.impl.repositories.BillingAccountRepository;
import com.tt.shopping.customer.impl.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class CustomerManagementServiceImpl implements CustomerManagementService {

    private final CustomerRepository customerRepository;

    private final AddressRepository addressRepository;

    private final BillingAccountRepository billingAccountRepository;

    private final CreateCustomerProcessor createCustomerProcessor;

    private final UpdateCustomerProcessor updateCustomerProcessor;

    @Autowired
    public CustomerManagementServiceImpl(CustomerRepository customerRepository,
                                         AddressRepository addressRepository,
                                         BillingAccountRepository billingAccountRepository,
                                         CreateCustomerProcessor createCustomerProcessor,
                                         UpdateCustomerProcessor updateCustomerProcessor) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.billingAccountRepository = billingAccountRepository;
        this.createCustomerProcessor = createCustomerProcessor ;
        this.updateCustomerProcessor = updateCustomerProcessor;
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
    public List<Customer> getCustomerByPrimaryEmail(final String primaryEmail) {
        return this.customerRepository.findCustomersByPrimaryEmail(primaryEmail);
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
        return (Customer) this.createCustomerProcessor.process(customer);
    }

    @Transactional
    @Override
    public Customer updateCustomer(final Customer customer) {
        return (Customer) this.updateCustomerProcessor.process(customer);
    }

    @Override
    @Transactional
    public boolean deleteCustomer(final String id) {
        System.out.println("... deleteCustomer, id: ");
        if (this.exists(id)) {
            final List<Address> addresses = addressRepository.findAllByCustomerId(id);
            if (!CollectionUtils.isEmpty(addresses)) {
                addresses.stream()
                        .forEach(address -> {
                            this.addressRepository.deleteById(address.getId());
                        });
            }
            final List<BillingAccount> billingAccounts = this.billingAccountRepository.findAllByCustomerId(id);
            if (!CollectionUtils.isEmpty(billingAccounts)) {
                billingAccounts.stream()
                        .forEach(billingAccount -> {
                            this.billingAccountRepository.deleteById(billingAccount.getId());
                        });
            }
            this.customerRepository.deleteById(id);
            return true;
        }

        return false;
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
}
