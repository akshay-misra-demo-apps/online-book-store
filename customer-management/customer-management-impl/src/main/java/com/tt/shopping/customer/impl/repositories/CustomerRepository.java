package com.tt.shopping.customer.impl.repositories;


import com.tt.shopping.customer.api.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepository  extends MongoRepository<Customer, String> {
}
