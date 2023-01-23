package com.example.kata.api_rest.demo.repository;

import com.example.kata.api_rest.demo.model.BankAccount;
import com.example.kata.api_rest.demo.model.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BankAccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Test
    public void whenFindByPerson_thenReturnBankAccount() {
        // given
        Person alex = new Person("alex");
        entityManager.persist(alex);

        BankAccount bankAccount = new BankAccount();
        bankAccount.setPerson(alex);
        entityManager.persist(bankAccount);

        entityManager.flush();

        // when
        Set<BankAccount> foundBankAccounts = bankAccountRepository.findByPerson(alex);

        // then
        assertThat(foundBankAccounts).hasSize(1);
        assertThat(foundBankAccounts).contains(bankAccount);
    }
}
