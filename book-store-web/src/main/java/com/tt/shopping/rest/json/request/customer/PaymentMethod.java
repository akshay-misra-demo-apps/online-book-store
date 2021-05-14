package com.tt.shopping.rest.json.request.customer;

import com.tt.shopping.api.model.Characteristic;
import com.tt.shopping.api.model.customer.contants.PaymentMethodType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaymentMethod {

    private PaymentMethodType paymentMethodType;

    private boolean defaultMethod;

    private List<Characteristic> characteristic;
}
