package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.PersonPutRequest;
import com.zotov.edu.passportofficerestservice.model.PersonResponse;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import com.zotov.edu.passportofficerestservice.util.PersonDataHandler;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static com.zotov.edu.passportofficerestservice.util.PersonRequests.*;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.*;
import static org.assertj.core.api.Assertions.*;

class PutPersonIT extends BaseTest {

    @Autowired
    private PersonDataHandler personDataHandler;

    @Test
    void testPutPersonAndVerify() {
        Person person = personDataHandler.generatePersonData();
        PersonPutRequest personRequest = generatePersonPutRequest();

        PersonResponse updatedPersonResponse =
                putPerson(person.getId(), personRequest)
                        .statusCode(200)
                        .extract()
                        .as(PersonResponse.class);

        assertThat(updatedPersonResponse.getId()).isEqualTo(person.getId());
        assertThat(updatedPersonResponse.getName()).isEqualTo(personRequest.getName());
        assertThat(updatedPersonResponse.getBirthday()).isEqualTo(personRequest.getBirthday());
        assertThat(updatedPersonResponse.getCountry()).isEqualTo(personRequest.getCountry());

        Person personFromDB = personDataHandler.getPersonsRepository()
                .findById(person.getId())
                .orElseGet(() -> fail(String.format("Person '%s' is not found in the data.", person.getId())));

        assertThat(personFromDB.getId()).isEqualTo(person.getId());
        assertThat(personFromDB.getName()).isEqualTo(personRequest.getName());
        assertThat(personFromDB.getBirthday()).isEqualTo(personRequest.getBirthday());
        assertThat(personFromDB.getCountry()).isEqualTo(personRequest.getCountry());
    }

    private static Stream<Arguments> getPersonWithNullValuesToUpdate() {
        return Stream.of(
                Arguments.of(generatePersonPutRequest().withBirthday(null), "birthday", "Null birthday value"),
                Arguments.of(generatePersonPutRequest().withCountry(null), "country", "Null country value")
        );
    }

    @ParameterizedTest
    @MethodSource("getPersonWithNullValuesToUpdate")
    void testPutPersonWithNullValuesNegative(PersonPutRequest personPutRequest, String field, String description) {
        Person person = personDataHandler.generatePersonData();

        ErrorMessage errorResponse =
                putPerson(person.getId(), personPutRequest)
                        .statusCode(400)
                        .extract()
                        .as(ErrorMessage.class);

        verifyNullValueErrorMessages(errorResponse, field);
    }

    private static Stream<Arguments> getPersonForUpdateWithEmptyValuesToUpdate() {
        return Stream.of(
                Arguments.of(generatePersonPutRequest().withName(null), "name", "Null name value"),
                Arguments.of(generatePersonPutRequest().withName(StringUtils.EMPTY), "name", "Empty name value")
        );
    }

    @ParameterizedTest
    @MethodSource("getPersonForUpdateWithEmptyValuesToUpdate")
    void testPutPersonWithEmptyValuesNegative(PersonPutRequest personPutRequest, String field, String description) {
        Person person = personDataHandler.generatePersonData();

        ErrorMessage errorResponse =
                putPerson(person.getId(), personPutRequest)
                        .statusCode(400)
                        .extract()
                        .as(ErrorMessage.class);

        verifyEmptyValueErrorMessages(errorResponse, field);
    }

    @Test
    void testPutPersonsWithInvalidBirthdayNegative() {
        Person person = personDataHandler.generatePersonData();
        String invalidBirthdayValue = "1993-06-072";

        ErrorMessage errorMessage =
                putPerson(person.getId(), generatePersonPutRequest().withBirthday(invalidBirthdayValue))
                        .statusCode(400)
                        .extract()
                        .as(ErrorMessage.class);

        verifyInvalidDateErrorMessages(errorMessage, invalidBirthdayValue);
    }

    private static Stream<Arguments> getPersonWithInvalidCountryToUpdate() {
        return Stream.of(
                Arguments.of(generatePersonPutRequest().withCountry("Invalid"), "Invalid country value"),
                Arguments.of(generatePersonPutRequest().withCountry(StringUtils.EMPTY), "Empty country value")
        );
    }

    @ParameterizedTest
    @MethodSource("getPersonWithInvalidCountryToUpdate")
    void testPutPersonsWithInvalidCountryNegative(PersonPutRequest personPutRequest, String description) {
        Person person = personDataHandler.generatePersonData();

        ErrorMessage errorMessage =
                putPerson(person.getId(), personPutRequest)
                        .statusCode(400)
                        .extract()
                        .as(ErrorMessage.class);

        verifyInvalidCountryErrorMessages(errorMessage);
    }

    @Test
    void testPutNonexistentPersonsByPersonIdNegative() {
        String nonexistentPersonId = generateRandomPersonId();

        ErrorMessage errorMessage =
                putPerson(nonexistentPersonId, generatePersonPutRequest())
                        .statusCode(404)
                        .extract()
                        .as(ErrorMessage.class);

        verifyNotFoundErrorMessages(errorMessage, nonexistentPersonId, "Person");
    }

}
