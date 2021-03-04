package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.PersonSpecification;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static com.zotov.edu.passportofficerestservice.util.DataConverter.convertToPersonSpec;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class PutPersonIT extends PersonBaseTest {

    private Stream<Arguments> getPersonToUpdate() {
        return Stream.of(
                Arguments.of(generatePersonData())
        );
    }

    @ParameterizedTest
    @MethodSource("getPersonToUpdate")
    void testPutPersonAndVerify(PersonSpecification personSpecificationForUpdate) {
        PersonSpecification updatedPersonSpecification = putForPersonResponse(personSpecificationForUpdate);
        assertThat(updatedPersonSpecification).isEqualTo(personSpecificationForUpdate);
        Person foundPerson = personsRepository
                .findById(personSpecificationForUpdate.getId())
                .orElseGet(() -> fail(String.format("Person '%s' is not found in the data.", personSpecificationForUpdate.getId())));
        assertThat(updatedPersonSpecification).isEqualTo(convertToPersonSpec(foundPerson));
    }

    private Stream<Arguments> getPersonWithNullValuesToUpdate() {
        return Stream.of(
                Arguments.of(generatePersonData().withBirthday(null), "birthday"),
                Arguments.of(generatePersonData().withCountry(null), "country")
        );
    }

    @ParameterizedTest
    @MethodSource("getPersonWithNullValuesToUpdate")
    void testPutPersonWithNullValuesNegative(PersonSpecification personSpecificationForUpdate, String field) {
        ErrorMessage errorResponse = putPersonForBadRequest(personSpecificationForUpdate);
        verifyNullValueErrorMessages(errorResponse, field);
    }

    private Stream<Arguments> getPersonForUpdateWithEmptyValuesToUpdate() {
        return Stream.of(
                Arguments.of(generatePersonData().withName(null), "name"),
                Arguments.of(generatePersonData().withName(StringUtils.EMPTY), "name")
        );
    }

    @ParameterizedTest
    @MethodSource("getPersonForUpdateWithEmptyValuesToUpdate")
    void testPutPersonWithEmptyValuesNegative(PersonSpecification personSpecificationForUpdate, String field) {
        ErrorMessage errorResponse = putPersonForBadRequest(personSpecificationForUpdate);
        verifyEmptyValueErrorMessages(errorResponse, field);
    }

    private Stream<Arguments> getPersonWithInvalidBirthdayToUpdate() {
        return Stream.of(
                Arguments.of(generatePersonData().withBirthday("1993-06-072"))
        );
    }

    @ParameterizedTest
    @MethodSource("getPersonWithInvalidBirthdayToUpdate")
    void testPutPersonsWithInvalidBirthdayNegative(PersonSpecification personSpecificationForUpdate) {
        ErrorMessage errorMessage = putPersonForBadRequest(personSpecificationForUpdate);
        verifyInvalidDateErrorMessages(errorMessage, personSpecificationForUpdate.getBirthday());
    }

    private Stream<Arguments> getPersonWithInvalidCountryToUpdate() {
        return Stream.of(
                Arguments.of(generatePersonData().withCountry("Invalid")),
                Arguments.of(generatePersonData().withCountry(StringUtils.EMPTY))
        );
    }

    @ParameterizedTest
    @MethodSource("getPersonWithInvalidCountryToUpdate")
    void testPutPersonsWithInvalidCountryNegative(PersonSpecification personSpecificationForUpdate) {
        ErrorMessage errorMessage = putPersonForBadRequest(personSpecificationForUpdate);
        verifyInvalidCountryErrorMessages(errorMessage);
    }

    @Test
    void testPutNonexistentPersonsByPersonIdNegative() {
        String nonexistentPersonId = UUID.randomUUID().toString();
        ErrorMessage errorMessage = putForPersonNotFound(nonexistentPersonId);
        verifyPersonNotFoundErrorMessages(errorMessage, nonexistentPersonId);
    }

}
