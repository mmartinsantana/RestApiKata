package com.example.kata.api_rest.demo.repository;

import com.example.kata.api_rest.demo.model.Person;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class PersonRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PersonRepository employeeRepository;

    Person alex;

    @BeforeEach
    public void setUp() {
        alex = new Person("alex");
        entityManager.persist(alex);
        entityManager.flush();
    }

    @Test
    public void whenFindByName() {
        // when
        Person found = employeeRepository.findByName(alex.getName());

        // then
        assertEquals(found.getName(), alex.getName());
    }

}