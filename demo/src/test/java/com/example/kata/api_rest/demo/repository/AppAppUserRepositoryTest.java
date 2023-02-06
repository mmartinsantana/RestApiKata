package com.example.kata.api_rest.demo.repository;

import com.example.kata.api_rest.demo.model.AppUser;
import com.example.kata.api_rest.demo.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AppAppUserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AppUserRepository appUserRepository;

    private Person alexPerson;

    private AppUser alexAppUser;

    @BeforeEach
    public void setUp() {
        alexPerson = new Person("alex");
        entityManager.persist(alexPerson);

        alexAppUser = new AppUser(alexPerson, alexPerson.getName(), alexPerson.getName());
        entityManager.persist(alexAppUser);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void whenFindByName() {
        // when
        AppUser found = appUserRepository.findByUserName(alexAppUser.getUserName());

        // then
        assertEquals(found.getUserName(), alexAppUser.getUserName());
    }

}