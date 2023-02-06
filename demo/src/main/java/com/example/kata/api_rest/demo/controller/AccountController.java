package com.example.kata.api_rest.demo.controller;

import com.example.kata.api_rest.demo.model.Account;
import com.example.kata.api_rest.demo.model.Operation;
import com.example.kata.api_rest.demo.model.OperationType;
import com.example.kata.api_rest.demo.repository.AccountRepository;
import com.example.kata.api_rest.demo.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping(AccountController.PATH)
public class AccountController {

    public static final String PATH="/api/account";

    public static final String SUB_PATH_WITHDRAW = "/withdrawal";

    public static final String SUB_PATH_DEPOSIT = "/deposit";

    private final AccountService accountService;

    private final AccountRepository accountRepository;

    public AccountController(AccountService accountService, AccountRepository accountRepository) {
        this.accountService = accountService;
        this.accountRepository = accountRepository;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Account>> getAccounts() {
        return ResponseEntity.ok().body(accountRepository.findAll());
    }

    /*@GetMapping(value = "/find/{id}")
    public ResponseEntity<List<Account>> getAccountsForPerson(@PathVariable(value = "id") long id) {
        Optional<Person> person = personRepository.findById(id);

        if (person.isPresent()) {
            List<Account> accounts = new ArrayList<>(person.get().getAccounts());
            return ResponseEntity.ok().body(accounts);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/

    @PostMapping(value = SUB_PATH_WITHDRAW)
    public ResponseEntity<Operation> withdraw(@RequestParam long accountId, @RequestParam double amount) {
        Optional<Operation> operation = accountService.execute(OperationType.WITHDRAWAL, accountId, amount);

        return operation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = SUB_PATH_DEPOSIT)
    public ResponseEntity<Operation> deposit(@RequestParam long accountId, @RequestParam double amount) {
        Optional<Operation> operation = accountService.execute(OperationType.DEPOSIT, accountId, amount);

        return operation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/operation")
    public ResponseEntity<Operation> operate(@RequestBody Operation operationIn) {
        OperationType operationType = operationIn.getType();

        Optional<Operation> operationOut = accountService.execute(operationType, operationIn.getAccount().getId(), operationIn.getAmount());

        return operationOut.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
