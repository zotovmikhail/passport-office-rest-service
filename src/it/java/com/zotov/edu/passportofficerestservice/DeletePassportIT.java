package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.extension.TestConfigurationExtension;
import com.zotov.edu.passportofficerestservice.extension.TestExecutionLoggerExtension;
import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.repository.PassportsRepository;
import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import com.zotov.edu.passportofficerestservice.util.PersonDataHandler;
import com.zotov.edu.passportofficerestservice.util.ReplaceCamelCase;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.zotov.edu.passportofficerestservice.util.PassportRequests.*;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(ReplaceCamelCase.class)
@ExtendWith({TestExecutionLoggerExtension.class, TestConfigurationExtension.class})
@Testcontainers
class DeletePassportIT {

    @Container
    private static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres");

    @Autowired
    private PersonDataHandler personDataHandler;

    @Autowired
    private PassportsRepository passportsRepository;

    @Test
    void testDeletePassportAndVerify() {
        Person person = personDataHandler.generatePersonData();
        Passport passport = passportsRepository.create((generatePassport(person.getId())));

        deletePassport(passport.getOwnerId(), passport.getNumber())
                .statusCode(204);

        assertThat(passportsRepository.existsByPassportNumber(passport.getNumber())).isFalse();
    }

    @Test
    void testDeleteNonexistentPassportByPassportIdNegative() {
        String nonexistentPassportId = generateRandomPassportNumber();
        Person person = personDataHandler.generatePersonData();

        ErrorMessage errorMessage =
                deletePassport(person.getId(), nonexistentPassportId)
                        .statusCode(404)
                        .extract()
                        .as(ErrorMessage.class);

        assertThat(errorMessage.getMessages()).containsExactlyInAnyOrder(String.format("Passport with id '%s' is not found", nonexistentPassportId));
    }

    @Test
    void testDeletePassportOfNonexistentPersonNegative() {
        String nonexistentPersonId = generateRandomPersonId();

        ErrorMessage errorMessage =
                deletePassport(nonexistentPersonId, generateRandomString())
                        .statusCode(404)
                        .extract()
                        .as(ErrorMessage.class);

        assertThat(errorMessage.getMessages()).containsExactlyInAnyOrder(String.format("Person with id '%s' is not found", nonexistentPersonId));
    }

}
