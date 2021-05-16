package com.tt.shopping.rest.json.request.order;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItem {

    private String sku;

    private Integer quantity;

    private String billingAccountRef;
}
