package com.tt.shopping.rest.controllers;

import com.tt.shopping.api.service.CustomerManagementService;
import com.tt.shopping.rest.converters.CustomerConverter;
import com.tt.shopping.rest.json.request.customer.Customer;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

/**
 * Rest APIs for customer management.
 *
 * Acceptance Criteria:
 * In a case of a new customer, create RESTful APIs for:
 * i. Creating a customer.
 * ii. Getting customer’s info using an ID you choose for your customers that can be identified with that.
 * iii. Updating customer’s info using the ID.
 * iv. Listing all customers.
 */
@RestController
@RequestMapping("/tmf-api/customerManagement/v1")
@Api(value = "Customer Controller", description = "Customer Management REST Endpoints.")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CustomerController {

    private final CustomerManagementService customerManagementService;

    private final CustomerConverter customerConverter;

    @Autowired
    public CustomerController(CustomerManagementService customerManagementService,
                              CustomerConverter customerConverter) {
        this.customerManagementService = customerManagementService;
        this.customerConverter = customerConverter;
    }

    @ApiOperation(value = "Creating a customer.")
    @PostMapping("/customer")
    public ResponseEntity<com.tt.shopping.api.model.customer.Customer> createCustomer(
            @Valid @RequestBody Customer request) {
        final com.tt.shopping.api.model.customer.Customer customer = this.customerConverter.apply(request);
        return ResponseEntity.ok(this.customerManagementService.createCustomer(customer));
    }

    @ApiOperation(value = "Getting customer’s info using an ID.")
    @GetMapping(value="/customer/{id}")
    public ResponseEntity<com.tt.shopping.api.model.customer.Customer> getCustomerById(
            @PathVariable("id") String id,
            @RequestParam(required = false) List<String> fields) {
        return ResponseEntity.ok(this.customerManagementService.getCustomerById(id, fields));
    }

    @ApiOperation(value = "Updating customer’s info using the ID.")
    @PatchMapping(value="/customer/{id}")
    public ResponseEntity<com.tt.shopping.api.model.customer.Customer> updateCustomer(@PathVariable("id") String id,
                                                   @Valid @RequestBody Customer request) {
        System.out.println("******** updateCustomer request: " + request);
        final com.tt.shopping.api.model.customer.Customer customer = this.customerConverter.apply(request);
        customer.setId(id);
        return ResponseEntity.ok(this.customerManagementService.updateCustomer(customer));
    }

    @ApiOperation(value = "Listing all customers.")
    @GetMapping("/customer")
    public ResponseEntity<List<com.tt.shopping.api.model.customer.Customer>> getCustomers(
             @RequestParam(required = false) List<String> fields
    ) {
        return ResponseEntity.ok(this.customerManagementService.getCustomers(fields));
    }
}
