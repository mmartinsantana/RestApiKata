package com.example.kata.api_rest.demo.repository;

import com.example.kata.api_rest.demo.model.Account;
import com.example.kata.api_rest.demo.model.Operation;
import com.example.kata.api_rest.demo.model.OperationType;
import com.example.kata.api_rest.demo.model.Person;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
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

    Person alex;

    Account account;

    Operation operation;

    double addedAmount = 1.;

    @BeforeEach
    public void setUp() {
        alex = new Person("alex");
        entityManager.persist(alex);

        account = new Account();
        account.setPerson(alex);
        entityManager.persist(account);

        operation = new Operation(account, OperationType.DEPOSIT, addedAmount);
        entityManager.persist(operation);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void testBidirectional() {
        assertThat(account.getOperations().get(0)).isEqualTo(operation);
        assertThat(operation.getAccount()).isEqualTo(account);
    }


    @Test
    public void testFindByAccount() {
        // when
        List<Operation> foundOperations = operationRepository.findByAccount(account);

        // then
        assertThat(foundOperations).hasSize(1);
        assertThat(foundOperations).contains(operation);

        Operation foundOperation = foundOperations.get(0);
        Account foundAccount = foundOperation.getAccount();
        assertThat(foundAccount.getBalance()).isEqualTo(addedAmount);
        assertThat(foundAccount.getBalance()).isEqualTo(operation.getBalance());
    }
}
