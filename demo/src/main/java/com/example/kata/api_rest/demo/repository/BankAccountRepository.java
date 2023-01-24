package com.example.kata.api_rest.demo.repository;

import com.example.kata.api_rest.demo.model.Account;
import com.example.kata.api_rest.demo.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface BankAccountRepository extends JpaRepository<Account, Long> {

    public Set<Account> findByPerson(Person person);
}
