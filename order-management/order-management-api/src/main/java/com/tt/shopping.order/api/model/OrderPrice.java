package com.tt.shopping.order.api.model;

import com.tt.shopping.product.api.model.constants.PriceType;
import com.tt.shopping.product.api.model.constants.RecurringChargePeriod;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderPrice {

    private String name;

    private String description;

    private PriceType priceType;

    private RecurringChargePeriod recurringChargePeriod;

    private Double taxIncludedAmount;

    private Double dutyFreeAmount;

    private Double taxRate;


}
