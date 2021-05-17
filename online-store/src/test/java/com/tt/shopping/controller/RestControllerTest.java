package com.tt.shopping.controller;

import com.tt.shopping.common.api.model.Characteristic;
import com.tt.shopping.customer.api.model.Address;
import com.tt.shopping.customer.api.model.BillingAccount;
import com.tt.shopping.customer.api.model.Customer;
import com.tt.shopping.customer.api.model.PaymentMethod;
import com.tt.shopping.customer.api.model.contants.AddressType;
import com.tt.shopping.customer.api.model.contants.CustomerCategory;
import com.tt.shopping.customer.api.model.contants.PaymentMethodType;
import com.tt.shopping.init.ApplicationInitializationProcessor;
import com.tt.shopping.order.api.model.OrderCancelRequest;
import com.tt.shopping.order.api.model.constants.CancellationReason;
import com.tt.shopping.rest.constants.RestUris;
import com.tt.shopping.rest.json.request.order.OrderItem;
import com.tt.shopping.rest.json.request.order.ProductOrder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RestControllerTest extends AbstractTest {

    @Autowired
    private ApplicationInitializationProcessor applicationInitializationProcessor;

    @Override
    @Before
    public void setup() {
        super.setup();
    }

    @Test
    @Order(1)
    public void test1_createCustomer() throws Exception {
        final Customer customer = new Customer();
        customer.setFirstName("T-First");
        customer.setLastName("T-Last");
        customer.setPrimaryEmail("test@primary.com");
        customer.setSecondaryEmail("test@secondary.com");
        customer.setCustomerCategory(CustomerCategory.RESIDENTIAL);
        final List<Characteristic> chars = new ArrayList<>();
        final Characteristic characteristic = Characteristic.builder().name("membership").value("VIP").build();
        chars.add(characteristic);
        customer.setCharacteristic(chars);

        final String inputJson = super.mapToJson(customer);
        final MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(RestUris.CUSTOMER_URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson))
                .andReturn();

        final int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(201, status);
    }

    @Test
    @Order(2)
    public void test2_getCustomers() throws Exception {
        final MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(RestUris.CUSTOMER_URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        final int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(200, status);
        final String content = mvcResult.getResponse().getContentAsString();
        Customer[] customers = super.mapFromJson(content, Customer[].class);
        Assert.assertTrue(customers.length > 0);
    }

    @Test
    @Order(3)
    public void test3_updateCustomer() throws Exception {
        final Customer customerToUpdate = this.getTestCustomer(Collections.EMPTY_LIST);
        customerToUpdate.setFirstName("T-First-Updated");
        final List<Address> addresses = new ArrayList<>(1);
        final Address address = Address.builder()
                .addressType(AddressType.HOME)
                .street1("test street 1")
                .street2("test street 2")
                .city("test city")
                .stateOrProvince("test province")
                .country("test country")
                .postCode("000000")
                .build();
        addresses.add(address);
        customerToUpdate.setAddress(addresses);

        final List<BillingAccount> billingAccounts = new ArrayList<>(1);
        final BillingAccount billingAccount = new BillingAccount();
        billingAccount.setDefaultBillingAccount(false);
        billingAccount.setCurrency("USD");
        final List<PaymentMethod> paymentMethods = new ArrayList<>(1);
        final PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setPaymentMethodType(PaymentMethodType.PAYPAL);
        paymentMethod.setDefaultMethod(false);
        paymentMethod.setCharacteristic(Arrays.asList(new Characteristic("accountNumber", "5709890721")));
        paymentMethods.add(paymentMethod);
        billingAccount.setPaymentMethod(paymentMethods);
        billingAccounts.add(billingAccount);
        customerToUpdate.setBillingAccount(billingAccounts);

        final String uri = RestUris.CUSTOMER_URI +  "/" + customerToUpdate.getId();
        final String requestBody = super.mapToJson(customerToUpdate);
        final MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .patch(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody))
                .andReturn();
        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
        final Customer updatedCustomer = this.getCustomerById(customerToUpdate.getId(),
                Arrays.asList("address", "billingAccount"));
        Assert.assertEquals("T-First-Updated", updatedCustomer.getFirstName());
        Assert.assertTrue(!updatedCustomer.getAddress().isEmpty());
        Assert.assertTrue(!updatedCustomer.getBillingAccount().isEmpty());
    }

    public Customer getTestCustomer(final List<String> fields) throws Exception {
        final String getUri = RestUris.CUSTOMER_URI + "?fields=" + String.join(",", fields);;
        final MvcResult getResult = mvc.perform(MockMvcRequestBuilders.get(getUri)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        final int status = getResult.getResponse().getStatus();
        Assert.assertEquals(200, status);
        final String content = getResult.getResponse().getContentAsString();
        final Customer[] customers = super.mapFromJson(content, Customer[].class);
        Customer customerToUpdate = null;
        for(final Customer customer : customers) {
            if ("test@primary.com".equals(customer.getPrimaryEmail())) {
                customerToUpdate = customer;
                break;
            }
        }

        return customerToUpdate;
    }

    public Customer getCustomerById(final String id, List<String> fields) throws Exception {
        final String uri = RestUris.CUSTOMER_URI +  "/" + id + "?fields=" + String.join(",", fields);
        final MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        final int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(200, status);
        final String content = mvcResult.getResponse().getContentAsString();
        return super.mapFromJson(content, Customer.class);
    }

    @Test
    @Order(4)
    public void test4_createProductOrder() throws Exception {
        final Customer customer = this.getTestCustomer(Arrays.asList("address", "billingAccount"));
        final BillingAccount billingAccount = customer.getBillingAccount().stream().findFirst().get();
        final com.tt.shopping.rest.json.request.customer.PaymentMethod paymentMethod =
                com.tt.shopping.rest.json.request.customer.PaymentMethod.builder()
                        .paymentMethodType(PaymentMethodType.PAYPAL)
                        .characteristic(Arrays.asList(new Characteristic(
                                "accountNumber",
                                "5709890721")))
                        .build();
        final List<OrderItem> orderItems = new ArrayList<>(1);
        final OrderItem orderItem = OrderItem.builder()
                .billingAccountRef(billingAccount.getId())
                .quantity(1)
                .sku("BKROMT02")
                .build();
        orderItems.add(orderItem);
        final ProductOrder order = ProductOrder.builder()
                .customerId(customer.getId())
                .description("Placing test order.")
                .deliveryAddressRef(customer.getAddress()
                        .stream().map(address -> address.getId()).
                                findFirst().get())
                .externalId("EXT-TEST-001")
                .notificationContact("test@contact.com")
                .paymentMethod(paymentMethod)
                .orderItem(orderItems)
                .build();

        final String inputJson = super.mapToJson(order);
        final MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post(RestUris.PRODUCT_ORDER_URI )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson))
                .andReturn();

        final int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(201, status);

        final String content = mvcResult.getResponse().getContentAsString();
        com.tt.shopping.order.api.model.ProductOrder persisted = super.mapFromJson(content,
                com.tt.shopping.order.api.model.ProductOrder.class);
        Assert.assertTrue(persisted.getId() != null);
    }

    @Test
    @Order(5)
    public void test5_getProductOrderListByCustomer() throws Exception {
        final com.tt.shopping.order.api.model.ProductOrder[] orders = this.getTestProductOrders();
        Assert.assertTrue(orders.length > 0);
    }

    @Test
    @Order(6)
    public void test6_cancelProductOrder() throws Exception {
        final com.tt.shopping.order.api.model.ProductOrder[] orders = this.getTestProductOrders();
        for (final com.tt.shopping.order.api.model.ProductOrder order : orders) {
            this.cancelProductOrder(order);
        }
        this.applicationInitializationProcessor.init();
    }

    private void cancelProductOrder(com.tt.shopping.order.api.model.ProductOrder order) throws Exception {
        final OrderCancelRequest cancelRequest = OrderCancelRequest.builder()
                .orderId(order.getId())
                .cancellationReason(CancellationReason.OTHER)
                .cancellationDescription("Cancel order test")
                .build();
        final String inputJson = super.mapToJson(cancelRequest);
        final MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post(RestUris.PRODUCT_ORDER_URI + "cancel" )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson))
                .andReturn();

        final int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(200, status);
    }

    private com.tt.shopping.order.api.model.ProductOrder[] getTestProductOrders() throws Exception {
        final Customer customer = this.getTestCustomer(Collections.EMPTY_LIST);
        final MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .get(RestUris.PRODUCT_ORDER_URI + customer.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        final int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(200, status);
        final String content = mvcResult.getResponse().getContentAsString();

        return super.mapFromJson(content, com.tt.shopping.order.api.model.ProductOrder[].class);
    }
}
