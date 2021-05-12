package com.tt.shopping.api.service;

import com.tt.shopping.api.model.customer.Customer;

import java.util.List;

/**
 * Created by Akshay Misra on 11/13/2018.
 */
public interface CustomerManagementService {

    List<Customer> getCustomers();

    Customer getCustomerById(String id);

    boolean exists(String customerId);

    Customer createCustomer(Customer customer);

    Customer updateCustomer(String id, Customer product);

    void validateCustomer(String customerId);

    void validateCustomer(Customer customer);
}
