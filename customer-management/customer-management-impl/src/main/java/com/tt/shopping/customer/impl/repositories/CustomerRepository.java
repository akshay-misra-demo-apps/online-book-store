package com.tt.shopping.customer.impl.repositories;


import com.tt.shopping.customer.api.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CustomerRepository  extends MongoRepository<Customer, String> {

    Customer findFirstByPrimaryEmail(String primaryEmail);

    List<Customer> findCustomersByPrimaryEmail(String primaryEmail);
}
