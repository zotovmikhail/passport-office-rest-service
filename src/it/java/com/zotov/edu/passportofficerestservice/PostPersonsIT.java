package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.extension.TestConfigurationExtension;
import com.zotov.edu.passportofficerestservice.extension.TestExecutionLoggerExtension;
import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.PersonRequest;
import com.zotov.edu.passportofficerestservice.model.PersonResponse;
import com.zotov.edu.passportofficerestservice.repository.PersonsRepository;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import com.zotov.edu.passportofficerestservice.util.ReplaceCamelCase;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;
import java.util.stream.Stream;

import static com.zotov.edu.passportofficerestservice.util.PersonRequests.*;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(ReplaceCamelCase.class)
@ExtendWith({TestExecutionLoggerExtension.class, TestConfigurationExtension.class})
@Testcontainers
class PostPersonsIT {

    @Container
    private static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres");


    @Autowired
    private PersonsRepository personsRepository;

    @Test
    void testPostPersonAndVerify() {
        PersonRequest personRequest = generatePersonRequest();

        PersonResponse personResponse =
                postPerson(personRequest)
                        .statusCode(201)
                        .extract()
                        .as(PersonResponse.class);

        verifyIsUUID(personResponse.getId());
        assertThat(personResponse.getName()).isEqualTo(personRequest.getName());
        assertThat(personResponse.getBirthday()).isEqualTo(personRequest.getBirthday());
        assertThat(personResponse.getCountry()).isEqualTo(personRequest.getCountry());

        Person personFromDB = personsRepository
                .findById(personResponse.getId())
                .orElseGet(() -> fail(String.format("Person '%s' is not found in the database", personResponse.getId())));

        assertThat(personFromDB.getName()).isEqualTo(personRequest.getName());
        assertThat(personFromDB.getBirthday()).isEqualTo(personRequest.getBirthday());
        assertThat(personFromDB.getCountry()).isEqualTo(personRequest.getCountry());
    }

    private static Stream<Arguments> getListOfPersonsWithInvalidValues() {
        return Stream.of(
                Arguments.of(generatePersonRequest().withBirthday(null), "birthday must not be null"),
                Arguments.of(generatePersonRequest().withBirthday(StringUtils.EMPTY), "birthday must not be null"),
                Arguments.of(generatePersonRequest().withCountry(null),  "country must not be null"),
                Arguments.of(generatePersonRequest().withName(null), "name must not be empty"),
                Arguments.of(generatePersonRequest().withName(StringUtils.EMPTY), "name must not be empty"),
                Arguments.of(generatePersonRequest().withBirthday("1993-06-072"), "Text '1993-06-072' could not be parsed, unparsed text found at index 10"),
                Arguments.of(generatePersonRequest().withCountry("Invalid"), "country is not found in the list of available countries."),
                Arguments.of(generatePersonRequest().withCountry(StringUtils.EMPTY), "country is not found in the list of available countries.")
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsWithInvalidValues")
    void testPostPersonsNegative(PersonRequest personRequest, String expectedErrorMessage) {
        ErrorMessage errorMessage =
                postPerson(personRequest)
                        .statusCode(400)
                        .extract()
                        .as(ErrorMessage.class);

        assertThat(errorMessage.getMessages()).containsExactlyInAnyOrder(expectedErrorMessage);
    }

    private void verifyIsUUID(String id) {
        try {
            UUID.fromString(id);
        } catch (IllegalArgumentException exception) {
            fail(exception.getMessage());
        }
    }

}
