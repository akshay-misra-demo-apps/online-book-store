package com.tt.shopping.rest.converters;

import com.tt.shopping.customer.api.model.Address;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AddressConverter implements Function<com.tt.shopping.rest.json.request.customer.Address,
        Address> {

    @Override
    public Address apply(com.tt.shopping.rest.json.request.customer.Address source) {
        return Address.builder()
                .id(source.getId())
                .street1(source.getStreet1())
                .street2(source.getStreet2())
                .city(source.getCity())
                .stateOrProvince(source.getStateOrProvince())
                .postCode(source.getPostCode())
                .country(source.getCountry())
                .phoneNumber(source.getPhoneNumber())
                .addressType(source.getAddressType())
                .build();
    }
}
