package com.tt.shopping.order.api.model;

import com.tt.shopping.common.api.model.HasHrefName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class OrderItem extends HasHrefName {

    private String sku;

    private Integer quantity;

    private OrderPrice itemPrice;

    private String billingAccountRef;
}
