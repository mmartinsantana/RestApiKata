package com.example.kata.api_rest.demo.service;

import com.example.kata.api_rest.demo.model.*;
import com.example.kata.api_rest.demo.repository.AccountRepository;
import com.example.kata.api_rest.demo.repository.OperationRepository;
import com.example.kata.api_rest.demo.repository.PersonRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Mock
    private PersonRepository personRepository;

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
    public void withdrawTest()  {
        // given
        double withdrawnAmount = 2;

        OffsetDateTime before = OffsetDateTime.now();

        // when
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        // then
        Optional<Operation> operationOpt = accountService.execute(OperationType.WITHDRAWAL, ACCOUNT_ID, withdrawnAmount);

        assertTrue(operationOpt.isPresent());
        Operation operation = operationOpt.get();

        assertEquals(operation.getAmount(), withdrawnAmount);
        assertEquals(operation.getBalance(), -withdrawnAmount);
        assertEquals(operation.getType(), OperationType.WITHDRAWAL);
        assertTrue(operation.getDateTime().isBefore(OffsetDateTime.now()));
        assertTrue(operation.getDateTime().isAfter(before));
    }

    @Test
    public void historyTest()  {
        // given
        double withdrawnAmount = 2;

        String userName = "test_user";

        AppUser user = new AppUser();
        user.setUserName(userName);
        user.setPerson(person);

        OffsetDateTime before = OffsetDateTime.now();

        // when
        when(personRepository.findByUserName(userName)).thenReturn(person);
        when(accountRepository.findByPerson(person)).thenReturn(List.of(account));
        when(accountRepository.findByAuhtorisedPersons(person)).thenReturn(Lists.emptyList());

        // then
        List<Account> accounts = accountService.getHistory(userName);

        assertEquals(accounts.size(), 1);
        assertEquals(accounts.get(0), account);
    }

    @Test
    public void historyTestWithAuthorisedAcc() {
        // given
        double withdrawnAmount = 2;

        String userName = "test_user";

        AppUser user = new AppUser();
        user.setUserName(userName);
        user.setPerson(person);

        Account account_auth = new Account();
        account_auth.addAuthorisedPerson(person);

        OffsetDateTime before = OffsetDateTime.now();

        // when
        when(personRepository.findByUserName(userName)).thenReturn(person);
        when(accountRepository.findByPerson(person)).thenReturn(List.of(account));
        when(accountRepository.findByAuhtorisedPersons(person)).thenReturn(List.of(account_auth));

        // then
        List<Account> accounts = accountService.getHistory(userName);

        assertThat(accounts, containsInAnyOrder(account, account_auth));
    }
}
