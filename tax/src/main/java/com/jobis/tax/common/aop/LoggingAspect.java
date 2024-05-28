package com.jobis.tax.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {


    @Around("execution(* com.jobis.tax..ctrl..*.*(..))")
    public Object logRequestAndResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        // 요청 파라미터 로깅
        Object[] args = joinPoint.getArgs();
        log.info("Request Parameters: ");
        for (Object arg : args) {
            log.info(arg.toString());
        }

        // 메서드 실행
        Object result = joinPoint.proceed();

        // 응답 값 로깅
        log.info("Response: " + result.toString());

        return result;
    }
}