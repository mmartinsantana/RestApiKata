package com.example.kata.api_rest.demo.repository;

import com.example.kata.api_rest.demo.model.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @Test
    public void whenFindByName_thenReturnPerson() {
        // given
        Person alex = new Person("alex");
        entityManager.persist(alex);
        entityManager.flush();

        // when
        Person found = employeeRepository.findByName(alex.getName());

        // then
        assertEquals(found.getName(), alex.getName());
    }

}