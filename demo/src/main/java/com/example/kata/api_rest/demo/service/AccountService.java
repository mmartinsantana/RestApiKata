package com.example.kata.api_rest.demo.service;

import com.example.kata.api_rest.demo.model.Account;
import com.example.kata.api_rest.demo.model.Operation;
import com.example.kata.api_rest.demo.model.OperationType;
import com.example.kata.api_rest.demo.repository.AccountRepository;
import com.example.kata.api_rest.demo.repository.OperationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;

    public AccountService(AccountRepository accountRepository, OperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }

    @Transactional
    public Optional<Operation> withdraw(long accountId, double amount) {


        return createOperationResponse(accountId, OperationType.WITHDRAWAL, amount);
    }

    @Transactional
    public Optional<Operation> deposit(long accountId, double amount) {
        return createOperationResponse(accountId, OperationType.DEPOSIT, amount);
    }


    private Optional<Operation> createOperationResponse(long accountId, OperationType type, double amount) {
        Optional<Account> accountOpt = accountRepository.findById(accountId);
        if (!accountOpt.isPresent())
        {
            return Optional.empty();
        }

        Account account = accountOpt.get();

        Operation operation = new Operation(type, amount);
        account.addOperation(operation);
        operationRepository.save(operation);  // Should we do cascade on update from account.operations?
        accountRepository.save(account);
        return Optional.of(operation);
    }}
