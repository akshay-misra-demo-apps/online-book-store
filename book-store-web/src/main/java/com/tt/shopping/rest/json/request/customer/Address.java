package com.tt.shopping.rest.json.request.customer;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class Address {

    private String id;

    private String country;

    private String stateOrProvince;

    private String city;

    private String street1;

    private String street2;

    private String postCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        if (this.getId() == null || ((Address) o).getId() == null) return false;
        Address address = (Address) o;
        return getId().equals(address.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
