package com.tt.shopping.customer.api.service;


import com.tt.shopping.customer.api.model.Address;

import java.util.List;

public interface AddressManagementService {

    List<Address> getAddress();

    Address getAddressById(String id);

    Address createAddress(Address Address);

    Address updateAddress(Address product);
}
