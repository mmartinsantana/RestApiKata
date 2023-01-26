package com.example.kata.api_rest.demo.service;

import com.example.kata.api_rest.demo.model.Account;
import com.example.kata.api_rest.demo.model.Operation;
import com.example.kata.api_rest.demo.model.OperationType;
import com.example.kata.api_rest.demo.repository.AccountRepository;
import com.example.kata.api_rest.demo.repository.OperationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;

    public AccountService(AccountRepository accountRepository, OperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }

    public Optional<Operation> withdraw(long accountId, double amount) {
        Optional<Account> accountOpt = accountRepository.findById(accountId);

        return accountOpt.flatMap(account -> createOperationResponse(account, OperationType.WITHDRAWAL, amount));
    }

    private Optional<Operation> createOperationResponse(Account account, OperationType type, double amount) {
        Operation operation = new Operation(type, amount);
        account.addOperation(operation);
        operationRepository.save(operation);  // Should we do cascade on update from account.operations?
        accountRepository.save(account);
        return Optional.of(operation);
    }}
