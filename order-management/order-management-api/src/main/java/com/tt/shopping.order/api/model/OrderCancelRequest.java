package com.tt.shopping.order.api.model;

import com.tt.shopping.order.api.model.constants.CancellationReason;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderCancelRequest {

    private String orderId;

    private CancellationReason cancellationReason;

    private String cancellationDescription;
}
