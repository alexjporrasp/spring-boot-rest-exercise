package com.example.restexercise;

import java.util.UUID;

public class Account {
    private final UUID accountId;
    private final String name;
    private final String currency;
    private final Double balance;
    private final Boolean treasury;

    public Account(UUID accountId, String name, String currency, Double balance, Boolean treasury) {
        this.accountId = accountId;
        this.name = name;
        this.currency = currency;
        this.balance = balance;
        this.treasury = treasury;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }

    public Double getBalance() {
        return balance;
    }

    public Boolean getTreasury() {
        return treasury;
    }
}
