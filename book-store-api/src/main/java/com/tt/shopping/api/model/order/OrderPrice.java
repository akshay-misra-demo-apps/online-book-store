package com.tt.shopping.api.model.order;

import com.tt.shopping.api.model.product.constants.PriceType;
import com.tt.shopping.api.model.product.constants.RecurringChargePeriod;
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
