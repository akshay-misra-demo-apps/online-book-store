package com.tt.shopping.api.model.customer;

import com.tt.shopping.api.model.HasHrefName;
import com.tt.shopping.api.model.customer.contants.CustomerCategory;
import com.tt.shopping.api.model.customer.contants.CustomerStatus;
import com.tt.shopping.api.model.Characteristic;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@Document(collection="customers")
public class Customer extends HasHrefName {

    private String firstName;

    private String lastName;

    @Indexed(name = "primary_email_index")
    private String primaryEmail;

    private String secondaryEmail;

    @Indexed(name = "account_number_index")
    private String accountNumber;

    private CustomerCategory customerCategory;

    private CustomerStatus status;

    private String statusReason;

    private List<Characteristic> characteristic;

    @Indexed(name = "contact_medium_index")
    private List<ContactMedium> contactMedium;

    private List<Address> address;

    private List<BillingAccount> billingAccount;
}
