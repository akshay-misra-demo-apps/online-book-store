package com.tt.shopping.customer.impl.validator;

import com.tt.shopping.common.api.exceptions.IncorrectRequestException;
import com.tt.shopping.common.api.validator.PreValidator;
import com.tt.shopping.customer.api.model.Customer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

@Component
public class ContactMediumValidator<T extends Customer> implements PreValidator<T> {

    @Override
    public List<String> getActions() {
        return Arrays.asList("CREATE");
    }

    @Override
    public int getOrder() {
        return 4;
    }

    @Override
    public void validate(final Customer customer) {
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
    }
}
