package com.tt.shopping.api.model.customer;

import com.tt.shopping.api.model.Characteristic;
import com.tt.shopping.api.model.HasHrefName;
import com.tt.shopping.api.model.customer.contants.OrderPaymentStatus;
import com.tt.shopping.api.model.customer.contants.PaymentMethodType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
public class OrderPayment extends HasHrefName {

  private OrderPaymentStatus status;

  private Double amount;

  private PaymentMethodType type;

  private List<Characteristic> characteristic;
}
