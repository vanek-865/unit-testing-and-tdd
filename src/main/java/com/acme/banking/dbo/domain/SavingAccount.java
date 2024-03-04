package com.acme.banking.dbo.domain;

public class SavingAccount implements Account {
    private int id;
    private Client client;
    private double amount;

    public SavingAccount(int id, Client client, double amount) {

        if (id < 0) throw new IllegalArgumentException("id should be more zero");
        if (client == null) throw new IllegalArgumentException("client should be not empty");
        if (amount < 0) throw new IllegalArgumentException("amount should be more zero");

        this.id = id;
        this.client = client;
        this.amount = amount;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Client getClient() {
        return client;
    }

    public void withDraw(double amount) {
        if (amount < 0 ) throw new IllegalArgumentException("Amount is incorrect");
        //if (this.getAmount() - amount < 0d) throw new IllegalStateException("Not many");
        if (Double.compare(this.getAmount(), amount) < 0) throw new IllegalStateException("Not many");

        this.amount -= amount;
    }

    public void addAmount(double amount){
        if (amount < 0 ) throw new IllegalArgumentException("Amount is incorrect");
        this.amount += amount;
    }
}
