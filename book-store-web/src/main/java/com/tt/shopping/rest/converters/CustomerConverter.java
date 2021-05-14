package com.tt.shopping.rest.converters;

import com.tt.shopping.api.model.customer.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CustomerConverter implements Function<com.tt.shopping.rest.json.request.customer.Customer,
        Customer> {

    private final AddressConverter addressConverter;

    private final ContactMediumConverter contactMediumConverter;

    private final BillingAccountConverter billingAccountConverter;

    @Autowired
    public CustomerConverter(AddressConverter addressConverter,
                             ContactMediumConverter contactMediumConverter,
                             BillingAccountConverter billingAccountConverter) {
        this.addressConverter = addressConverter;
        this.contactMediumConverter = contactMediumConverter;
        this.billingAccountConverter = billingAccountConverter;
    }

    @Override
    public Customer apply(com.tt.shopping.rest.json.request.customer.Customer source) {
        return Customer.builder()
                .id(source.getId())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .primaryEmail(source.getPrimaryEmail())
                .secondaryEmail(source.getSecondaryEmail())
                .customerCategory(source.getCustomerCategory())
                .characteristic(source.getCharacteristic())
                .address(CollectionUtils.isEmpty(source.getAddress()) ? null : source.getAddress()
                        .stream()
                        .map(address -> this.addressConverter.apply(address))
                        .collect(Collectors.toList()))
                .contactMedium(CollectionUtils.isEmpty(source.getContactMedium()) ? null : source.getContactMedium()
                        .stream()
                        .map(contactMedium -> this.contactMediumConverter.apply(contactMedium))
                        .collect(Collectors.toList()))
                .billingAccount(CollectionUtils.isEmpty(source.getBillingAccount()) ? null :source.getBillingAccount()
                        .stream()
                        .map(billingAccount -> this.billingAccountConverter.apply(billingAccount))
                        .collect(Collectors.toList()))
                .build();
    }
}
