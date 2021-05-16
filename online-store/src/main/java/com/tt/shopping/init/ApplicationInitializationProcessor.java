package com.tt.shopping.init;

import com.tt.shopping.customer.api.model.Customer;
import com.tt.shopping.customer.api.service.CustomerManagementService;
import com.tt.shopping.order.api.service.OrderManagementService;
import com.tt.shopping.product.api.service.ProductManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ApplicationInitializationProcessor {

    private final ProductManagementService productManagementService;

    private final OrderManagementService orderManagementService;

    private final CustomerManagementService customerManagementService;

    @Autowired
    public ApplicationInitializationProcessor(OrderManagementService orderManagementService,
                                              CustomerManagementService customerManagementService,
                                              ProductManagementService productManagementService) {
        this.orderManagementService = orderManagementService;
        this.customerManagementService = customerManagementService;
        this.productManagementService = productManagementService;
    }

    @Transactional
    public void init() {
        this.productManagementService.createSampleProduct();
        List<Customer> customers = customerManagementService.getCustomerByPrimaryEmail("test@primary.com");
        customers.stream()
                .forEach(customer -> {
                    orderManagementService.deleteOrdersForCustomer(customer.getId());
                    customerManagementService.deleteCustomer(customer.getId());
                });
    }
}
