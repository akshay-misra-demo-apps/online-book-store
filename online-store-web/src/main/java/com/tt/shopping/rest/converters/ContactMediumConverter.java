package com.tt.shopping.rest.converters;

import com.tt.shopping.customer.api.model.ContactMedium;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ContactMediumConverter implements Function<com.tt.shopping.rest.json.request.customer.ContactMedium,
        ContactMedium> {

    @Override
    public ContactMedium apply(com.tt.shopping.rest.json.request.customer.ContactMedium source) {
        return ContactMedium.builder()
                .id(source.getId())
                .preferred(source.isPreferred())
                .type(source.getType())
                .characteristic(source.getCharacteristic())
                .build();
    }
}
