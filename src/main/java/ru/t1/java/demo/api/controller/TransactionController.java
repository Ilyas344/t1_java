package ru.t1.java.demo.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.java.demo.aspect.annotation.HandlingResult;
import ru.t1.java.demo.aspect.annotation.LogException;
import ru.t1.java.demo.aspect.annotation.Track;
import ru.t1.java.demo.kafka.KafkaTransactionProducer;
import ru.t1.java.demo.model.dto.TransactionDto;
import ru.t1.java.demo.service.ParseService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1/transaction")
public class TransactionController {
    private final ParseService<TransactionDto> accountService;

    private final KafkaTransactionProducer kafkaTransactionProducer;
    @Value("${t1.kafka.topic.transaction}")
    private String topic;

    @LogException
    @Track
    @GetMapping(value = "/parse")
    @HandlingResult
    public void parseSource() {
        List<TransactionDto> transactionDtos = accountService.parseJson("TRANSACTION_DATA", TransactionDto[].class);
        transactionDtos.forEach(dto -> {
            kafkaTransactionProducer.sendTo(topic, dto);
        });
    }
}
