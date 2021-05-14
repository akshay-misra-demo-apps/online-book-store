package com.tt.shopping.customer.api.model;

import com.tt.shopping.common.api.model.HasHrefName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@SuperBuilder
@NoArgsConstructor
@Document(collection="billingAccounts")
public class BillingAccount extends HasHrefName {

    private Boolean defaultBillingAccount;

    private String customerId;

    private String accountNumber;

    private String currency;

    private LocalDateTime actualStartDate;

    private List<PaymentMethod> paymentMethod;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BillingAccount)) return false;
        BillingAccount that = (BillingAccount) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
