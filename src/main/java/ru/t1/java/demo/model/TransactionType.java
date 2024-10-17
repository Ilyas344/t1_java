package ru.t1.java.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TransactionType {


    WRITE_OFF("Списание"), PAYMENT( "Начисление"), CANCEL( "Отмена");

    TransactionType(String transactionType) {
    }
}
