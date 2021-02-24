package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.generatePerson;
import static io.restassured.RestAssured.given;

class DeletePersonsIT extends PersonBaseTest {

    private static Stream<Arguments> getListOfPersonsForDelete() {
        return Stream.of(
                Arguments.of(generatePerson())
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsForDelete")
    void testDeletePersonAndVerify(Person person) {
        Person personResponse = postForPersonResponse(person);
        person.setId(personResponse.getId());
        given()
                    .body(person)
                    .pathParam("personId", person.getId())
                .when()
                    .delete("/persons/{personId}")
                .then()
                    .statusCode(204);

        ErrorMessage errorMessage = deletePersonForNotFound(person.getId());
        verifyPersonNotFoundErrorMessages(errorMessage, person.getId());
    }

    @Test
    void testDeleteNonexistentPersonsByPersonIdNegative() {
        String nonexistentPersonId = UUID.randomUUID().toString();
        ErrorMessage errorMessage =deletePersonForNotFound(nonexistentPersonId);
        verifyPersonNotFoundErrorMessages(errorMessage, nonexistentPersonId);
    }

}
