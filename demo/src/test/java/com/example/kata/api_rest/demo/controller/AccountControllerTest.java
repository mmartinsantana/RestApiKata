package com.example.kata.api_rest.demo.controller;

import com.example.kata.api_rest.demo.model.*;
import com.example.kata.api_rest.demo.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.kata.api_rest.demo.service.AccountServiceTest.PASS;
import static com.example.kata.api_rest.demo.service.AccountServiceTest.USER_NAME;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    private final static long ACCOUNT_ID = 2;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mvc;

    private Person person;

    private AppUser appUser;

    private Account account;

    private JacksonTester<Operation> jsonOperation;

    private JacksonTester<List<Account>> jsonAccountList;

    private Principal mockPrincipal = Mockito.mock(Principal.class);

    @BeforeEach
    public void setup() {
        // We would need this line if we would not use the MockitoExtension
        // MockitoAnnotations.initMocks(this);
        // Here we can't use @AutoConfigureJsonTesters because there isn't a Spring context
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();

        JacksonTester.initFields(this, mapper);

        // MockMvc standalone approach
        mvc = MockMvcBuilders.standaloneSetup(accountController)
                //.setControllerAdvice(new SuperHeroExceptionHandler())
                //.addFilters(new SuperHeroFilter())
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
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
    public void withdraw() throws Exception {
        // given
        double withdrawnAmount = 2;

        when(mockPrincipal.getName()).thenReturn(USER_NAME);

        Operation operation = new Operation(OperationType.WITHDRAWAL, withdrawnAmount);
        operation.setAccount(account);
        operation.setBalance(-withdrawnAmount);

        given(accountService.execute(USER_NAME, OperationType.WITHDRAWAL, account.getId(), withdrawnAmount))
                .willReturn(Optional.of(operation));

        // when
        MockHttpServletResponse response = mvc.perform(
                post(AccountController.PATH + AccountController.SUB_PATH_OPERATION).contentType(MediaType.APPLICATION_JSON).content(
                        jsonOperation.write(operation).getJson()
                ).principal(mockPrincipal))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonOperation.write(operation).getJson());

    }

    @Test
    public void withdrawBeinAuthorised() throws Exception {

        when(mockPrincipal.getName()).thenReturn("a");

        // given
        Person authorisedPerson = new Person("a");
        AppUser authorisedUser=new AppUser(authorisedPerson, "a",  "a");
        authorisedPerson.setAppUser(authorisedUser);
        account.addAuthorisedPerson(authorisedPerson);

        double withdrawnAmount = 2;

        Operation operation = new Operation(OperationType.WITHDRAWAL, withdrawnAmount);
        operation.setAccount(account);
        operation.setBalance(-withdrawnAmount);

        when(accountService.execute(authorisedUser.getUserName(), OperationType.WITHDRAWAL, account.getId(), withdrawnAmount))
                .thenReturn(Optional.of(operation));

        // when
        MockHttpServletResponse response = mvc.perform(
                        post(AccountController.PATH + AccountController.SUB_PATH_OPERATION).contentType(MediaType.APPLICATION_JSON).content(
                                jsonOperation.write(operation).getJson()
                        ).principal(mockPrincipal))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonOperation.write(operation).getJson());
    }

    @Test
    @WithMockUser(roles = "REST", username = USER_NAME)
    public void testDeposit() throws Exception {
        // given
        double savedAmount = 3;

        when(mockPrincipal.getName()).thenReturn(USER_NAME);

        Operation operation = new Operation(OperationType.DEPOSIT, savedAmount);
        operation.setAccount(account);
        operation.setBalance(savedAmount);

        when(accountService.execute(USER_NAME, OperationType.DEPOSIT, account.getId(), savedAmount))
                .thenReturn(Optional.of(operation));

        // when
        MockHttpServletResponse response = mvc.perform(
                        post(AccountController.PATH + AccountController.SUB_PATH_OPERATION).contentType(MediaType.APPLICATION_JSON).content(
                                jsonOperation.write(operation).getJson()
                        ).principal(mockPrincipal))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonOperation.write(operation).getJson());
    }

    @Test
    public void testDepositNoAuth() throws Exception {
        // given
        double savedAmount = 3;

        when(mockPrincipal.getName()).thenReturn("wrong");

        Operation operation = new Operation(OperationType.DEPOSIT, savedAmount);
        operation.setAccount(account);
        operation.setBalance(savedAmount);

        when(accountService.execute("wrong", OperationType.DEPOSIT, account.getId(), savedAmount))
                .thenReturn(Optional.empty());

        // when
        MockHttpServletResponse response = mvc.perform(
                        post(AccountController.PATH + AccountController.SUB_PATH_OPERATION).contentType(MediaType.APPLICATION_JSON).content(
                                jsonOperation.write(operation).getJson()
                        ).principal(mockPrincipal))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isEmpty();

    }

    @Test
    public void testHistory() throws Exception {
        // given
        when(mockPrincipal.getName()).thenReturn(USER_NAME);

        List<Account> accounts = new ArrayList<Account>(appUser.getPerson().getAccounts());
        accounts.addAll(appUser.getPerson().getAuthorisedAccounts());

        when(accountService.getHistory(USER_NAME))
                .thenReturn(accounts);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get(AccountController.PATH + AccountController.SUB_PATH_HISTORY)
                                .accept(MediaType.APPLICATION_JSON)
                                .principal(mockPrincipal))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonAccountList.write(accounts).getJson());
    }

}
