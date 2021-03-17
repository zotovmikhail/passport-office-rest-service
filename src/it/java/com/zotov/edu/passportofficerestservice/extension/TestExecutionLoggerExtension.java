package com.zotov.edu.passportofficerestservice.extension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestExecutionLoggerExtension implements InvocationInterceptor {

    @Override
    public void interceptTestTemplateMethod(Invocation<Void> invocation,
                                            ReflectiveInvocationContext<Method> invocationContext,
                                            ExtensionContext extensionContext) throws Throwable {
        Logger logger = LoggerFactory.getLogger(extensionContext.getRequiredTestInstance().getClass());
        logger.info("Test Parameters:");
        List<String> parameterNames = Arrays.stream(
                invocationContext.getExecutable().getParameters()).map(Parameter::getName).collect(Collectors.toList());
        logger.info("Parameters Names: " + parameterNames.toString());
        logger.info("Parameters Values: " + invocationContext.getArguments().toString());
        invocation.proceed();
    }

}
