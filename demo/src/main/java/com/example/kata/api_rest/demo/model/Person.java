package com.example.kata.api_rest.demo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Person {

    @Id
    @SequenceGenerator(name = "person_sequence",
            sequenceName = "person_sequence",
            initialValue = 1, allocationSize = 20)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_sequence")
    private Long id;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "person")
    //@JsonManagedReference // @JsonIgnore
    private List<Account> accounts = new ArrayList<>();

    @ManyToMany(mappedBy = "authorisedPersons")
    //@JsonManagedReference // @JsonIgnore
    private Set<Account> authorisedAccounts = new HashSet<>();

    @OneToMany(mappedBy = "person")
    //@JsonManagedReference // @JsonIgnore
    private List<AppUser> appUsers = new ArrayList<>();

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

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account) {
        addInternalAccount(account);
        account.setPerson(this);
    }

    void addInternalAccount(Account account) {
        accounts.add(account);
    }

    public Set<Account> getAuthorisedAccounts() {
        return authorisedAccounts;
    }

    public List<AppUser> getAppUsers() {
        return appUsers;
    }

    public void setAppUsers(List<AppUser> appUsers) {
        this.appUsers = appUsers;
    }

    public void setAuthorisedAccounts(Set<Account> authorisedAccounts) {
        this.authorisedAccounts = authorisedAccounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Person other))
            return false;

        return id != null &&
                id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void addAuthorisedAccount(Account account) {
        authorisedAccounts.add(account);
    }
}
