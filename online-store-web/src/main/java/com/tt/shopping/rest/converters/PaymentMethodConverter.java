package com.tt.shopping.rest.converters;

import com.tt.shopping.customer.api.model.PaymentMethod;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PaymentMethodConverter implements Function<com.tt.shopping.rest.json.request.customer.PaymentMethod,
        PaymentMethod> {

    @Override
    public PaymentMethod apply(com.tt.shopping.rest.json.request.customer.PaymentMethod source) {
        return PaymentMethod.builder()
                .name("Payment Method - " + source.getPaymentMethodType())
                .paymentMethodType(source.getPaymentMethodType())
                .defaultMethod(source.isDefaultMethod())
                .characteristic(source.getCharacteristic())
                .build();
    }
}
