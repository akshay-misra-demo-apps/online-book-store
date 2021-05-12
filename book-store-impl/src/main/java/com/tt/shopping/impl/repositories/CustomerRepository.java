package com.tt.shopping.impl.repositories;

import com.tt.shopping.api.model.customer.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Akshay Misra on 09-08-2018.
 */
@Repository
public interface CustomerRepository  extends MongoRepository<Customer, String> {
}
