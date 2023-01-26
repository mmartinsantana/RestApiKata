package com.example.kata.api_rest.demo.controller;

import com.example.kata.api_rest.demo.model.Account;
import com.example.kata.api_rest.demo.model.Operation;
import com.example.kata.api_rest.demo.model.OperationType;
import com.example.kata.api_rest.demo.model.Person;
import com.example.kata.api_rest.demo.repository.AccountRepository;
import com.example.kata.api_rest.demo.repository.OperationRepository;
import com.example.kata.api_rest.demo.repository.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@AutoConfigureJsonTesters
@WebMvcTest(AccountController.class)
public class AccountControllerTest {
    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private OperationRepository operationRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<Operation> jsonOperation;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JacksonTester<Operation> json;

    @Test
    public void canRetrieveByIdWhenExists() throws Exception {
        // given
        long accountId = 2;
        double withdrawnAmount = 2;
        
        Person person = new Person("Test");
        person.setId(1L);
        Account account = new Account();
        account.setId(accountId);
        account.setPerson(person);

        given(accountRepository.findById(2L))
                .willReturn(Optional.of(account));

        Operation operation = new Operation(OperationType.WITHDRAWAL, withdrawnAmount);
        operation.setAccount(account);
        operation.setBalance(-withdrawnAmount);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(AccountController.PATH + AccountController.SUB_PATH_WITHDRAW)
                .accept(MediaType.APPLICATION_JSON)
                .queryParam("accountId", String.valueOf(accountId))
                .queryParam("amount", String.valueOf(withdrawnAmount))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();


        // then
        OffsetDateTime dateTime = getOffsetDateTime(response);
        operation.setDateTime(dateTime);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(json.write(operation).toString())
                .contains(response.getContentAsString());
    }

    private static OffsetDateTime getOffsetDateTime(MockHttpServletResponse response) throws JsonProcessingException, UnsupportedEncodingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response.getContentAsString());
        String dateTime = jsonNode.get("dateTime").asText();
        return OffsetDateTime.parse(dateTime);
    }


/*
    @Test
    public void testWithdraw_2() throws Exception {
        // given
        long accountId = 2;
        double withdrawnAmount = 2;

        Person person = new Person("Test");
        person.setId(1L);
        Account account = new Account();
        account.setId(accountId);
        account.setPerson(person);

        given(accountRepository.findById(2L))
                .willReturn(Optional.of(account));

        Operation operation = new Operation(OperationType.WITHDRAWAL, withdrawnAmount);
        operation.setAccount(account);
        operation.setBalance(-withdrawnAmount);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(AccountController.PATH + AccountController.SUB_PATH_WITHDRAW)
                .accept(MediaType.APPLICATION_JSON)
                .queryParam("accountId", String.valueOf(accountId))
                .queryParam("amount", String.valueOf(withdrawnAmount))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();


        // then
        OffsetDateTime dateTime = getOffsetDateTime(response);
        operation.setDateTime(dateTime);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(json.write(operation).toString())
                .contains(response.getContentAsString());

        when()

        when(employeeDAO.getAllEmployees()).thenReturn(employees);

        Employees result = employeeController.getEmployees();

        assertThat(result.getEmployeeList().size()).isEqualTo(2);
        assertThat(result.getEmployeeList().get(0).getFirstName()).isEqualTo(employee1.getFirstName());
        assertThat(result.getEmployeeList().get(1).getFirstName()).isEqualTo(employee2.getFirstName());
    }*/

}
