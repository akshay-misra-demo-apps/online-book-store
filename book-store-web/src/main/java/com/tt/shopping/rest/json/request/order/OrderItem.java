package com.tt.shopping.rest.json.request.order;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItem {

    private String sku;

    private Integer quantity;

    private String billingAccountRef;
}
