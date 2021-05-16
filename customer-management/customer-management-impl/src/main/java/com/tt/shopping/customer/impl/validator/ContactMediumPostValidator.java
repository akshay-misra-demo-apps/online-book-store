package com.tt.shopping.customer.impl.validator;

import com.tt.shopping.common.api.validator.PostValidator;
import com.tt.shopping.customer.api.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ContactMediumPostValidator<T extends Customer> implements PostValidator<T> {

    @Autowired
    private ContactMediumValidator contactMediumValidator;

    @Override
    public List<String> getActions() {
        return Arrays.asList("UPDATE");
    }

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public void validate(Customer customer) {
        this.contactMediumValidator.validate(customer);
    }
}
