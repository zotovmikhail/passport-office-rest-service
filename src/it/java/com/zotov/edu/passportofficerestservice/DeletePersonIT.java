package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.PersonSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

class DeletePersonIT extends PersonBaseTest {

    private Stream<Arguments> getPersonToDelete() {
        return Stream.of(
                Arguments.of(generatePersonData())
        );
    }

    @ParameterizedTest
    @MethodSource("getPersonToDelete")
    void testDeletePersonAndVerify(PersonSpecification personSpecification) {
        given()
                    .body(personSpecification)
                    .pathParam("personId", personSpecification.getId())
                .when()
                    .delete("/persons/{personId}")
                .then()
                    .statusCode(204);

        assertThat(personsRepository.existsById(personSpecification.getId())).isFalse();
    }

    @Test
    void testDeleteNonexistentPersonByPersonIdNegative() {
        String nonexistentPersonId = UUID.randomUUID().toString();
        ErrorMessage errorMessage = deletePersonForNotFound(nonexistentPersonId);
        verifyPersonNotFoundErrorMessages(errorMessage, nonexistentPersonId);
    }

}
