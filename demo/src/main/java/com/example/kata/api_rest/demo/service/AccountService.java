package com.example.kata.api_rest.demo.service;

import com.example.kata.api_rest.demo.model.Account;
import com.example.kata.api_rest.demo.model.Operation;
import com.example.kata.api_rest.demo.model.OperationType;
import com.example.kata.api_rest.demo.model.Person;
import com.example.kata.api_rest.demo.repository.AccountRepository;
import com.example.kata.api_rest.demo.repository.OperationRepository;
import com.example.kata.api_rest.demo.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final PersonRepository personRepository;

    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;

    public AccountService(PersonRepository personRepository, AccountRepository accountRepository, OperationRepository operationRepository) {
        this.personRepository = personRepository;
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }

    @Transactional(readOnly = true)
    public List<Account> getHistory(String userName) {
        Person person = personRepository.findByUserName(userName);
        List<Account> result = new ArrayList<>(accountRepository.findByPerson(person));
        result.addAll(accountRepository.findByAuhtorisedPersons(person));
        return result;
    }

    @Transactional
    public Optional<Operation> execute(/*String userName, */OperationType type, long accountId, double amount) {
        Optional<Account> accountOpt = accountRepository.findById(accountId);
        if (!accountOpt.isPresent())
        {
            return Optional.empty();
        }

        Account account = accountOpt.get();

        /*if (!userIsAuthorised(account, userName))
        {
            return Optional.empty();
        }*/

        Operation operation = new Operation(type, amount);
        account.addOperation(operation);
        operationRepository.save(operation);  // Should we do cascade on update from account.operations?
        accountRepository.save(account);
        return Optional.of(operation);
    }

    private boolean userIsAuthorised(Account account, String userName) {
        return true;
    }
}
