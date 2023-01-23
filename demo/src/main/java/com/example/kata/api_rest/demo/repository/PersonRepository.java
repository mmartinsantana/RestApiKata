package com.example.kata.api_rest.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.kata.api_rest.demo.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    // TODO Pageable?

    public Person findByName(String name);

}
