package com.tt.shopping.customer.api.service;


import com.tt.shopping.customer.api.model.Customer;

import java.util.List;

public interface CustomerManagementService {

    List<Customer> getCustomers(List<String> fields);

    Customer getCustomerById(String id, List<String> fields);

    boolean exists(String customerId);

    Customer createCustomer(Customer customer);

    Customer updateCustomer(Customer customer);

    void validateCustomer(String customerId);
}
