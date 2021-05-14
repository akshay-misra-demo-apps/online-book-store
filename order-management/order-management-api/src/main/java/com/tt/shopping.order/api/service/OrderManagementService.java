package com.tt.shopping.api.service;

import com.tt.shopping.order.api.model.OrderCancelRequest;
import com.tt.shopping.order.api.model.ProductOrder;

import java.util.List;

public interface OrderManagementService {

    ProductOrder createOrder(ProductOrder order);

    List<ProductOrder> getOrdersByCustomerId(String customerId);

    String cancelOrder(OrderCancelRequest cancelRequest);
}
