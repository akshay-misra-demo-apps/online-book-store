package com.tt.shopping.order.api.model;

import com.tt.shopping.order.api.model.constants.CancellationReason;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderCancelRequest {

    private String orderId;

    private CancellationReason cancellationReason;

    private String cancellationDescription;
}
