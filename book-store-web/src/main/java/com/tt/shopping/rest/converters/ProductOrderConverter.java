package com.tt.shopping.rest.converters;

import com.tt.shopping.api.model.order.ProductOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ProductOrderConverter implements Function<com.tt.shopping.rest.json.request.order.ProductOrder,
        ProductOrder> {

    private final OrderItemConverter orderItemConverter;

    private final OrderPaymentConverter orderPaymentConverter;

    @Autowired
    public ProductOrderConverter(OrderItemConverter orderItemConverter,
                                 OrderPaymentConverter orderPaymentConverter) {
        this.orderItemConverter = orderItemConverter;
        this.orderPaymentConverter = orderPaymentConverter;
    }

    @Override
    public ProductOrder apply(com.tt.shopping.rest.json.request.order.ProductOrder source) {
        return ProductOrder.builder()
                .description(source.getDescription())
                .customerId(source.getCustomerId())
                .externalId(source.getExternalId())
                .priority(source.getPriority())
                .billingAccount(source.getBillingAccount())
                .category(source.getCategory())
                .notificationContact(source.getNotificationContact())
                .orderItem(source.getOrderItem().stream()
                        .map(orderItem -> this.orderItemConverter.apply(orderItem))
                        .collect(Collectors.toList()))
                .payment(this.orderPaymentConverter.apply(source.getPaymentMethod()))
                .build();
    }
}
