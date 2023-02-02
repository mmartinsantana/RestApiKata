package com.example.kata.api_rest.demo.controller;

import com.example.kata.api_rest.demo.model.Account;
import com.example.kata.api_rest.demo.model.Operation;
import com.example.kata.api_rest.demo.model.OperationType;
import com.example.kata.api_rest.demo.model.Person;
import com.example.kata.api_rest.demo.repository.AccountRepository;
import com.example.kata.api_rest.demo.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService  accountService;

    @Mock
    private AccountRepository accountRepository;

    private final long ACCOUNT_ID = 2;

    private Person person;

    private Account account;

    @BeforeEach
    private void setUp() {
        person = new Person("Test");
        person.setId(1L);

        account = new Account();
        account.setId(ACCOUNT_ID);
        account.setPerson(person);
    }


    @Test
    public void testWithdraw() throws Exception {
        // given
        double withdrawnAmount = 2;

        Operation operation = new Operation(OperationType.WITHDRAWAL, withdrawnAmount);
        account.addOperation(operation);

        // when
        when(accountService.withdraw(ACCOUNT_ID,withdrawnAmount)).thenReturn(Optional.of(operation));

        // then
        ResponseEntity<Operation> responseEntity = accountController.withdraw(ACCOUNT_ID, withdrawnAmount);

        // TODO: should we return 201 (created + path to operation?)
        // assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
        // assertThat(responseEntity.getHeaders().getLocation().getPath()).isEqualTo("/1");

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        Operation returnedOperation = responseEntity.getBody();
        assertThat(returnedOperation).isEqualTo(operation);
    }

    @Test
    public void testDeposit() throws Exception {
        // given
        double savedAmount = 3;

        Operation operation = new Operation(OperationType.DEPOSIT, savedAmount);
        account.addOperation(operation);

        // when
        when(accountService.withdraw(ACCOUNT_ID,savedAmount)).thenReturn(Optional.of(operation));

        // then
        ResponseEntity<Operation> responseEntity = accountController.withdraw(ACCOUNT_ID, savedAmount);

        // TODO: should we return 201 (created + path to operation?)
        // assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
        // assertThat(responseEntity.getHeaders().getLocation().getPath()).isEqualTo("/1");

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        Operation returnedOperation = responseEntity.getBody();
        assertThat(returnedOperation).isEqualTo(operation);
    }





}
