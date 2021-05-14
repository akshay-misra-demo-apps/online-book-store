package com.tt.shopping.impl.repositories;

import com.tt.shopping.api.model.customer.Address;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends MongoRepository<Address, String> {

    List<Address> findAllByCustomerId(String customerId);
}
