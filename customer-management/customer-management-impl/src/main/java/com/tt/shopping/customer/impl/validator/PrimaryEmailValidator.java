package com.tt.shopping.customer.impl.validator;

import com.tt.shopping.common.api.exceptions.IncorrectRequestException;
import com.tt.shopping.common.api.validator.PreValidator;
import com.tt.shopping.customer.api.model.Customer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class PrimaryEmailValidator<T extends Customer> implements PreValidator<T> {

    @Override
    public List<String> getActions() {
        return Arrays.asList("CREATE");
    }

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public void validate(final Customer customer) {
        if (customer.getPrimaryEmail() == null) {
            throw new IncorrectRequestException("Incorrect request, 'primaryEmail' is required.");
        }
    }
}
