package com.example.kata.api_rest.demo.controller;

import com.example.kata.api_rest.demo.model.Account;
import com.example.kata.api_rest.demo.model.Operation;
import com.example.kata.api_rest.demo.model.OperationType;
import com.example.kata.api_rest.demo.repository.AccountRepository;
import com.example.kata.api_rest.demo.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping(AccountController.PATH)
public class AccountController {

    public static final String PATH="/api/account";

    public static final String SUB_PATH_HISTORY = "/history";

    public static final String SUB_PATH_OPERATION = "/operation";

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

    @GetMapping(value = SUB_PATH_HISTORY)
    public ResponseEntity<List<Account>> getOperations(Principal principal) {
        String name = principal.getName();
        return ResponseEntity.ok().body(accountService.getHistory(name));
    }

    @PostMapping(value = SUB_PATH_OPERATION)
    public ResponseEntity<Operation> operate(Principal principal, @RequestBody Operation operationIn) {
        String name = principal.getName();

        OperationType operationType = operationIn.getType();

        Optional<Operation> operationOut = accountService.execute(name, operationType, operationIn.getAccount().getId(), operationIn.getAmount());

        return operationOut.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
