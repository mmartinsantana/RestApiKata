package com.example.kata.api_rest.demo.controller;

import com.example.kata.api_rest.demo.model.Account;
import com.example.kata.api_rest.demo.model.Person;
import com.example.kata.api_rest.demo.repository.AccountRepository;
import com.example.kata.api_rest.demo.repository.PersonRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final PersonRepository personRepository;

    private final AccountRepository accountRepository;

    public AccountController(PersonRepository personRepository, AccountRepository accountRepository) {
        this.personRepository = personRepository;
        this.accountRepository = accountRepository;
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
    public void putWithdrawal(Person person, Account account, double amount) {

    }

}
