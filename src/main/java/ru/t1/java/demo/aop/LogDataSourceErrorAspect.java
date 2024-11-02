package ru.t1.java.demo.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.service.DataSourceErrorLogService;


@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogDataSourceErrorAspect {
    private final DataSourceErrorLogService dataSourceErrorLogService;

    @AfterThrowing(pointcut = "@annotation(LogDataSourceError)",
            throwing = "e")
    public void afterThrowingCRUDErrorAdvice(JoinPoint joinPoint, Exception e) {
        log.info("AFTER THROWING IN: {}", joinPoint.getSignature().getName());
        dataSourceErrorLogService.saveDataSourceError(DataSourceErrorLog.builder()
                .message(e.getMessage())
                .stacktrace(ExceptionUtils.getStackTrace(e))
                .methodSignature(joinPoint.getSignature().getName())
                .build());
        log.info("WRITE TO DATABASE EXCEPTION IN: {}", joinPoint.getSignature().getName());

    }



}
