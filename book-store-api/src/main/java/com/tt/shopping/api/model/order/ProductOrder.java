package com.tt.shopping.api.model.order;

import com.tt.shopping.api.model.HasHref;
import com.tt.shopping.api.model.customer.OrderPayment;
import com.tt.shopping.api.model.order.constants.CancellationReason;
import com.tt.shopping.api.model.order.constants.ProductOrderCategory;
import com.tt.shopping.api.model.order.constants.ProductOrderStatus;
import com.tt.shopping.api.model.product.constants.DistributionChannel;
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

    private ProductOrderCategory category;

    private ProductOrderStatus state;

    private CancellationReason cancellationReason;

    private String cancellationDescription;

    @Indexed(name = "customer_index")
    private String customerId;

    private LocalDateTime orderDate;

    private LocalDateTime completionDate;

    private LocalDateTime expectedCompletionDate;

    private List<OrderItem> orderItem;

    private String notificationContact;

    private Double orderTotalPrice;

    private String billingAccount;

    private DistributionChannel channel;

    private OrderPayment payment;
}
