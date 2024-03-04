package com.acme.banking.dbo.service;

import com.acme.banking.dbo.domain.Client;
import com.acme.banking.dbo.domain.SavingAccount;

public interface ClientRepository {
    public Client save(Client client);

    public int generateId();

}
