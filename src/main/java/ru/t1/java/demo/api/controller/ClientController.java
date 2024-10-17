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
import ru.t1.java.demo.kafka.KafkaClientProducer;
import ru.t1.java.demo.model.dto.ClientDto;
import ru.t1.java.demo.service.ParseService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1/client")
public class ClientController {

    private final ParseService<ClientDto> clientService;

    private final KafkaClientProducer kafkaClientProducer;
    @Value("${t1.kafka.topic.client_registration}")
    private String topic;

    @LogException
    @Track
    @GetMapping(value = "/parse")
    @HandlingResult
    public void parseSource() {
        List<ClientDto> clientDtos = clientService.parseJson("MOCK_DATA", ClientDto[].class);
        clientDtos.forEach(dto -> {
            kafkaClientProducer.sendTo(topic, dto);
        });
    }

}
