package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.PassportSpecification;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.generatePassport;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.generateRandomString;
import static org.assertj.core.api.Assertions.assertThat;

class GetPassportIT extends PassportsBaseTest {

    private Stream<Arguments> getPassport() {
        return Stream.of(
                Arguments.of(generatePassportData(generatePassport()))
        );
    }

    @ParameterizedTest
    @MethodSource("getPassport")
    void testGetPassportByPassportId(PassportSpecification expectedPassportSpecification) {
            PassportSpecification passportSpecificationResponse =
                    getForPassportResponseByPassportNumber(expectedPassportSpecification.getOwnerId(), expectedPassportSpecification.getNumber());
            assertThat(passportSpecificationResponse).isEqualTo(expectedPassportSpecification);
    }

    private Stream<Arguments> getPersonIdAndNonexistentPassport() {
        return Stream.of(
                Arguments.of(generatePersonData().getId(), generateRandomString())
        );
    }

    @ParameterizedTest
    @MethodSource("getPersonIdAndNonexistentPassport")
    void testGetNonexistentPassportByPassportIdNegative(String createdPersonId, String nonExistentPassportNumber) {
        ErrorMessage errorMessage = getForNotFoundByPassportNumber(createdPersonId, nonExistentPassportNumber);
        verifyPassportNotFoundErrorMessages(errorMessage, nonExistentPassportNumber);
    }

    private static Stream<Arguments> getNonExistentPersonAndNonexistentPassport() {
        return Stream.of(
                Arguments.of(generateRandomString(), UUID.randomUUID().toString())
        );
    }

    @ParameterizedTest
    @MethodSource("getNonExistentPersonAndNonexistentPassport")
    void testGetPassportOfNonexistentPersonByPassportIdNegative(String nonExistentPassportNumber, String nonExistentPersonId) {
        ErrorMessage errorMessage = getForNotFoundByPassportNumber(nonExistentPersonId, nonExistentPassportNumber);
        verifyPersonNotFoundErrorMessages(errorMessage, nonExistentPersonId);
    }

}
