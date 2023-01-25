package com.example.kata.api_rest.demo.controller;

import com.example.kata.api_rest.demo.model.Person;
import com.example.kata.api_rest.demo.repository.PersonRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    private final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Person>> getPersons() {
        return ResponseEntity.ok().body(personRepository.findAll());
    }

}
