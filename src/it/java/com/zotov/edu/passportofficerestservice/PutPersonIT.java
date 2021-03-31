package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.extension.IntegrationTest;
import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.PersonPutRequest;
import com.zotov.edu.passportofficerestservice.model.PersonResponse;
import com.zotov.edu.passportofficerestservice.repository.PersonsRepository;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import com.zotov.edu.passportofficerestservice.util.PersonDataHandler;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.stream.Stream;

import static com.zotov.edu.passportofficerestservice.util.PersonRequests.*;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.*;
import static org.assertj.core.api.Assertions.*;

@IntegrationTest
class PutPersonIT {

    @Autowired
    private PersonDataHandler personDataHandler;

    @Autowired
    private PersonsRepository personsRepository;

    @Container
    public static final PostgreSQLContainer<?> postgreSqlContainer = new PostgreSQLContainer<>("postgres");

    @Test
    void testPutPersonAndVerify() {
        Person person = personDataHandler.generatePersonData();
        PersonPutRequest personPutRequest = generatePersonPutRequest();

        PersonResponse updatedPersonResponse =
                putPerson(person.getId(), personPutRequest)
                        .statusCode(200)
                        .extract()
                        .as(PersonResponse.class);

        assertThat(updatedPersonResponse.getId()).isEqualTo(person.getId());
        assertThat(updatedPersonResponse.getName()).isEqualTo(personPutRequest.getName());
        assertThat(updatedPersonResponse.getBirthday()).isEqualTo(personPutRequest.getBirthday());
        assertThat(updatedPersonResponse.getCountry()).isEqualTo(personPutRequest.getCountry());

        Person personFromDB = personsRepository
                .findById(person.getId())
                .orElseGet(() -> fail(String.format("Person '%s' is not found in the data.", person.getId())));

        assertThat(personFromDB.getId()).isEqualTo(person.getId());
        assertThat(personFromDB.getName()).isEqualTo(personPutRequest.getName());
        assertThat(personFromDB.getBirthday()).isEqualTo(personPutRequest.getBirthday());
        assertThat(personFromDB.getCountry()).isEqualTo(personPutRequest.getCountry());
    }

    private static Stream<Arguments> getPersonWithNullValuesToUpdate() {
        return Stream.of(
                Arguments.of(generatePersonPutRequest().withBirthday(null), "birthday must not be null"),
                Arguments.of(generatePersonPutRequest().withBirthday(StringUtils.EMPTY), "birthday must not be null"),
                Arguments.of(generatePersonPutRequest().withCountry(null), "country must not be null"),
                Arguments.of(generatePersonPutRequest().withName(null), "name must not be empty"),
                Arguments.of(generatePersonPutRequest().withName(StringUtils.EMPTY), "name must not be empty"),
                Arguments.of(generatePersonPutRequest().withBirthday("1993-06-072"), "Text '1993-06-072' could not be parsed, unparsed text found at index 10"),
                Arguments.of(generatePersonPutRequest().withCountry("Invalid"), "country is not found in the list of available countries."),
                Arguments.of(generatePersonPutRequest().withCountry(StringUtils.EMPTY), "country is not found in the list of available countries.")
        );
    }

    @ParameterizedTest
    @MethodSource("getPersonWithNullValuesToUpdate")
    void testPutPersonNegative(PersonPutRequest personPutRequest, String expectedErrorMessage) {
        Person person = personDataHandler.generatePersonData();

        ErrorMessage errorResponse =
                putPerson(person.getId(), personPutRequest)
                        .statusCode(400)
                        .extract()
                        .as(ErrorMessage.class);

        assertThat(errorResponse.getMessages()).containsExactlyInAnyOrder(expectedErrorMessage);
    }

    @Test
    void testPutNonexistentPersonsByPersonIdNegative() {
        String nonexistentPersonId = generateRandomPersonId();

        ErrorMessage errorMessage =
                putPerson(nonexistentPersonId, generatePersonPutRequest())
                        .statusCode(404)
                        .extract()
                        .as(ErrorMessage.class);

        assertThat(errorMessage.getMessages()).containsExactlyInAnyOrder(String.format("Person with id '%s' is not found", nonexistentPersonId));
    }

}
