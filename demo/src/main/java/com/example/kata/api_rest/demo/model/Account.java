package com.example.kata.api_rest.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JsonManagedReference // JsonIgnore
    private Person person;

    @OneToMany(mappedBy = "account")
    @OrderBy(value = "dateTime")
    @JsonBackReference // @JsonIgnore
    List<Operation> operations = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
        person.addInternalAccount(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Account))
            return false;

        Account other = (Account) o;

        return id != null &&
                id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    public void addOperation(Operation operation) {
        OperationType type = operation.getType();
        double amount = operation.getAmount();

        double balance = calcNewBalance(type, amount);

        operation.setBalance(balance);

        operation.setAccount(this);
        operations.add(operation);
    }

    private double calcNewBalance(OperationType newOperationType, double newAmount) {
        return switch (newOperationType) {
            case DEPOSIT -> getBalance() + newAmount;
            case WITHDRAWAL -> getBalance() - newAmount;
        };
    }

    public double getBalance() {
        // TODO: IF operations not loaded -> Improve querying last op... rather than loading the full list
        if (operations.isEmpty()) {
            return 0;
        } else {
            return operations.get(operations.size()-1).getBalance();
        }
    }
}
