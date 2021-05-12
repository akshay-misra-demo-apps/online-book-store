package com.tt.shopping.api.model.order;

import com.tt.shopping.api.model.order.constants.CancellationReason;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderCancelRequest {

    private String orderId;

    private CancellationReason cancellationReason;

    private String cancellationDescription;
}
