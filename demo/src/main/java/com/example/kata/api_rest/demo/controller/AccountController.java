package com.example.kata.api_rest.demo.controller;

import com.example.kata.api_rest.demo.model.Account;
import com.example.kata.api_rest.demo.model.Operation;
import com.example.kata.api_rest.demo.model.OperationType;
import com.example.kata.api_rest.demo.model.Person;
import com.example.kata.api_rest.demo.repository.AccountRepository;
import com.example.kata.api_rest.demo.repository.OperationRepository;
import com.example.kata.api_rest.demo.repository.PersonRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping(AccountController.PATH)
public class AccountController {

    public static final String PATH="/api/account";

    public static final String SUB_PATH_WITHDRAW = "/withdrawal";

    private final PersonRepository personRepository;

    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;

    public AccountController(PersonRepository personRepository, AccountRepository accountRepository,
                             OperationRepository operationRepository) {
        this.personRepository = personRepository;
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Account>> getAccounts() {
        return ResponseEntity.ok().body(accountRepository.findAll());
    }

    @GetMapping(value = "/find/{id}")
    public ResponseEntity<List<Account>> getAccountsForPerson(@PathVariable(value = "id") long id) {
        Optional<Person> person = personRepository.findById(id);

        if (person.isPresent()) {
            List<Account> accounts = new ArrayList<>(person.get().getAccounts());
            return ResponseEntity.ok().body(accounts);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = SUB_PATH_WITHDRAW)
    public ResponseEntity<Operation> withdraw(@RequestParam long accountId, @RequestParam double amount) {

        Optional<Account> accountOpt = accountRepository.findById(accountId);
        if (accountOpt.isPresent()) {
            return createOperationResponse(accountOpt.get(), OperationType.WITHDRAWAL, amount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

        private ResponseEntity<Operation> createOperationResponse(Account account, OperationType type, double amount) {
        Operation operation = new Operation(type, amount);
        account.addOperation(operation);
        operationRepository.save(operation);  // Should we do cascade on update from account.operations?
        accountRepository.save(account);
        return ResponseEntity.ok(operation);
    }

}
