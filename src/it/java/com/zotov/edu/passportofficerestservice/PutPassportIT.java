package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.PassportSpecification;
import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static com.zotov.edu.passportofficerestservice.util.DataConverter.convertToPassportSpec;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class PutPassportIT extends PassportsBaseTest {

    private Stream<Arguments> getPassportToUpdate() {
        return Stream.of(
                Arguments.of(generatePassportData(generatePassport())
                        .withGivenDate(generateRandomDate())
                        .withDepartmentCode(generateRandomDepartmentCode()))
        );
    }

    @ParameterizedTest
    @MethodSource("getPassportToUpdate")
    void testPutPersonAndVerify(PassportSpecification passportSpecificationForUpdate) {
        PassportSpecification updatedPassportSpecification = putForPassportResponse(passportSpecificationForUpdate.getOwnerId(), passportSpecificationForUpdate);
        assertThat(updatedPassportSpecification).isEqualTo(passportSpecificationForUpdate);
        Passport foundPassport = passportsRepository
                .findByPassportNumber(passportSpecificationForUpdate.getNumber())
                .orElseGet(() -> fail(String.format("Passport '%s' is not found in the data.", passportSpecificationForUpdate.getNumber())));
        assertThat(updatedPassportSpecification).isEqualTo(convertToPassportSpec(foundPassport));
    }

    private Stream<Arguments> getPassportWithNullValuesToUpdate() {
        return Stream.of(
                Arguments.of(generatePassportData(generatePassport()).withGivenDate(null), "givenDate"),
                Arguments.of(generatePassportData(generatePassport()).withGivenDate(StringUtils.EMPTY), "givenDate")
        );
    }

    @ParameterizedTest
    @MethodSource("getPassportWithNullValuesToUpdate")
    void testPutPassportWithNullValuesNegative(PassportSpecification passportSpecification, String field) {
        ErrorMessage errorMessage = putPassportForBadRequest(passportSpecification.getOwnerId(), passportSpecification);
        verifyNullValueErrorMessages(errorMessage, field);
    }

    private Stream<Arguments> getPassportWithEmptyValuesToUpdate() {
        return Stream.of(
                Arguments.of(generatePassportData(generatePassport()).withDepartmentCode(StringUtils.EMPTY), "departmentCode")
        );
    }

    @ParameterizedTest
    @MethodSource("getPassportWithEmptyValuesToUpdate")
    void testPutPassportWithEmptyValuesNegative(PassportSpecification passportSpecification, String field) {
        ErrorMessage errorMessage = putPassportForBadRequest(passportSpecification.getOwnerId(), passportSpecification);
        verifyEmptyValueErrorMessages(errorMessage, field);
    }

    private Stream<Arguments> getPassportWithInvalidGivenDateToUpdate() {
        return Stream.of(
                Arguments.of(generatePassportData(generatePassport()).withGivenDate("1993-06-072"))
        );
    }

    @ParameterizedTest
    @MethodSource("getPassportWithInvalidGivenDateToUpdate")
    void testPutPassportWithInvalidGivenDateNegative(PassportSpecification passportSpecification) {
        ErrorMessage errorMessage = postPassportForBadRequest(passportSpecification.getOwnerId(), passportSpecification);
        verifyInvalidDateErrorMessages(errorMessage, passportSpecification.getGivenDate());
    }

    @Test
    void testPutPassportOfNonexistentPersonBNegative() {
        String nonExistentPersonId = UUID.randomUUID().toString();
        ErrorMessage errorMessage = putForNotFoundByPassportNumber(nonExistentPersonId, generatePassport());
        verifyPersonNotFoundErrorMessages(errorMessage, nonExistentPersonId);
    }
}
