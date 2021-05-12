package com.tt.shopping.api.model.customer;

import com.tt.shopping.api.model.HasHrefName;
import com.tt.shopping.api.model.customer.contants.CustomerStatus;
import com.tt.shopping.api.model.Characteristic;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@Document(collection="customers")
public class Customer extends HasHrefName {

    private CustomerStatus status;

    private String statusReason;

    private List<ContactMedium> contactMedium;

    private List<Address> address;

    private List<Characteristic> characteristic;

    private List<PaymentMethod> paymentMethod;
}
