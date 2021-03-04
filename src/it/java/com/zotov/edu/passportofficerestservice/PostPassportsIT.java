package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.PassportSpecification;
import com.zotov.edu.passportofficerestservice.model.PersonSpecification;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.generatePassport;
import static org.assertj.core.api.Assertions.assertThat;

class PostPassportsIT extends PassportsBaseTest {

    private Stream<Arguments> getListOfPassportsToCreate() {
        return Stream.of(
                Arguments.of(generatePersonData(), generatePassport())
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPassportsToCreate")
    void testPostPassportAndVerify(PersonSpecification personSpecification, PassportSpecification expectedPassportSpecification) {
        PassportSpecification createdPassportSpecification = postForPassportResponse(personSpecification.getId(), expectedPassportSpecification);
        assertThat(createdPassportSpecification).isEqualTo(expectedPassportSpecification);
    }

    private Stream<Arguments> getPersonAndPassportWithNullValues() {
        return Stream.of(
                Arguments.of(generatePersonData(), generatePassport().withGivenDate(null), "givenDate"),
                Arguments.of(generatePersonData(), generatePassport().withGivenDate(StringUtils.EMPTY), "givenDate")
        );
    }

    @ParameterizedTest
    @MethodSource("getPersonAndPassportWithNullValues")
    void testPostPassportWithNullValuesNegative(PersonSpecification personSpecification, PassportSpecification passportSpecification, String field) {
        ErrorMessage errorMessage = postPassportForBadRequest(personSpecification.getId(), passportSpecification);
        verifyNullValueErrorMessages(errorMessage, field);
    }

    private Stream<Arguments> getPersonAndPassportWithEmptyValues() {
        return Stream.of(
                Arguments.of(generatePersonData(), generatePassport().withNumber(StringUtils.EMPTY), "number"),
                Arguments.of(generatePersonData(), generatePassport().withDepartmentCode(StringUtils.EMPTY), "departmentCode")
        );
    }

    @ParameterizedTest
    @MethodSource("getPersonAndPassportWithEmptyValues")
    void testPostPassportWithEmptyValuesNegative(PersonSpecification personSpecification, PassportSpecification passportSpecification, String field) {
        ErrorMessage errorMessage = postPassportForBadRequest(personSpecification.getId(), passportSpecification);
        verifyEmptyValueErrorMessages(errorMessage, field);
    }

    private Stream<Arguments> getPersonAndPassportWithInvalidGivenDate() {
        return Stream.of(
                Arguments.of(generatePersonData(), generatePassport().withGivenDate("1993-06-072"))
        );
    }

    @ParameterizedTest
    @MethodSource("getPersonAndPassportWithInvalidGivenDate")
    void testPostPassportWithInvalidGivenDateNegative(PersonSpecification personSpecification, PassportSpecification passportSpecification) {
        ErrorMessage errorMessage = postPassportForBadRequest(personSpecification.getId(), passportSpecification);
        verifyInvalidDateErrorMessages(errorMessage, passportSpecification.getGivenDate());
    }

    @Test
    void testPostPassportOfNonexistentPersonBNegative() {
        String nonExistentPersonId = UUID.randomUUID().toString();
        ErrorMessage errorMessage = postForNotFoundByPassportNumber(nonExistentPersonId, generatePassport());
        verifyPersonNotFoundErrorMessages(errorMessage, nonExistentPersonId);
    }

    private Stream<Arguments> getPassportAndPerson() {
        return Stream.of(
                Arguments.of(generatePassportData(generatePassport()))
        );
    }

    @ParameterizedTest
    @MethodSource("getPassportAndPerson")
    void testPostAlreadyExistsPassportNegative(PassportSpecification passportSpecification) {
            ErrorMessage errorMessage = postForConflictPassport(passportSpecification.getOwnerId(), passportSpecification);
            verifyErrorMessages(errorMessage, List.of(String.format("Passport with id '%s' already exists in the data", passportSpecification.getNumber())));
    }

}
