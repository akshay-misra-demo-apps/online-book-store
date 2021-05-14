package com.tt.shopping.rest.converters;

import com.tt.shopping.order.api.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class OrderItemConverter implements Function<com.tt.shopping.rest.json.request.order.OrderItem,
        OrderItem> {

    @Override
    public OrderItem apply(com.tt.shopping.rest.json.request.order.OrderItem source) {
        return OrderItem.builder()
                .sku(source.getSku())
                .billingAccountRef(source.getBillingAccountRef())
                .quantity(source.getQuantity())
                .build();
    }
}
