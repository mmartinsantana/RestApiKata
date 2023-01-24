package com.example.kata.api_rest.demo.repository;

import com.example.kata.api_rest.demo.model.Account;
import com.example.kata.api_rest.demo.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

    // TODO Paginate?
    public List<Operation> findByAccount(Account account);
}
