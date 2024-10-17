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
import ru.t1.java.demo.kafka.KafkaAccountProducer;
import ru.t1.java.demo.model.dto.AccountDto;
import ru.t1.java.demo.service.ParseService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1/account")
public class AccountController {
    private final ParseService<AccountDto> accountService;

    private final KafkaAccountProducer kafkaAccountProducer;
    @Value("${t1.kafka.topic.account}")
    private String topic;

    @LogException
    @Track
    @GetMapping(value = "/parse")
    @HandlingResult
    public void parseSource() {
        List<AccountDto> accountDtos = accountService.parseJson("ACCOUNT_DATA", AccountDto[].class);
        System.out.println(accountDtos);
        accountDtos.forEach(dto -> {
            kafkaAccountProducer.sendTo(topic, dto);
        });
    }
}
