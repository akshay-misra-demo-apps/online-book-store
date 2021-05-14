package com.tt.shopping.impl.repositories;

import com.tt.shopping.api.model.customer.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepository  extends MongoRepository<Customer, String> {
}
