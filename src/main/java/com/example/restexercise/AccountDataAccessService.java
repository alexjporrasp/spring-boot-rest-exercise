package com.example.restexercise;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Repository
public class AccountDataAccessService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AccountDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    List<Account> selectAllAccounts() {
        return jdbcTemplate.query("SELECT * FROM ACCOUNTS", mapAccountFromDb());
    }

    Account findAccountById(UUID accountId) {
        return (Account) jdbcTemplate.queryForObject(
                "SELECT * FROM ACCOUNTS WHERE id = ?",
                new Object[]{accountId}, mapAccountFromDb());
    }

    int insertAccount(UUID accountId, @NotNull Account account) {
        return jdbcTemplate.update(
                "INSERT INTO ACCOUNTS (id, name, currency, balance, treasury) VALUES (?, ?, ?, ?, ?)",
                accountId,
                account.getName(),
                account.getCurrency(),
                account.getBalance(),
                account.getTreasury());
    }

    @Transactional
    void transferMoney(UUID originId, UUID targetId, Double originNewBalance, Double targetNewBalance) {
        jdbcTemplate.update(
                "UPDATE ACCOUNTS SET balance = ? WHERE id = ?",
                originNewBalance,
                originId
        );
        jdbcTemplate.update(
                "UPDATE ACCOUNTS SET balance = ? WHERE id = ?",
                targetNewBalance,
                targetId
        );
    }

    private RowMapper<Account> mapAccountFromDb() {
        return (resultSet, i) -> new Account(
                UUID.fromString(resultSet.getString("id")),
                resultSet.getString("name"),
                resultSet.getString("currency"),
                resultSet.getDouble("balance"),
                resultSet.getBoolean("treasury")
        );
    }
}
