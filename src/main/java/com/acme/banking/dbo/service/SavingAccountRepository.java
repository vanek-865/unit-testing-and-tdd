package com.acme.banking.dbo.service;

import com.acme.banking.dbo.domain.SavingAccount;

public interface SavingAccountRepository {
    SavingAccount getAccountById(int fromAccountId);

    void save(SavingAccount toAccount);
}
