package ru.t1.java.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;
import ru.t1.java.demo.kafka.KafkaClientProducer;
import ru.t1.java.demo.kafka.MessageDeserializer;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.Transaction;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class KafkaConfig {

    @Value("${t1.kafka.consumer.group-id}")
    private String groupId;
    @Value("${t1.kafka.bootstrap.server}")
    private String servers;
    @Value("${t1.kafka.session.timeout.ms:15000}")
    private String sessionTimeout;
    @Value("${t1.kafka.max.partition.fetch.bytes:300000}")
    private String maxPartitionFetchBytes;
    @Value("${t1.kafka.max.poll.records:1}")
    private String maxPollRecords;
    @Value("${t1.kafka.max.poll.interval.ms:3000}")
    private String maxPollIntervalsMs;
    @Value("${t1.kafka.topic.client_id_registered}")
    private String clientTopic;
    @Value("${t1.kafka.topic.account}")
    private String accountTopic;
    @Value("${t1.kafka.topic.transaction}")
    private String transactionTopic;


    @Bean
    public ConsumerFactory<String, Account> accountConsumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties(null);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "ru.t1.java.demo.model");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public KafkaListenerContainerFactory<?> accountListenerFactory(ConsumerFactory<String, Account> accountConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Account> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(accountConsumerFactory);
        factory.setBatchListener(false);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, Client> clientConsumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties(null);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "ru.t1.java.demo.model");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public KafkaListenerContainerFactory<?> clientListenerFactory(ConsumerFactory<String, Client> clientConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Client> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(clientConsumerFactory);
        factory.setBatchListener(false);
        return factory;
    }

  
    @Bean
    public ConsumerFactory<String, Transaction> transactionConsumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties(null);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "ru.t1.java.demo.model");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public KafkaListenerContainerFactory<?> transactionListenerFactory(ConsumerFactory<String, Transaction> transactionConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Transaction> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(transactionConsumerFactory);
        factory.setBatchListener(false);
        return factory;
    }

    @Bean
    DefaultKafkaProducerFactory<String, Object> objectProducerFactory(KafkaProperties properties) {
        Map<String, Object> producerProperties = properties.buildProducerProperties(null);
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(producerProperties);
    }

    @Bean
    KafkaTemplate<String, Object> objectKafkaTemplate(DefaultKafkaProducerFactory<String, Object> objectProducerFactory) {
        return new KafkaTemplate<>(objectProducerFactory);
    }
}