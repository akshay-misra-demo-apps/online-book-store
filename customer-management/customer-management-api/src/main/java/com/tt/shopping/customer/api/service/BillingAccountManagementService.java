package com.tt.shopping.customer.api.service;


import com.tt.shopping.customer.api.model.BillingAccount;

import java.util.List;

public interface BillingAccountManagementService {

    List<BillingAccount> getBillingAccounts();

    BillingAccount getBillingAccountById(String id);

    boolean exists(String billingAccountId);

    BillingAccount createBillingAccount(BillingAccount billingAccount);

    BillingAccount updateBillingAccount(BillingAccount billingAccount);
}
