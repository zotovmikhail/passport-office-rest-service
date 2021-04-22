package com.zotov.edu.passportofficerestservice.extension;

import com.zotov.edu.passportofficerestservice.util.ReplaceCamelCase;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayNameGeneration(ReplaceCamelCase.class)
@ExtendWith({TestExecutionLoggerExtension.class, TestConfigurationExtension.class})
@Execution(ExecutionMode.CONCURRENT)
public @interface IntegrationTest {
}
