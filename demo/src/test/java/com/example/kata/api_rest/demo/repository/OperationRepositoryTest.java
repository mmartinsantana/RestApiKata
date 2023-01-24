package com.example.kata.api_rest.demo.repository;

import com.example.kata.api_rest.demo.model.Account;
import com.example.kata.api_rest.demo.model.Operation;
import com.example.kata.api_rest.demo.model.OperationType;
import com.example.kata.api_rest.demo.model.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class OperationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OperationRepository operationRepository;

    @Test
    public void whenFindByPerson_thenReturnBankAccount() {
        // given
        Person alex = new Person("alex");
        entityManager.persist(alex);

        Account account = new Account();
        account.setPerson(alex);
        entityManager.persist(account);

        Operation operation = new Operation(account, OperationType.DEPOSIT, 0.);
        entityManager.persist(operation);

        entityManager.flush();

        // when
        List<Operation> foundOperations = operationRepository.findByAccount(account);

        // then
        assertThat(foundOperations).hasSize(1);
        assertThat(foundOperations).contains(operation);
    }
}
