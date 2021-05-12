package com.tt.shopping.api.service;

import com.tt.shopping.api.model.order.OrderCancelRequest;
import com.tt.shopping.api.model.order.ProductOrder;

import java.util.List;

public interface OrderManagementService {

    ProductOrder createOrder(ProductOrder order);

    List<ProductOrder> getOrdersByCustomerId(String customerId);

    String cancelOrder(OrderCancelRequest cancelRequest);
}
