package com.example.kata.api_rest.demo.repository;

import com.example.kata.api_rest.demo.model.Account;
import com.example.kata.api_rest.demo.model.Person;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    // These 2 are equivalent... to load the account.operations
    //@Query("select distinct a from Account a left join fetch a.operations where a.person = :person")
    @EntityGraph(attributePaths = "operations")
    public List<Account> findByPerson(Person person);

    @EntityGraph(attributePaths = "operations")
    public List<Account> findByAuhtorisedPersons(Person person);
}
