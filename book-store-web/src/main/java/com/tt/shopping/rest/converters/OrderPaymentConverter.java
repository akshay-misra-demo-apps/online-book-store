package com.tt.shopping.rest.converters;

import com.tt.shopping.api.model.customer.OrderPayment;
import com.tt.shopping.rest.json.request.customer.PaymentMethod;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class OrderPaymentConverter implements Function<PaymentMethod, OrderPayment> {

    @Override
    public OrderPayment apply(PaymentMethod source) {
        return OrderPayment.builder()
                .name("Order Payment - " + source.getPaymentMethodType())
                .type(source.getPaymentMethodType())
                .characteristic(source.getCharacteristic())
                .build();
    }
}
