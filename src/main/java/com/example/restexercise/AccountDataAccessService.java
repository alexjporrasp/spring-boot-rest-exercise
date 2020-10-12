package com.example.restexercise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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
