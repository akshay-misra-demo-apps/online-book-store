package com.tt.shopping.api.model.customer;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Objects;

@Data
@Builder
//@NoArgsConstructor
public class Address {

    @Id
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
