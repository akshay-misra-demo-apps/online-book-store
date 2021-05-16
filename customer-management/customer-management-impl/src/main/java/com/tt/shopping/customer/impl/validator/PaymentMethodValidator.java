package com.tt.shopping.customer.impl.validator;

import com.tt.shopping.common.api.exceptions.IncorrectRequestException;
import com.tt.shopping.common.api.validator.PreValidator;
import com.tt.shopping.customer.api.model.Customer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class PaymentMethodValidator<T extends Customer> implements PreValidator<T> {

    @Override
    public List<String> getActions() {
        return Arrays.asList("CREATE", "UPDATE");
    }

    @Override
    public int getOrder() {
        return 6;
    }

    @Override
    public void validate(final Customer customer) {
        if (!CollectionUtils.isEmpty(customer.getBillingAccount())) {
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
}
