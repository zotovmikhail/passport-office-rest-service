package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.extension.TestConfigurationExtension;
import com.zotov.edu.passportofficerestservice.extension.TestExecutionLoggerExtension;
import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.repository.PersonsRepository;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import com.zotov.edu.passportofficerestservice.util.ReplaceCamelCase;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static com.zotov.edu.passportofficerestservice.util.PersonRequests.*;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(ReplaceCamelCase.class)
@ExtendWith({TestExecutionLoggerExtension.class, TestConfigurationExtension.class})
@Testcontainers
class DeletePersonIT {

    @Container
    private static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres");

    @Autowired
    private PersonsRepository personsRepository;

    @Test
    void testDeletePersonAndVerify() {
        Person person = personsRepository.create(generatePerson());

        deletePerson(person.getId())
                .statusCode(204);

        assertThat(personsRepository.existsById(person.getId())).isFalse();
    }

    @Test
    void testDeleteNonexistentPersonByPersonIdNegative() {
        String nonexistentPersonId = UUID.randomUUID().toString();

        ErrorMessage errorMessage =
                deletePerson(nonexistentPersonId)
                        .statusCode(404)
                        .extract()
                        .as(ErrorMessage.class);

        assertThat(errorMessage.getMessages()).containsExactlyInAnyOrder(String.format("Person with id '%s' is not found", nonexistentPersonId));
    }

}
