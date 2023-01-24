package com.example.kata.api_rest.demo.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private OffsetDateTime dateTime;

    @Enumerated(EnumType.STRING)
    private OperationType type;

    private double amount;

    private double balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Account account;

    Operation() {}

    public Operation(Account account, OperationType type, double amount) {
        this.balance = calcNewBalance(account, type, amount);

        setAccount(account);
        setType(type);
        setAmount(amount);
        setDateTime(OffsetDateTime.now());
    }

    private static double calcNewBalance(Account account, OperationType newOperationType, double newAmount) {
        return switch (newOperationType) {
            case DEPOSIT -> account.getBalance() + newAmount;
            case WITHDRAW -> account.getBalance() - newAmount;
        };
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OffsetDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(OffsetDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
        account.addInternalOperation(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Operation other))
            return false;

        return id != null &&
                id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
