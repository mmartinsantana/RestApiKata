package com.example.kata.api_rest.demo.repository;

import com.example.kata.api_rest.demo.message.Sender;
import com.example.kata.api_rest.demo.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MockBean(Sender.class)
@DataJpaTest
public class PersonRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PersonRepository personRepository;

    private Person alex;

    @BeforeEach
    public void setUp() {
        alex = new Person("alex");
        entityManager.persist(alex);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void whenFindByName() {
        // when
        Person found = personRepository.findByName(alex.getName());

        // then
        assertEquals(found.getName(), alex.getName());
    }

}