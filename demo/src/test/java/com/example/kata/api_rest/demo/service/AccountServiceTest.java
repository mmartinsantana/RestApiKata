package com.example.kata.api_rest.demo.service;

import com.example.kata.api_rest.demo.model.Account;
import com.example.kata.api_rest.demo.model.Operation;
import com.example.kata.api_rest.demo.model.OperationType;
import com.example.kata.api_rest.demo.model.Person;
import com.example.kata.api_rest.demo.repository.AccountRepository;
import com.example.kata.api_rest.demo.repository.OperationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private OperationRepository operationRepository;

    @Test
    public void withdrawTest() throws Exception {
        // given
        long accountId = 2;
        double withdrawnAmount = 2;
        
        Person person = new Person("Test");
        person.setId(1L);

        Account account = new Account();
        account.setId(accountId);
        account.setPerson(person);

        OffsetDateTime before = OffsetDateTime.now();

        // when
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // then
        Optional<Operation> operationOpt = accountService.execute(OperationType.WITHDRAWAL, accountId, withdrawnAmount);

        assertTrue(operationOpt.isPresent());
        Operation operation = operationOpt.get();

        assertThat(operation.getAmount()).isEqualTo(withdrawnAmount);
        assertThat(operation.getBalance()).isEqualTo(-withdrawnAmount);
        assertThat(operation.getType()).isEqualTo(OperationType.WITHDRAWAL);
        assertThat(operation.getDateTime()).isBefore(OffsetDateTime.now());
        assertThat(operation.getDateTime()).isAfter(before);
    }
}
