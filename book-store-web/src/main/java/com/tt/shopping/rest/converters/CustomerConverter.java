package com.tt.shopping.rest.converters;

import com.tt.shopping.api.model.customer.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CustomerConverter implements Function<com.tt.shopping.rest.json.request.customer.Customer,
        Customer> {

    private final AddressConverter addressConverter;

    private final ContactMediumConverter contactMediumConverter;

    private final PaymentMethodConverter paymentMethodConverter;

    @Autowired
    public CustomerConverter(AddressConverter addressConverter,
                             ContactMediumConverter contactMediumConverter,
                             PaymentMethodConverter paymentMethodConverter) {
        this.addressConverter = addressConverter;
        this.contactMediumConverter = contactMediumConverter;
        this.paymentMethodConverter = paymentMethodConverter;
    }

    @Override
    public Customer apply(com.tt.shopping.rest.json.request.customer.Customer source) {
        return Customer.builder()
                .id(source.getId())
                .name(source.getName())
                .characteristic(source.getCharacteristic())
                .address(source.getAddress()
                        .stream()
                        .map(address -> this.addressConverter.apply(address))
                        .collect(Collectors.toList()))
                .contactMedium(source.getContactMedium()
                        .stream()
                        .map(contactMedium -> this.contactMediumConverter.apply(contactMedium))
                        .collect(Collectors.toList()))
                .paymentMethod(source.getPaymentMethod()
                        .stream()
                        .map(paymentMethod -> this.paymentMethodConverter.apply(paymentMethod))
                        .collect(Collectors.toList()))
                .build();
    }
}
