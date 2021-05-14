package com.tt.shopping.rest.converters;

import com.tt.shopping.customer.api.model.BillingAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BillingAccountConverter implements Function<com.tt.shopping.rest.json.request.customer.BillingAccount,
        BillingAccount> {

    private final PaymentMethodConverter paymentMethodConverter;

    @Autowired
    public BillingAccountConverter(PaymentMethodConverter paymentMethodConverter) {
        this.paymentMethodConverter = paymentMethodConverter;
    }

    @Override
    public BillingAccount apply(com.tt.shopping.rest.json.request.customer.BillingAccount source) {
        return BillingAccount.builder()
                .id(source.getId())
                .defaultBillingAccount(source.getDefaultBillingAccount())
                .currency(source.getCurrency())
                .paymentMethod(source.getPaymentMethod() == null ? null : source.getPaymentMethod()
                        .stream()
                        .map(paymentMethod -> this.paymentMethodConverter.apply(paymentMethod))
                        .collect(Collectors.toList()))
                .build();
    }
}
