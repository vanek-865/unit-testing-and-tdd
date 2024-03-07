package com.acme.banking.dbo.domain;

import java.util.Collection;

import static java.util.Collections.unmodifiableCollection;

public class Branch {
    private Collection<Account> accounts; //TODO

    private int id;

    public Branch(Collection<Account> accounts, int id) {
        this.accounts = accounts;
        this.id = id;
    }

    public Collection<Account> getAccounts() {
        return unmodifiableCollection(accounts);
    }

    public Collection<Branch> getChildren() {
        return null; //TODO
    }

    public int getId() {
        return id;
    }
}
