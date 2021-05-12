package com.tt.shopping.api.model.customer;

import com.tt.shopping.api.model.Characteristic;
import com.tt.shopping.api.model.customer.contants.ContactType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Objects;

@Data
@Builder
//@NoArgsConstructor
public class ContactMedium {

    @Id
    private String id;

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
