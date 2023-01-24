package com.example.kata.api_rest.demo.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "person")
    private Set<Account> accounts;

    Person() {}

    public Person(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Account> getBankAccounts() {
        return accounts;
    }

    public void setBankAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }
}
