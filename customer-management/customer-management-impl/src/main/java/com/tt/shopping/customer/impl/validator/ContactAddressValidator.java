package com.tt.shopping.customer.impl.validator;

import com.tt.shopping.common.api.exceptions.IncorrectRequestException;
import com.tt.shopping.common.api.validator.PreValidator;
import com.tt.shopping.customer.api.model.Customer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

@Component
public class ContactAddressValidator <T extends Customer> implements PreValidator<T> {

    @Override
    public List<String> getActions() {
        return Arrays.asList("CREATE");
    }

    @Override
    public int getOrder() {
        return 7;
    }

    @Override
    public void validate(final Customer customer) {
        if (!CollectionUtils.isEmpty(customer.getAddress())) {
            customer.getAddress().stream()
                    .forEach(address -> {
                        if (address.getCity() == null
                                || address.getCountry() == null
                                || address.getPostCode() == null
                                || address.getStreet1() == null
                                || address.getStateOrProvince() == null
                                || address.getPhoneNumber() == null
                                || address.getAddressType() == null) {
                            throw new IncorrectRequestException("Incorrect request, one of the required field is" +
                                    " missing from following list: 'street1', 'city', 'stateOrProvince'" +
                                    ", 'country', 'postCode', 'phoneNumber' or 'addressType'.");
                        }
                    });
        }
    }
}
