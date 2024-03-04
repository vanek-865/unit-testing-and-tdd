package com.acme.banking.dbo.service;

import com.acme.banking.dbo.domain.Account;
import com.acme.banking.dbo.domain.Client;
import com.acme.banking.dbo.domain.SavingAccount;

import java.util.Collection;

public class Processing {
    private ClientRepository clientRepository;
    private SavingAccountRepository savingAccountRepository;
    private CashApi cash;

    public Processing(ClientRepository clientRepository, CashApi cash, SavingAccountRepository savingAccountRepository) {
        this.clientRepository = clientRepository;
        this.cash = cash;
        this.savingAccountRepository = savingAccountRepository;
    }


    public Client createClient(String name) {
        var client = new Client(clientRepository.generateId(), name);
        return clientRepository.save(client);
    }

    public Collection<Account> getAccountsByClientId(int clientId) {
        return null; //TODO
    }

    public void transfer(int fromAccountId, int toAccountId, double amount) {

        if (fromAccountId < 0) throw new IllegalArgumentException("Incorrect from account id");
        if (toAccountId < 0) throw new IllegalArgumentException("Incorrect to account id");

        SavingAccount fromAccount = savingAccountRepository.getAccountById(fromAccountId);
        SavingAccount toAccount = savingAccountRepository.getAccountById(toAccountId);

        if (fromAccount == null) throw new IllegalStateException("From account is null");
        if (toAccount == null) throw new IllegalStateException("To account is null");

        fromAccount.withDraw(amount);
        toAccount.addAmount(amount);

        savingAccountRepository.save(fromAccount);
        savingAccountRepository.save(toAccount);

    }

    public void cash(double amount, int fromAccountId) {
        cash.log(amount, fromAccountId);
    }
}
