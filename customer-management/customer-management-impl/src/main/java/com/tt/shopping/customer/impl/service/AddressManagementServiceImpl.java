package com.tt.shopping.customer.impl.service;

import com.tt.shopping.common.api.exceptions.ResourceNotFoundException;
import com.tt.shopping.customer.api.model.Address;
import com.tt.shopping.customer.api.service.AddressManagementService;
import com.tt.shopping.customer.impl.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressManagementServiceImpl implements AddressManagementService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<Address> getAddress() {
        return this.addressRepository.findAll();
    }

    @Override
    public Address getAddressById(String id) {
        return this.addressRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Address with id '" +  id + "' not found."));
    }

    @Override
    public Address createAddress(Address address) {
        return this.addressRepository.save(address);
    }

    @Override
    public Address updateAddress(Address address) {
        return this.addressRepository.save(address);
    }
}
