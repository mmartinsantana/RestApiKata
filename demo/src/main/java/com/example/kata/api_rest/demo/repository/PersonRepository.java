package com.example.kata.api_rest.demo.repository;

import com.example.kata.api_rest.demo.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    // TODO Pageable?

    public Person findByName(String name);

    @Query("Select p FROM Person p WHERE exists (select 1 from AppUser u where u.person = p and u.userName = ?1)")    //This is using a named query method
    public Person findByUserName(String name);

}
