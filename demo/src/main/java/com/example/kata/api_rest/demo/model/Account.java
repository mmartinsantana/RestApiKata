package com.example.kata.api_rest.demo.model;

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
    private Person person;

    @OneToMany(mappedBy = "account")
    @OrderBy(value = "dateTime")
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

    void addInternalOperation(Operation operation) {
        operations.add(operation);
    }

    public double getBalance() {
        // TODO: Improve querying last op... rather than loading the full list
        if (operations.isEmpty()) {
            return 0;
        } else {
            return operations.get(operations.size()-1).getBalance();
        }
    }
}
