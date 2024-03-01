package com.acme.banking.dbo.service;

import com.acme.banking.dbo.domain.Account;
import com.acme.banking.dbo.domain.Client;

import java.util.Collection;

public class Processing {
    private ClientRepository clientRepository;
    private CashApi cash;

    public Processing(ClientRepository clientRepository, CashApi cash) {
        this.clientRepository = clientRepository;
        this.cash = cash;
    }


    public Client createClient(String name) {
        var client = new Client(clientRepository.generateId(), name);
        return clientRepository.save(client);
    }

    public Collection<Account> getAccountsByClientId(int clientId) {
        return null; //TODO
    }

    public void transfer(int fromAccountId, int toAccountId, double amount) {
        //TODO
    }

    public void cash(double amount, int fromAccountId) {
        cash.log(amount, fromAccountId);
    }
}
