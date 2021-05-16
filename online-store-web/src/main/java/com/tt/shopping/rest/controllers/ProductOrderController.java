package com.tt.shopping.rest.controllers;

import com.tt.shopping.order.api.model.OrderCancelRequest;
import com.tt.shopping.order.api.service.OrderManagementService;
import com.tt.shopping.rest.constants.RestUris;
import com.tt.shopping.rest.converters.ProductOrderConverter;
import com.tt.shopping.rest.json.request.order.ProductOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;


/**
 * Rest APIs for customer management.
 *
 * Acceptance Criteria:
 * The customer is already logged in and trying to place an order. Create RESTful APIs for:
 * i. Creating an order
 * ii. Canceling/Deleting an order
 * iii. List all orders using customer ID
 */
@RestController
@RequestMapping(RestUris.PRODUCT_ORDER_BASE_URI)
@Api(value = "Order Controller", description = "Product Order Management REST Endpoints.")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductOrderController {

    private final OrderManagementService orderManagementService;

    private final ProductOrderConverter productOrderConverter;

    @Autowired
    public ProductOrderController(OrderManagementService orderManagementService, ProductOrderConverter productOrderConverter) {
        this.orderManagementService = orderManagementService;
        this.productOrderConverter = productOrderConverter;
    }

    @ApiOperation(value = "Creating an order.")
    @PostMapping("/productOrder")
    public ResponseEntity<com.tt.shopping.order.api.model.ProductOrder> createOrder(
            @Valid @RequestBody ProductOrder request) {
        final com.tt.shopping.order.api.model.ProductOrder order =
                this.productOrderConverter.apply(request);
        final com.tt.shopping.order.api.model.ProductOrder persisted = this.orderManagementService.createOrder(order);
        return ResponseEntity
                .created(URI.create(RestUris.PRODUCT_ORDER_URI + persisted.getId()))
                .body(persisted);
    }

    @ApiOperation(value = "Creating an order.")
    @PostMapping("/productOrder/cancel")
    public ResponseEntity<String> cancelCustomer(@Valid @RequestBody OrderCancelRequest cancelRequest) {
        return ResponseEntity.ok(this.orderManagementService.cancelOrder(cancelRequest));
    }

    @ApiOperation(value = "List all orders using customer ID.")
    @GetMapping(value="/productOrder/{customerId}")
    public ResponseEntity<List<com.tt.shopping.order.api.model.ProductOrder>> getCustomerById(
            @PathVariable("customerId") String customerId) {
        return ResponseEntity.ok(this.orderManagementService.getOrdersByCustomerId(customerId));
    }

}
