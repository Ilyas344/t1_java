package ru.t1.java.demo.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ExceptionTracingAspect {

    private final KafkaTemplate<Object, Object> kafkaTemplate;
    @Value("${t1.kafka.topic.error_trace")
    private String errorTraceTopic;

    @Around("@annotation(ru.t1.java.demo.aspect.annotation.ExceptionTracing)")
    public Object traceExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            sendErrorToKafka(joinPoint, throwable);
            throw throwable;
        }
    }

    private void sendErrorToKafka(ProceedingJoinPoint joinPoint, Throwable throwable) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        Object[] args = joinPoint.getArgs();

        Map<String, Object> errorData = new HashMap<>();
        errorData.put("methodName", methodName);
        errorData.put("parameters", args);
        errorData.put("stackTrace", throwable.getStackTrace());

        kafkaTemplate.send(errorTraceTopic, errorData);
    }
}