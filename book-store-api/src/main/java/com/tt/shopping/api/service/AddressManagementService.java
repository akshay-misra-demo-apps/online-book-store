package com.tt.shopping.api.service;

import com.tt.shopping.api.model.customer.Address;

import java.util.List;

public interface AddressManagementService {

    List<Address> getAddress();

    Address getAddressById(String id);

    Address createAddress(Address Address);

    Address updateAddress(Address product);
}
