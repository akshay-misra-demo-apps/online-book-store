package com.tt.shopping.customer.impl.repositories;

import com.tt.shopping.customer.api.model.BillingAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingAccountRepository extends MongoRepository<BillingAccount, String> {

    List<BillingAccount> findAllByCustomerId(String customerId);
}
