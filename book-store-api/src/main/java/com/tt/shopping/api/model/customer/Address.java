package com.tt.shopping.api.model.customer;

import com.tt.shopping.api.model.HasHref;
import com.tt.shopping.api.model.customer.contants.AddressType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Data
@SuperBuilder
@NoArgsConstructor
@Document(collection="addresses")
public class Address extends HasHref {

    private String customerId;

    private String country;

    private String stateOrProvince;

    private String city;

    private String street1;

    private String street2;

    private String postCode;

    private String phoneNumber;

    private AddressType addressType;

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
