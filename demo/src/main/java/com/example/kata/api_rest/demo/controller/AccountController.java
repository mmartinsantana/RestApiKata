package com.example.kata.api_rest.demo.controller;

import com.example.kata.api_rest.demo.model.Account;
import com.example.kata.api_rest.demo.model.Person;
import com.example.kata.api_rest.demo.repository.AccountRepository;
import com.example.kata.api_rest.demo.repository.PersonRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    PersonRepository personRepository;

    AccountRepository accountRepository;

    @GetMapping(value = "/persons/")
    public ResponseEntity<List<Person>> getPersons() {
        return ResponseEntity.ok().body(personRepository.findAll());
    }

    @GetMapping(value = "/{id}/")
    public ResponseEntity<List<Account>> getAccountsForPerson(@PathVariable("id") long id) {
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
