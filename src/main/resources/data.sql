DROP TABLE IF EXISTS ACCOUNTS;

CREATE TABLE ACCOUNTS (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(250),
    currency VARCHAR(3),
    balance DOUBLE DEFAULT 0.0,
    treasury BOOLEAN DEFAULT FALSE
);

INSERT INTO ACCOUNTS (id, name, currency, balance, treasury) VALUES
('65d251e0-0bfc-11eb-adc1-0242ac120002', 'Alice', 'USD', 100.0, TRUE),
('824164ce-0bfc-11eb-adc1-0242ac120002', 'Bob', 'EUR', 20.0, FALSE);