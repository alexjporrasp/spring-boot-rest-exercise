package com.example.restexercise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
