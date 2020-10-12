package com.example.restexercise;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class RestExerciseApplicationTests {

	@Autowired
	private AccountController accountController;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
		assertThat(accountController).isNotNull();
	}

	@Test
	public void basePathShowsAllAccounts() throws Exception {
		mockMvc.perform(get("/api/accounts"))
				.andExpect(status().isOk())
				.andExpect(content().json("[{\"accountId\":\"65d251e0-0bfc-11eb-adc1-0242ac120002\",\"name\":\"Alice\",\"currency\":\"USD\",\"balance\":100.0,\"treasury\":true},{\"accountId\":\"824164ce-0bfc-11eb-adc1-0242ac120002\",\"name\":\"Bob\",\"currency\":\"EUR\",\"balance\":20.0,\"treasury\":false},{\"accountId\":\"9e4e0e28-0c9d-11eb-adc1-0242ac120002\",\"name\":\"Dani\",\"currency\":\"EUR\",\"balance\":15.0,\"treasury\":false}]"));
	}

	@Test
	public void findAccountByIdOfExistingAccountIsOk() throws Exception {
		mockMvc.perform(get("/api/accounts/65d251e0-0bfc-11eb-adc1-0242ac120002"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"accountId\":\"65d251e0-0bfc-11eb-adc1-0242ac120002\",\"name\":\"Alice\",\"currency\":\"USD\",\"balance\":100.0,\"treasury\":true}"));
	}

	@Test
	public void findAccountByOfNonExistingAccountIdNotFound() throws Exception {
		mockMvc.perform(get("/api/accounts/00000000-0000-0000-0000-000000000000"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void insertNewAccountHappyCaseIsOk() throws Exception {
		mockMvc.perform(post("/api/accounts")
				.content("{\"name\": \"Charlie\", \"currency\": \"EUR\", \"balance\": 100.0, \"treasury\": true}")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void insertAccountWithNegativeBalanceAndNoTreasuryIsBadRequest() throws Exception {
		mockMvc.perform(post("/api/accounts")
				.content("{\"name\": \"Charlie\", \"currency\": \"EUR\", \"balance\": -100.0, \"treasury\": false}")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()
		);
	}

	@Test
	public void insertAccountWithUnsupportedCurrencyIsBadRequest() throws Exception {
		mockMvc.perform(post("/api/accounts")
				.content("{\"name\": \"Charlie\", \"currency\": \"AAA\", \"balance\": 100.0, \"treasury\": false}")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void transferMoneySameCurrencyHappyCase() throws Exception {
		mockMvc.perform(put("/api/accounts/transfer")
				.param("originId", "824164ce-0bfc-11eb-adc1-0242ac120002")
				.param("targetId", "9e4e0e28-0c9d-11eb-adc1-0242ac120002")
				.param("amount", "2.50"))
				.andExpect(status().isOk());
	}

	@Test
	public void transferMoneySameCurrencyNotEnoughBalanceIsBadRequest() throws Exception {
		mockMvc.perform(put("/api/accounts/transfer")
				.param("originId", "824164ce-0bfc-11eb-adc1-0242ac120002")
				.param("targetId", "9e4e0e28-0c9d-11eb-adc1-0242ac120002")
				.param("amount", "100"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void transferMoneyTreasuryNegativeBalanceIsOk() throws Exception {
		mockMvc.perform(put("/api/accounts/transfer")
				.param("originId", "65d251e0-0bfc-11eb-adc1-0242ac120002")
				.param("targetId", "824164ce-0bfc-11eb-adc1-0242ac120002")
				.param("amount", "1000"))
				.andExpect(status().isOk());
		mockMvc.perform(put("/api/accounts/transfer")
				.param("originId", "65d251e0-0bfc-11eb-adc1-0242ac120002")
				.param("targetId", "824164ce-0bfc-11eb-adc1-0242ac120002")
				.param("amount", "1000"))
				.andExpect(status().isOk());
	}

	@Test
	// Since exchange rates change very often. To test the actual conversion
	// it is needed to mock the currency conversion service.
	public void transferMoneyDistinctCurrencyHappyCase() throws Exception {
		mockMvc.perform(put("/api/accounts/transfer")
				.param("originId", "65d251e0-0bfc-11eb-adc1-0242ac120002")
				.param("targetId", "824164ce-0bfc-11eb-adc1-0242ac120002")
				.param("amount", "20"))
				.andExpect(status().isOk());
	}
}
