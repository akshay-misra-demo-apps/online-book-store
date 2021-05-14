package com.tt.shopping.order.impl.repositories;

import com.tt.shopping.order.api.model.ProductOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Akshay Misra on 06-04-2020.
 */
@Repository
public interface OrderRepository extends MongoRepository<ProductOrder, String> {

    List<ProductOrder> findAllByCustomerId(String customerId);
}
