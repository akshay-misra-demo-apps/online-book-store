package com.tt.shopping.api.service;

import com.tt.shopping.api.model.product.Product;

import java.util.List;

public interface ProductManagementService {

    List<Product> getProducts();

    Product getProductById(String id);

    Product getProductBySku(String sku);

    Product createProduct(Product product);

    void createSampleProduct();

    Product updateProduct(Product product);

    boolean deleteProduct(String id);
}
