package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.Passport;
import com.zotov.edu.passportofficerestservice.model.Person;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.generatePassport;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.generatePerson;
import static org.assertj.core.api.Assertions.assertThat;

class PostPassportsIT extends PassportsBaseTest {


    private static Stream<Arguments> getListOfPersonsToCreate() {
        return Stream.of(
                Arguments.of(generatePerson(), generatePassport())
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsToCreate")
    void testPostPassportAndVerify(Person person, Passport expectedPassport) {
        Person createdPerson = postForPersonResponse(person);
        Passport createdPassport = postForPassportResponse(createdPerson.getId(), expectedPassport);
        assertThat(createdPassport).isEqualTo(expectedPassport);
    }

    private static Stream<Arguments> getPersonAndPassportWithNullValues() {
        return Stream.of(
                Arguments.of(generatePerson(), generatePassport().withGivenDate(null), "givenDate"),
                Arguments.of(generatePerson(), generatePassport().withGivenDate(StringUtils.EMPTY), "givenDate")
        );
    }

    @ParameterizedTest
    @MethodSource("getPersonAndPassportWithNullValues")
    void testPostPassportWithNullValuesNegative(Person person, Passport passport, String field) {
        Person createdPerson = postForPersonResponse(person);
        ErrorMessage errorMessage = postPassportForBadRequest(createdPerson.getId(), passport);
        verifyNullValueErrorMessages(errorMessage, field);
    }

    private static Stream<Arguments> getPersonAndPassportWithEmptyValues() {
        return Stream.of(
                Arguments.of(generatePerson(), generatePassport().withNumber(StringUtils.EMPTY), "number"),
                Arguments.of(generatePerson(), generatePassport().withDepartmentCode(StringUtils.EMPTY), "departmentCode")
        );
    }

    @ParameterizedTest
    @MethodSource("getPersonAndPassportWithEmptyValues")
    void testPostPassportWithEmptyValuesNegative(Person person, Passport passport, String field) {
        Person createdPerson = postForPersonResponse(person);
        ErrorMessage errorMessage = postPassportForBadRequest(createdPerson.getId(), passport);
        verifyEmptyValueErrorMessages(errorMessage, field);
    }

    private static Stream<Arguments> getPersonAndPassportWithInvalidGivenDate() {
        return Stream.of(
                Arguments.of(generatePerson(), generatePassport().withGivenDate("1993-06-072"))
        );
    }

    @ParameterizedTest
    @MethodSource("getPersonAndPassportWithInvalidGivenDate")
    void testPostPassportWithInvalidGivenDateNegative(Person person, Passport passport) {
        Person createdPerson = postForPersonResponse(person);
        ErrorMessage errorMessage = postPassportForBadRequest(createdPerson.getId(), passport);
        verifyInvalidDateErrorMessages(errorMessage, passport.getGivenDate());
    }

    @Test
    void testPostPassportOfNonexistentPersonBNegative() {
        String nonExistentPersonId = UUID.randomUUID().toString();
        ErrorMessage errorMessage = postForNotFoundByPassportNumber(nonExistentPersonId, generatePassport());
        verifyPersonNotFoundErrorMessages(errorMessage, nonExistentPersonId);
    }

    @Test
    void testPostAlreadyExistsPassportNegative() {
        Person createdPerson = postForPersonResponse(generatePerson());
        Passport existingPassport = postForPassportResponse(createdPerson.getId(), generatePassport());
        ErrorMessage errorMessage = postForConflictPassport(createdPerson.getId(), existingPassport);
        verifyErrorMessages(errorMessage, List.of(String.format("Passport with id '%s' already exists in the data", existingPassport.getNumber())));
    }

}
