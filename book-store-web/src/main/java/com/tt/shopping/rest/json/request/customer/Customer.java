package com.tt.shopping.rest.json.request.customer;

import com.tt.shopping.api.model.Characteristic;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Customer {

    private String id;

    private String name;

    private List<ContactMedium> contactMedium;

    private List<Address> address;

    private List<Characteristic> characteristic;

    private List<PaymentMethod> paymentMethod;
}
