package com.tt.shopping.customer.impl.validator;

import com.tt.shopping.common.api.exceptions.IncorrectRequestException;
import com.tt.shopping.common.api.exceptions.ResourceNotFoundException;
import com.tt.shopping.common.api.validator.PreValidator;
import com.tt.shopping.customer.api.model.Customer;
import com.tt.shopping.customer.api.service.CustomerManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CustomerExistValidation<T extends Customer> implements PreValidator<T> {

    @Autowired
    private CustomerManagementService customerManagementService;

    @Override
    public List<String> getActions() {
        return Arrays.asList("UPDATE");
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void validate(final Customer customer) {
        if (customer.getId() == null) {
            throw new IncorrectRequestException("Incorrect request, 'customerId'" +
                    "is missing.");
        }
        boolean exists = this.customerManagementService.exists(customer.getId());
        if (!exists) {
            throw new ResourceNotFoundException("Customer with id '" +  customer.getId() + "' not found.");
        }
    }
}