package com.tt.shopping.api.model.customer;

import com.tt.shopping.api.model.Characteristic;
import com.tt.shopping.api.model.HasHref;
import com.tt.shopping.api.model.customer.contants.ContactType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Objects;

@Data
@SuperBuilder
@NoArgsConstructor
public class ContactMedium extends HasHref {

    private boolean preferred;

    private ContactType type;

    private List<Characteristic> characteristic;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContactMedium)) return false;
        ContactMedium that = (ContactMedium) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
