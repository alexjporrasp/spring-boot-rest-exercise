package com.example.restexercise;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

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
        accountDataAccessService.insertAccount(newAccountId, account);
    }
}
