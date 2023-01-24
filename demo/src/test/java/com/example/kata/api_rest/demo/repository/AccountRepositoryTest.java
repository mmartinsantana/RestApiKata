package com.example.kata.api_rest.demo.repository;

import com.example.kata.api_rest.demo.model.Account;
import com.example.kata.api_rest.demo.model.Person;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    Person alex;

    Account account;

    @BeforeEach
    public void setUp() {
        alex = new Person("alex");
        entityManager.persist(alex);

        account = new Account();
        account.setPerson(alex);
        entityManager.persist(account);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void testBidirectional() {
        assertThat(account.getBalance()).isEqualTo(0.);

        assertThat(alex.getAccounts().iterator().next()).isEqualTo(account);
        assertThat(account.getPerson()).isEqualTo(alex);
    }

    @Test
    public void whenFindByPerson() {
        // when
        Set<Account> foundAccounts = accountRepository.findByPerson(alex);

        // then
        assertThat(foundAccounts).hasSize(1);
        assertThat(foundAccounts).contains(account);
    }
}
