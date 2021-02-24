package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.Person;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.generatePerson;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

class PutPersonsIT extends PersonBaseTest {

    private static Stream<Arguments> getListOfPersonsForUpdate() {
        return Stream.of(
                Arguments.of(generatePerson(), generatePerson())
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsForUpdate")
    void testPutPersonAndVerify(Person initialPerson, Person personForUpdate) {
        Person personResponse = postForPersonResponse(initialPerson);
        personForUpdate.setId(personResponse.getId());
        Person updatedPerson = putForPersonResponse(personForUpdate);
        assertThat(updatedPerson).isEqualTo(personForUpdate);
        Person receivedUpdatedPerson = getForPersonResponseById(updatedPerson.getId());
        assertThat(receivedUpdatedPerson).isEqualTo(personForUpdate);
    }

    private static Stream<Arguments> getListOfPersonsForUpdateWithNullValues() {
        return Stream.of(
                Arguments.of(generatePerson(), generatePerson().withBirthday(null), "birthday"),
                Arguments.of(generatePerson(), generatePerson().withCountry(null), "country")
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsForUpdateWithNullValues")
    void testPutPersonWithNullValuesNegative(Person initialPerson, Person personForUpdate, String field) {
        Person personResponse = postForPersonResponse(initialPerson);
        personForUpdate.setId(personResponse.getId());
        ErrorMessage errorResponse = putPersonForBadRequest(personForUpdate);
        verifyNullValueErrorMessages(errorResponse, field);
    }

    private static Stream<Arguments> getListOfPersonsForUpdateWithEmptyValues() {
        return Stream.of(
                Arguments.of(generatePerson(), generatePerson().withName(null), "name"),
                Arguments.of(generatePerson(), generatePerson().withName(StringUtils.EMPTY), "name")
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsForUpdateWithEmptyValues")
    void testPutPersonWithEmptyValuesNegative(Person initialPerson, Person personForUpdate, String field) {
        Person personResponse = postForPersonResponse(initialPerson);
        personForUpdate.setId(personResponse.getId());
        ErrorMessage errorResponse = putPersonForBadRequest(personForUpdate);
        verifyEmptyValueErrorMessages(errorResponse, field);
    }

    private static Stream<Arguments> getListOfPersonsForUpdateWithInvalidBirthday() {
        return Stream.of(
                Arguments.of(generatePerson(), generatePerson().withBirthday("1993-06-072"))
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsForUpdateWithInvalidBirthday")
    void testPutPersonsWithInvalidBirthdayNegative(Person initialPerson, Person personForUpdate) {
        Person personResponse = postForPersonResponse(initialPerson);
        personForUpdate.setId(personResponse.getId());
        ErrorMessage errorMessage = putPersonForBadRequest(personForUpdate);
        verifyInvalidDateErrorMessages(errorMessage, personForUpdate.getBirthday());
    }

    private static Stream<Arguments> getListOfPersonsForUpdateWithInvalidCountry() {
        return Stream.of(
                Arguments.of(generatePerson(), generatePerson().withCountry("Invalid")),
                Arguments.of(generatePerson(), generatePerson().withCountry(StringUtils.EMPTY))
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsForUpdateWithInvalidCountry")
    void testPutPersonsWithInvalidCountryNegative(Person initialPerson, Person personForUpdate) {
        Person personResponse = postForPersonResponse(initialPerson);
        personForUpdate.setId(personResponse.getId());
        ErrorMessage errorMessage = putPersonForBadRequest(personForUpdate);
        verifyInvalidCountryErrorMessages(errorMessage);
    }

    @Test
    void testPutNonexistentPersonsByPersonIdNegative() {
        String nonexistentPersonId = UUID.randomUUID().toString();
        ErrorMessage errorMessage =
                given()
                            .body(generatePerson())
                            .pathParam("personId", nonexistentPersonId)
                        .when()
                            .put("/persons/{personId}")
                        .then()
                            .statusCode(404)
                            .extract()
                            .as(ErrorMessage.class);
        verifyPersonNotFoundErrorMessages(errorMessage, nonexistentPersonId);
    }

}
