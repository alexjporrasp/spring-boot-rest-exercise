package com.example.restexercise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("api/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("{id}")
    public Account getAccountById(@PathVariable("id") UUID accountId) {
        try {
            return accountService.getAccountById(accountId);
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "There is no account with id: " + accountId, ex);
        }
    }

    // Content-Type must be set to application/json in Request header.
    @PostMapping
    public void addNewAccount(@RequestBody Account account) {
        try {
            accountService.addNewAccount(account);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Illegal Argument: " + ex.getMessage(), ex
            );
        }
    }
}
