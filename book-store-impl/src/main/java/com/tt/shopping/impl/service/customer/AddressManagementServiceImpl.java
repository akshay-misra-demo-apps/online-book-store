package com.tt.shopping.impl.service.customer;

import com.tt.shopping.api.exceptions.ResourceNotFoundException;
import com.tt.shopping.api.model.customer.Address;
import com.tt.shopping.api.service.AddressManagementService;
import com.tt.shopping.impl.repositories.AddressRepository;
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
