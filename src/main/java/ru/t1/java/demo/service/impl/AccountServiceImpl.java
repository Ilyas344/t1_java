package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.t1.java.demo.kafka.KafkaAccountProducer;
import ru.t1.java.demo.kafka.KafkaClientProducer;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.service.AccountService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository repository;

    private final KafkaAccountProducer kafkaProducer;

    @Override
    public void registerAccounts(List<Account> accounts) {
        Assert.notEmpty(accounts, "Список счетов не может быть пустым");
        repository.saveAll(accounts)
                .stream()
                .map(Account::getId)
                .forEach(kafkaProducer::send);
    }

}
