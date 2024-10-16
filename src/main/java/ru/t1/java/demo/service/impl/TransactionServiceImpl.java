package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.t1.java.demo.kafka.KafkaClientProducer;
import ru.t1.java.demo.kafka.KafkaTransactionProducer;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.TransactionService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository repository;

    private final KafkaTransactionProducer kafkaProducer;

    @Override
    public void registerTransactions(List<Transaction> transactions) {
        Assert.notEmpty(transactions, "Список транзакций не может быть пустым");
        repository.saveAll(transactions)
                .stream()
                .map(Transaction::getId)
                .forEach(kafkaProducer::send);
    }


}
