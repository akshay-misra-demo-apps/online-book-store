package com.tt.shopping.rest.controllers;

import com.tt.shopping.product.api.model.Product;
import com.tt.shopping.product.api.service.ProductManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tmf-api/productOfferingManagement/v1")
@Api(value = "Order Controller", description = "Product Offering Management REST Endpoints.")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductOfferingController {

    private final ProductManagementService productManagementService;

    @Autowired
    public ProductOfferingController(ProductManagementService productManagementService) {
        this.productManagementService = productManagementService;
    }

    @ApiOperation(value = "List all available products.")
    @GetMapping("/productOffering")
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok(this.productManagementService.getProducts());
    }
}
