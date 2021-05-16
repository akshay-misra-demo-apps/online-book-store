package com.tt.shopping.order.impl.service;

import com.tt.shopping.common.api.exceptions.BusinessValidationException;
import com.tt.shopping.common.api.exceptions.IncorrectRequestException;
import com.tt.shopping.common.api.exceptions.ResourceNotFoundException;
import com.tt.shopping.customer.api.model.contants.OrderPaymentStatus;
import com.tt.shopping.customer.api.service.BillingAccountManagementService;
import com.tt.shopping.customer.api.service.CustomerManagementService;
import com.tt.shopping.order.api.model.OrderItem;
import com.tt.shopping.order.api.model.ProductOrder;
import com.tt.shopping.order.api.model.constants.ProductOrderStatus;
import com.tt.shopping.order.api.model.OrderCancelRequest;
import com.tt.shopping.order.api.model.OrderPrice;
import com.tt.shopping.order.api.service.OrderManagementService;
import com.tt.shopping.order.impl.repositories.OrderRepository;
import com.tt.shopping.product.api.model.Price;
import com.tt.shopping.product.api.model.Product;
import com.tt.shopping.product.api.model.constants.DistributionChannel;
import com.tt.shopping.product.api.service.ProductManagementService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderManagementServiceImpl implements OrderManagementService {

    private final OrderRepository orderRepository;

    private final ProductManagementService productManagementService;

    private final CustomerManagementService customerManagementService;

    private final BillingAccountManagementService billingAccountManagementService;

    @Autowired
    public OrderManagementServiceImpl(OrderRepository orderRepository,
                                      ProductManagementService productManagementService,
                                      CustomerManagementService customerManagementService,
                                      BillingAccountManagementService billingAccountManagementService) {
        this.orderRepository = orderRepository;
        this.productManagementService = productManagementService;
        this.customerManagementService = customerManagementService;
        this.billingAccountManagementService = billingAccountManagementService;
    }

    @Override
    public ProductOrder createOrder(final ProductOrder order) {
        this.validateOrder(order);
        order.setState(ProductOrderStatus.ACKNOWLEDGED);
        order.setChannel(DistributionChannel.ONLINE);
        order.setOrderDate(LocalDateTime.now());
        order.setExpectedCompletionDate(LocalDateTime.now().plusDays(3L));
        order.getOrderItem().stream()
                .forEach(orderItem -> {
                    Product product = this.productManagementService.getProductBySku(orderItem.getSku());
                    orderItem.setId(new ObjectId().toString());
                    orderItem.setItemPrice(this.composeOrderPrice(product.getPrice()));
                });
        if (order.getPriority() == null) {
            order.setPriority("0");
        }
        Double totalPriceWithTax = order.getOrderItem().stream()
                .map(orderItem -> orderItem.getItemPrice().getTaxIncludedAmount())
                .reduce(0.0, Double::sum);
        order.setOrderTotalPrice(totalPriceWithTax);
        order.getPayment().setAmount(totalPriceWithTax);
        order.getPayment().setStatus(OrderPaymentStatus.AWAITING);

        return this.orderRepository.save(order);
    }

    private OrderPrice composeOrderPrice(final Price price) {
        return OrderPrice.builder()
                .priceType(price.getPriceType())
                .name(price.getName())
                .description(price.getDescription())
                .dutyFreeAmount(price.getBasePrice())
                .taxRate(price.getTaxRate())
                .recurringChargePeriod(price.getRecurringChargePeriod())
                .taxIncludedAmount(price.getBasePrice() + (price.getBasePrice() * (price.getTaxRate()/100)))
                .build();
    }

    @Override
    public List<ProductOrder> getOrdersByCustomerId(final String customerId) {
        this.customerManagementService.validateCustomer(customerId);
        return this.orderRepository.findAllByCustomerId(customerId);
    }

    @Override
    public String cancelOrder(final OrderCancelRequest cancelRequest) {
        if (cancelRequest.getOrderId() == null || cancelRequest.getCancellationReason() == null) {
            throw new IncorrectRequestException("Incorrect request, 'orderId' and 'cancellationReason' " +
                    "must be present.");
        }
        final ProductOrder order = this.orderRepository.findById(cancelRequest.getOrderId()).orElseThrow(
                () -> new ResourceNotFoundException("Order with id '" +  cancelRequest.getOrderId() + "' not found."));
        order.setState(ProductOrderStatus.CANCELED);
        order.setCancellationReason(cancelRequest.getCancellationReason());
        order.setCancellationDescription(cancelRequest.getCancellationDescription());
        this.orderRepository.save(order);

        return "Product Order with id: " + order.getId() + " has been cancelled successfully.";
    }

    @Override
    public void deleteOrdersForCustomer(final String customerId) {
        final List<ProductOrder> orders = this.getOrdersByCustomerId(customerId);
        orders.stream()
                .forEach(productOrder -> {
                    this.deleteProductOrder(productOrder.getId());
                });
    }

    @Override
    public boolean deleteProductOrder(final String id) {
        boolean deleted = false;
        if(this.orderRepository.existsById(id)) {
            this.orderRepository.deleteById(id);
            deleted = true;
        }

        return deleted;
    }

    private void validateOrder(final ProductOrder order) {
        this.customerManagementService.validateCustomer(order.getCustomerId());
        if (CollectionUtils.isEmpty(order.getOrderItem())) {
            throw new IncorrectRequestException("Incorrect request, at least one 'orderItem'" +
                    "should be present.");
        } else {
            this.validateProduct(order.getOrderItem());
            order.getOrderItem().stream()
                    .forEach(orderItem -> {
                        if (orderItem.getBillingAccountRef() == null) {
                            throw new IncorrectRequestException("Incorrect request, 'billingAccountRef'" +
                                    "must be provided for each 'orderItem'.");
                        } else if (!this.billingAccountManagementService.exists(orderItem.getBillingAccountRef())) {
                            throw new ResourceNotFoundException("Billing account with id '"
                                    +  orderItem.getBillingAccountRef() + "' not found.");
                        }
                    });
        }
    }



    private void validateProduct(List<OrderItem> orderItems) {
        orderItems.stream()
                .forEach(orderItem -> {
                    if (orderItem.getSku() == null || orderItem.getQuantity() == null || orderItem.getQuantity() < 1) {
                        throw new IncorrectRequestException("Incorrect request, 'sku' and minimum one 'quantity'" +
                                "must be provided for each 'orderItem'.");
                    } else {
                        Product product = this.productManagementService.getProductBySku(orderItem.getSku());
                        if (product == null) {
                            throw new ResourceNotFoundException("Product with sku '"
                                    +  orderItem.getSku() + "' not found.");
                        } else if (orderItem.getQuantity() > product.getQuantityPerUser()) {
                            throw new BusinessValidationException("Cannot place order for more than allowed quantity '"
                                    + product.getQuantityPerUser() + "' for 'sku' " + orderItem.getSku( )
                                    + " per customer per order.");
                        }
                    }
                });
    }
}
