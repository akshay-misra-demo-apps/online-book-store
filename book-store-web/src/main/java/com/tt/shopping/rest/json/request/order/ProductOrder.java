package com.tt.shopping.rest.json.request.order;

import com.tt.shopping.api.model.order.constants.ProductOrderCategory;
import com.tt.shopping.rest.json.request.customer.PaymentMethod;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductOrder {

    private String externalId;

    private String priority;

    private String description;

    private ProductOrderCategory category;

    private String customerId;

    private List<OrderItem> orderItem;

    private String notificationContact;

    private Double orderTotalPrice;

    private String billingAccount;

    private PaymentMethod paymentMethod;
}
