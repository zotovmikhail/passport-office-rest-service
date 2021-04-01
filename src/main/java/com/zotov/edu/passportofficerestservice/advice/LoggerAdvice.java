package com.zotov.edu.passportofficerestservice.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@AllArgsConstructor
public class LoggerAdvice {

    private final ObjectMapper objectMapper;

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().toString();
        String methodName = joinPoint.getSignature().getName();
        Object[] methodArgs = joinPoint.getArgs();
        log.info("Method invoked: {}:{}()\nRequest:\n{}.", className, methodName,
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(methodArgs));
        Object response = joinPoint.proceed();
        log.info("{}:{}()\nResponse:\n {}", className, methodName,
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
        return response;
    }

    @Around(value = "within(@org.springframework.web.bind.annotation.RestControllerAdvice *)")
    public Object logAdviceController(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().toString();
        String methodName = joinPoint.getSignature().getName();
        log.info("Exception handler invoked: {}:{}", className, methodName);
        Object response = joinPoint.proceed();
        log.info("{}:{}()\nResponse:\n {}", className, methodName,
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
        return response;
    }

    @AfterThrowing(pointcut = "execution(* com.zotov.edu.passportofficerestservice.controller..* (..)) ", throwing = "exception")
    public void logExceptions(Throwable exception) {
        log.debug("Exception occurred:", exception);
    }
}
