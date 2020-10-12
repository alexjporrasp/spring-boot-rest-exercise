package com.example.restexercise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
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
}
