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

import java.util.UUID;
import java.util.stream.Stream;

import static com.zotov.edu.passportofficerestservice.util.DataConverter.convertToPersonPutRequest;
import static com.zotov.edu.passportofficerestservice.util.PersonRequests.*;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.generatePersonPutRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class PutPersonIT extends BaseTest {

    @Autowired
    private PersonDataHandler personDataHandler;

    @Test
    void testPutPersonAndVerify() {
        Person person = personDataHandler.generatePersonData();
        PersonPutRequest personRequest = convertToPersonPutRequest(person);

        PersonResponse updatedPersonResponse = putForPersonResponse(personRequest);

        assertThat(updatedPersonResponse.getId()).isEqualTo(personRequest.getId());
        assertThat(updatedPersonResponse.getName()).isEqualTo(personRequest.getName());
        assertThat(updatedPersonResponse.getBirthday()).isEqualTo(personRequest.getBirthday());
        assertThat(updatedPersonResponse.getCountry()).isEqualTo(personRequest.getCountry());

        Person personFromDB = personDataHandler.getPersonsRepository()
                .findById(personRequest.getId())
                .orElseGet(() -> fail(String.format("Person '%s' is not found in the data.", personRequest.getId())));

        assertThat(personFromDB.getId()).isEqualTo(personRequest.getId());
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

        ErrorMessage errorResponse = putPersonForBadRequest(personPutRequest.withId(person.getId()));

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

        ErrorMessage errorResponse = putPersonForBadRequest(personPutRequest.withId(person.getId()));

        verifyEmptyValueErrorMessages(errorResponse, field);
    }

    @Test
    void testPutPersonsWithInvalidBirthdayNegative() {
        Person person = personDataHandler.generatePersonData();
        String invalidBirthdayValue = "1993-06-072";

        ErrorMessage errorMessage = putPersonForBadRequest(generatePersonPutRequest()
                .withId(person.getId())
                .withBirthday(invalidBirthdayValue));

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

        ErrorMessage errorMessage = putPersonForBadRequest(personPutRequest.withId(person.getId()));

        verifyInvalidCountryErrorMessages(errorMessage);
    }

    @Test
    void testPutNonexistentPersonsByPersonIdNegative() {
        String nonexistentPersonId = UUID.randomUUID().toString();

        ErrorMessage errorMessage = putForPersonNotFound(nonexistentPersonId);

        verifyNotFoundErrorMessages(errorMessage, nonexistentPersonId, "Person");
    }

}
