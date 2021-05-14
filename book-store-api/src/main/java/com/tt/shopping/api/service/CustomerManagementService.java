package com.tt.shopping.api.service;

import com.tt.shopping.api.model.customer.Customer;

import java.util.List;

/**
 * Created by Akshay Misra on 11/13/2018.
 */
public interface CustomerManagementService {

    List<Customer> getCustomers(List<String> fields);

    Customer getCustomerById(String id, List<String> fields);

    boolean exists(String customerId);

    Customer createCustomer(Customer customer);

    Customer updateCustomer(Customer customer);

    void validateCustomer(String customerId);
}
