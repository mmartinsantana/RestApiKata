package com.example.kata.api_rest.demo.controller;

import com.example.kata.api_rest.demo.model.*;
import com.example.kata.api_rest.demo.repository.AccountRepository;
import com.example.kata.api_rest.demo.repository.OperationRepository;
import com.example.kata.api_rest.demo.repository.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.time.OffsetDateTime;
import java.util.Optional;

import static com.example.kata.api_rest.demo.service.AccountServiceTest.PASS;
import static com.example.kata.api_rest.demo.service.AccountServiceTest.USER_NAME;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureJsonTesters
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    private final static long ACCOUNT_ID = 2;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private OperationRepository operationRepository;

    @MockBean
    private PersonRepository personRepository;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JacksonTester<Operation> json;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private DataSource dataSource;

    private Person person;

    private AppUser appUser;

    private Account account;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        person = new Person("Test");
        person.setId(1L);

        appUser = new AppUser(person, USER_NAME, PASS);
        person.setAppUser(appUser);

        account = new Account();
        account.setId(ACCOUNT_ID);
        account.setPerson(person);
    }

    @Test
    @WithMockUser(roles = "REST", username = USER_NAME)
    public void withdraw() throws Exception {
        // given
        double withdrawnAmount = 2;

        given(accountRepository.findById(ACCOUNT_ID))
                .willReturn(Optional.of(account));

        Operation operation = new Operation(OperationType.WITHDRAWAL, withdrawnAmount);
        operation.setAccount(account);
        operation.setBalance(-withdrawnAmount);

        System.out.println("HEY " + json.write(operation).toString());

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(AccountController.PATH + AccountController.SUB_PATH_OPERATION)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(operation))
                .contentType(MediaType.APPLICATION_JSON);
                //.with(httpBasic("rest", "restPass"));

        MvcResult result = mockMvc
                .perform(requestBuilder)
                .andExpect(jsonPath("$.account.id").value(operation.getAccount().getId()))
                .andExpect(jsonPath("$.amount").value(operation.getAmount()))
                .andExpect(jsonPath("$.balance").value(operation.getBalance()))
                .andExpect(jsonPath("$.type").value(operation.getType().name()))
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        // then
        OffsetDateTime dateTime = getOffsetDateTime(response);
        operation.setDateTime(dateTime);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        // Duplicated with andExpect above ;)
        assertThat(json.write(operation).toString())
                .contains(response.getContentAsString());
    }

    public static String asJsonString(final Object obj) {
        try {

            ObjectMapper objectMapper = JsonMapper.builder()
                    .addModule(new JavaTimeModule())
                    .build();

            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static OffsetDateTime getOffsetDateTime(MockHttpServletResponse response) throws JsonProcessingException, UnsupportedEncodingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response.getContentAsString());
        String dateTime = jsonNode.get("dateTime").asText();
        return OffsetDateTime.parse(dateTime);
    }

    @Test
    @WithMockUser(roles = "REST", username = USER_NAME)
    public void testDeposit() throws Exception {
        // given
        double savedAmount = 3;

        given(accountRepository.findById(ACCOUNT_ID))
                .willReturn(Optional.of(account));

        Operation operation = new Operation(OperationType.DEPOSIT, savedAmount);
        operation.setAccount(account);
        operation.setBalance(savedAmount);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(AccountController.PATH + AccountController.SUB_PATH_OPERATION)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(operation))
                .contentType(MediaType.APPLICATION_JSON);
                //.with(httpBasic("rest", "restPass"));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        // then
        OffsetDateTime dateTime = getOffsetDateTime(response);
        operation.setDateTime(dateTime);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(json.write(operation).toString())
                .contains(response.getContentAsString());
    }

    @Test
    @WithMockUser(roles = "NO_AUTH", username = "wrong")
    public void testDepositNoAuth() throws Exception {
        // given
        double savedAmount = 3;

        given(accountRepository.findById(ACCOUNT_ID))
                .willReturn(Optional.of(account));

        Operation operation = new Operation(OperationType.DEPOSIT, savedAmount);
        operation.setAccount(account);
        operation.setBalance(savedAmount);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(AccountController.PATH + AccountController.SUB_PATH_OPERATION)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(operation))
                .contentType(MediaType.APPLICATION_JSON);
        //.with(httpBasic("rest", "restPass"));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @WithMockUser(roles = "REST", username = "other")
    public void testWithdrawWrongAccount() throws Exception {
        // given
        double withdrawnAmount = 2;

        given(accountRepository.findById(ACCOUNT_ID))
                .willReturn(Optional.of(account));

        Operation operation = new Operation(OperationType.WITHDRAWAL, withdrawnAmount);
        operation.setAccount(account);
        operation.setBalance(-withdrawnAmount);

        System.out.println("HEY " + json.write(operation).toString());

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(AccountController.PATH + AccountController.SUB_PATH_OPERATION)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(operation))
                .contentType(MediaType.APPLICATION_JSON);
        //.with(httpBasic("rest", "restPass"));

        MvcResult result = mockMvc
                .perform(requestBuilder)
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

}
