package ru.t1.java.demo.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.ExecutionTimeMetric;
import ru.t1.java.demo.kafka.KafkaClientProducer;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MetricAspect {
    @Value("${t1.kafka.topic.metrics}")
    private String metricsTopic;
    private final KafkaClientProducer kafkaClientProducer;

    @Around("@within(Metric)")
    public Object logExecTime(ProceedingJoinPoint pJoinPoint) throws Throwable {
        log.info("Вызов метода: {}", pJoinPoint.getSignature().toShortString());
        long timeForExecute = pJoinPoint.getTarget().getClass().getAnnotation(Metric.class).time();
        long beforeTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = pJoinPoint.proceed();
        } finally {
            long executeTime = System.currentTimeMillis() - beforeTime;
            log.info("Время исполнения: {} ms", executeTime);
            if (executeTime > timeForExecute) {
                ExecutionTimeMetric executionTimeMetric = ExecutionTimeMetric.builder()
                        .executionTime(executeTime)
                        .methodSignature(pJoinPoint.getSignature().getName())
                        .args(Arrays.stream(pJoinPoint.getArgs()).toList())
                        .build();
                kafkaClientProducer.sendToWithHeader(metricsTopic, executionTimeMetric, "METRICS");
            }
        }

        return result;
    }
}
