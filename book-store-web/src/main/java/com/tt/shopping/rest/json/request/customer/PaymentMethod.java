package com.tt.shopping.rest.json.request.customer;

import com.tt.shopping.api.model.Characteristic;
import com.tt.shopping.api.model.customer.contants.PaymentMethodType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaymentMethod {

    private String name;

    private PaymentMethodType paymentMethodType;

    private List<Characteristic> characteristic;
}
