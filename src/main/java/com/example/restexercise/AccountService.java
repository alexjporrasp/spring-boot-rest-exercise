package com.example.restexercise;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.UnknownCurrencyException;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountDataAccessService accountDataAccessService;

    @Autowired
    public AccountService(AccountDataAccessService accountDataAccessService) {
        this.accountDataAccessService = accountDataAccessService;
    }

    public List<Account> getAllAccounts() {
        return accountDataAccessService.selectAllAccounts();
    }

    public Account getAccountById(UUID accountId) {
        try {
            return accountDataAccessService.findAccountById(accountId);
        } catch (EmptyResultDataAccessException ex) {
            throw new NoSuchElementException("Account does not exist");
        }
    }

    public void addNewAccount(@NotNull Account account) {
        UUID newAccountId = UUID.randomUUID();
        if (!account.getTreasury() && account.getBalance() < 0) {
            throw new IllegalArgumentException(
                "Balance cannot be negative on a non treasury account. Balance: " + account.getBalance()
            );
        }
        if (Objects.isNull(account.getCurrency())) {
            throw new IllegalArgumentException(
                    "Currency must be specified"
            );
        }
        try {
            Monetary.getCurrency(account.getCurrency());
        } catch (UnknownCurrencyException ex) {
            throw new IllegalArgumentException(
                    "Unsupported currency: " + account.getCurrency()
            );
        }
        accountDataAccessService.insertAccount(newAccountId, account);
    }

    // TODO(alexjporrasp): Truncate to two decimals.
    public void transferMoney(UUID originId, UUID targetId, double amount) {
        // Make sure both accounts exist.
        Account origin, target;
        try {
            origin = getAccountById(originId);
            target = getAccountById(targetId);
        } catch (NoSuchElementException ex) {
            throw new NoSuchElementException("Both account must exist: " + originId + ", " + targetId);
        }

        if (!origin.getTreasury() && amount > origin.getBalance()) {
            throw new IllegalArgumentException("Not enough balance: " + origin.getBalance());
        }

        MonetaryAmount transferredMoney = Monetary.getDefaultAmountFactory()
                .setCurrency(origin.getCurrency()).setNumber(amount).create();
        MonetaryAmount targetMoney = Monetary.getDefaultAmountFactory()
                .setCurrency(target.getCurrency()).setNumber(target.getBalance()).create();

        CurrencyConversion conversion = MonetaryConversions.getConversion(target.getCurrency());
        targetMoney = targetMoney.add(transferredMoney.with(conversion));

        accountDataAccessService.transferMoney(
                originId,
                targetId,
                origin.getBalance() - amount,
                targetMoney.getNumber().doubleValue()
        );
    }
}
