package com.tt.shopping.product1.impl.repositories;

import com.tt.shopping.product.api.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Akshay Misra on 06-04-2020.
 */
@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    Product findBySku(String sku);
}
