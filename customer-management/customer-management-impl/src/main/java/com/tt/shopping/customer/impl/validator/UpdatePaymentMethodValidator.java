package com.tt.shopping.customer.impl.validator;

import com.tt.shopping.common.api.exceptions.BusinessValidationException;
import com.tt.shopping.common.api.validator.PreValidator;
import com.tt.shopping.customer.api.model.Customer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class UpdatePaymentMethodValidator<T extends Customer> implements PreValidator<T> {

    @Override
    public List<String> getActions() {
        return Arrays.asList("UPDATE");
    }

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public void validate(final Customer customer) {
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
        }
    }
}
