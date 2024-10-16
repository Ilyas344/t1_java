package ru.t1.java.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AccountType {
    DEBIT("Дебетовый счёт"),
    CREDIT("Кредитный счёт");

    AccountType(String accountType) {
    }
}

