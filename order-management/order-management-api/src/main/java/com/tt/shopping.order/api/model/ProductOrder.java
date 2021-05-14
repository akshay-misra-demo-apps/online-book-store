package com.tt.shopping.order.api.model;

import com.tt.shopping.common.api.model.HasHref;
import com.tt.shopping.customer.api.model.Address;
import com.tt.shopping.customer.api.model.OrderPayment;
import com.tt.shopping.order.api.model.constants.CancellationReason;
import com.tt.shopping.order.api.model.constants.ProductOrderStatus;
import com.tt.shopping.product.api.model.constants.DistributionChannel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@Document(collection="orders")
public class ProductOrder extends HasHref {

    @Indexed(name = "external_id_index")
    private String externalId;

    private String priority;

    private String description;

    private ProductOrderStatus state;

    private CancellationReason cancellationReason;

    private String cancellationDescription;

    @Indexed(name = "customer_id_index")
    private String customerId;

    private LocalDateTime orderDate;

    private LocalDateTime completionDate;

    private LocalDateTime expectedCompletionDate;

    private List<OrderItem> orderItem;

    private String notificationContact;

    private Double orderTotalPrice;

    private DistributionChannel channel;

    @Indexed(name = "order_payment_index")
    private OrderPayment payment;

    private Address deliveryAddress;
}
