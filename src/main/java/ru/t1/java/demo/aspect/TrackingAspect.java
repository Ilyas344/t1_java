package ru.t1.java.demo.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Async
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class TrackingAspect {
  //  private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${t1.method_execution_time}")
    private long methodExecutionTimeThresholdMs;
    @Value("${t1.kafka.topic.metric_trace}")
    private String metricTraceTopic;

    private static final AtomicLong START_TIME = new AtomicLong();

    @Before("@annotation(ru.t1.java.demo.aspect.annotation.Track)")
    public void logExecTime(JoinPoint joinPoint) throws Throwable {
        log.info("Старт метода: {}", joinPoint.getSignature().toShortString());
        START_TIME.addAndGet(System.currentTimeMillis());
    }

    @After("@annotation(ru.t1.java.demo.aspect.annotation.Track)")
    public void calculateTime(JoinPoint joinPoint) {
        long afterTime = System.currentTimeMillis();
        log.info("Время исполнения: {} ms", (afterTime - START_TIME.get()));
        START_TIME.set(0L);
    }

    @Around("@annotation(ru.t1.java.demo.aspect.annotation.Track)")
    public Object logExecTime(ProceedingJoinPoint pJoinPoint) {
        log.info("Вызов метода: {}", pJoinPoint.getSignature().toShortString());
        long beforeTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = pJoinPoint.proceed();//Important
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        long afterTime = System.currentTimeMillis();
        long executionTime = afterTime - beforeTime;
        if (executionTime > methodExecutionTimeThresholdMs) {
            sendMetricToKafka(pJoinPoint, executionTime);
        }
        log.info("Время исполнения: {} ms", (executionTime));
        return result;
    }

    private void sendMetricToKafka(ProceedingJoinPoint joinPoint, long executionTime) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        Object[] args = joinPoint.getArgs();

        Map<String, Object> metricData = new HashMap<>();
        metricData.put("methodName", methodName);
        metricData.put("executionTime", executionTime);
        metricData.put("parameters", args);

      //  kafkaTemplate.send(metricTraceTopic, metricData);
    }
}


