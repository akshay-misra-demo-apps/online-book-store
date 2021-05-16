package com.tt.shopping.customer.impl.processor;

import com.tt.shopping.common.api.processor.CreateActionProcessor;
import com.tt.shopping.common.api.utils.SequenceGenerator;
import com.tt.shopping.common.api.validator.factory.ValidatorFactory;
import com.tt.shopping.customer.api.model.Address;
import com.tt.shopping.customer.api.model.BillingAccount;
import com.tt.shopping.customer.api.model.Customer;
import com.tt.shopping.customer.api.model.contants.CustomerStatus;
import com.tt.shopping.customer.impl.repositories.AddressRepository;
import com.tt.shopping.customer.impl.repositories.BillingAccountRepository;
import com.tt.shopping.customer.impl.repositories.CustomerRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CreateCustomerProcessor<T extends Customer> extends CreateActionProcessor<T> {

    private final ValidatorFactory validatorFactory;

    private final CustomerRepository customerRepository;

    private final SequenceGenerator sequenceGenerator;

    private final AddressRepository addressRepository;

    private final BillingAccountRepository billingAccountRepository;

    @Autowired
    public CreateCustomerProcessor(ValidatorFactory validatorFactory,
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
        final List<Address> addressToPersist = customer.getAddress();
        customer.setAddress(null);
        customer.setBillingAccount(null);
        final Customer customerDb = this.customerRepository.save(customer);
        System.out.println("*** createCustomer persisted, customerDb: " + customerDb);

        if (addressToPersist != null) {
            addressToPersist.stream()
                    .forEach(address -> {
                        address.setCustomerId(customerDb.getId());
                    });
            this.addressRepository.saveAll(addressToPersist);
        }

        if (billingAccountsToPersist != null) {
            billingAccountsToPersist.stream()
                    .forEach(billingAccount -> {
                        billingAccount.setCustomerId(customerDb.getId());
                    });
            this.billingAccountRepository.saveAll(billingAccountsToPersist);
        }

        return customerDb;
    }

    @Override
    public void preValidation(final Customer customer) {
        this.validatorFactory.getPreValidators("CREATE")
                .stream()
                .forEach(validator -> {
                    validator.validate(customer);
                });
    }

    @Override
    public void postValidation(final Customer customer) {
        this.validatorFactory.getPostValidators("CREATE")
                .stream()
                .forEach(validator -> {
                    validator.validate(customer);
                });
    }
}
