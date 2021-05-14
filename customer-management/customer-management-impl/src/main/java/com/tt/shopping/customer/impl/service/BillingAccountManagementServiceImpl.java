package com.tt.shopping.customer.impl.service;

import com.tt.shopping.common.api.exceptions.ResourceNotFoundException;
import com.tt.shopping.customer.api.model.BillingAccount;
import com.tt.shopping.customer.api.service.BillingAccountManagementService;
import com.tt.shopping.customer.impl.repositories.BillingAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillingAccountManagementServiceImpl implements BillingAccountManagementService {

    private final BillingAccountRepository billingAccountRepository;

    @Autowired
    public BillingAccountManagementServiceImpl(BillingAccountRepository billingAccountRepository) {
        this.billingAccountRepository = billingAccountRepository;
    }

    @Override
    public List<BillingAccount> getBillingAccounts() {
        return this.billingAccountRepository.findAll();
    }

    @Override
    public BillingAccount getBillingAccountById(final String id) {
        return this.billingAccountRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Billing account with id '" +  id + "' not found."));
    }

    @Override
    public boolean exists(final String billingAccountId) {
        return this.billingAccountRepository.existsById(billingAccountId);
    }

    @Override
    public BillingAccount createBillingAccount(final BillingAccount billingAccount) {
        return this.billingAccountRepository.save(billingAccount);
    }

    @Override
    public BillingAccount updateBillingAccount(final BillingAccount billingAccount) {
        return this.billingAccountRepository.save(billingAccount);
    }
}
