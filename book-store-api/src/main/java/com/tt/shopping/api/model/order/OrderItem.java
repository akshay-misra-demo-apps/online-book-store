package com.tt.shopping.api.model.order;

import com.tt.shopping.api.model.HasHrefName;
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

    private String billingAccount;
}
