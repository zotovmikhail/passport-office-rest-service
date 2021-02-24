package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.Passport;
import com.zotov.edu.passportofficerestservice.model.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.*;
import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GetPassportsIT extends PassportsBaseTest {
    private static Stream<Arguments> getListOfPersonsAndPassports() {
        return Stream.of(
                Arguments.of(generatePerson(), Set.of()),
                Arguments.of(generatePerson(), generatePassports(2))
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsAndPassports")
    void testGetPassportsAndVerifyDefaultValues(Person person, Set<Passport> expectedPassports) {
        String createdPersonId = postForPersonResponse(person).getId();
        expectedPassports
                .forEach(passport -> postForPassportResponse(createdPersonId, passport));
        Set<Passport> passportsResponse = getForPassportResponse(createdPersonId);
        assertThat(passportsResponse).isEqualTo(expectedPassports);
    }

    private static Stream<Arguments> getListOfPersonsAndPassportsWithStateActive() {
        return Stream.of(
                Arguments.of(generatePerson(), Set.of()),
                Arguments.of(generatePerson(), generatePassports(2))
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsAndPassportsWithStateActive")
    void testGetActivePassportsByPassportStateAndVerify(Person person, Set<Passport> expectedPassports) {
        String createdPersonId = postForPersonResponse(person).getId();
        expectedPassports
                .forEach(passport -> postForPassportResponse(createdPersonId, passport));
        Set<Passport> passportsResponse = getForPassportResponseByPassportState(createdPersonId, "ACTIVE");
        assertThat(passportsResponse).isEqualTo(expectedPassports);
    }

    private static Stream<Arguments> getListOfPersonsAndPassportsWithStateLost() {
        return Stream.of(
                Arguments.of(generatePerson(), generatePassports(2))
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsAndPassportsWithStateLost")
    void testGetLostPassportsByPassportStateAndVerify(Person person, Set<Passport> expectedPassports) {
        String createdPersonId = postForPersonResponse(person).getId();
        expectedPassports
                .forEach(passport -> postForPassportResponse(createdPersonId, passport));
        Set<Passport> passportsResponse = getForPassportResponseByPassportState(createdPersonId, "LOST");
        assertThat(passportsResponse).isEqualTo(Set.of());
        expectedPassports
                .forEach(passport -> losePassportForPassportResponse(createdPersonId, passport));
        passportsResponse = getForPassportResponseByPassportState(createdPersonId, "LOST");
        assertThat(passportsResponse).isEqualTo(expectedPassports);
    }

    private static Stream<Arguments> getListOfPersonsAndPassportsForMaxAndMinGivenDate() {
        return Stream.of(
                Arguments.of(generatePerson(), generatePassport().withGivenDate("2021-01-01"), null, null),
                Arguments.of(generatePerson(), generatePassport().withGivenDate("2021-01-01"), "2020-01-01", null),
                Arguments.of(generatePerson(), generatePassport().withGivenDate("2021-01-01"), "2020-01-01", "2021-01-02"),
                Arguments.of(generatePerson(), generatePassport().withGivenDate("2021-01-01"), null, "2021-01-02")
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsAndPassportsForMaxAndMinGivenDate")
    void testGetPassportsByMinAndMaxGivenDate(Person person, Passport expectedPassport, String minGivenDate, String maxGivenDate) {
        String createdPersonId = postForPersonResponse(person).getId();
        postForPassportResponse(createdPersonId, expectedPassport);
        Set<Passport> passportsResponse = getForPassportResponseByMaxAndMinGivenDates(createdPersonId, minGivenDate, maxGivenDate);
        assertThat(passportsResponse).isEqualTo(Set.of(expectedPassport));
    }

    private static Stream<Arguments> getListOfPersonsAndPassportsForMaxAndMinGivenDateEmpty() {
        return Stream.of(
                Arguments.of(generatePerson(), generatePassport().withGivenDate("2021-01-01"), "2021-01-02", null),
                Arguments.of(generatePerson(), generatePassport().withGivenDate("2021-01-01"), "2021-01-02", "2021-01-02"),
                Arguments.of(generatePerson(), generatePassport().withGivenDate("2021-01-01"), null, "2021-01-01")
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsAndPassportsForMaxAndMinGivenDateEmpty")
    void testGetPassportsByMinAndMaxGivenDateAndVerifyEmpty(Person person, Passport expectedPassport, String minGivenDate, String maxGivenDate) {
        String createdPersonId = postForPersonResponse(person).getId();
        postForPassportResponse(createdPersonId, expectedPassport);
        Set<Passport> passportsResponse = getForPassportResponseByMaxAndMinGivenDates(createdPersonId, minGivenDate, maxGivenDate);
        assertThat(passportsResponse).isEqualTo(Set.of());
    }

    private static Stream<Arguments> getPersonAndPassport() {
        return Stream.of(
                Arguments.of(generatePerson(), generatePassport())
        );
    }

    @ParameterizedTest
    @MethodSource("getPersonAndPassport")
    void testGetPassportByPassportId(Person person, Passport expectedPassport) {
        String createdPersonId = postForPersonResponse(person).getId();
        postForPassportResponse(createdPersonId, expectedPassport);
        Passport passportResponse = getForPassportResponseByPassportNumber(createdPersonId, expectedPassport.getNumber());
        assertThat(passportResponse).isEqualTo(expectedPassport);
    }

    @Test
    void testGetNonexistentPassportByPassportIdNegative() {
        String nonExistentPassportNumber = generateRandomString();
        String createdPersonId = postForPersonResponse(generatePerson()).getId();
        ErrorMessage errorMessage = getForNotFoundByPassportNumber(createdPersonId, nonExistentPassportNumber);
        verifyPassportNotFoundErrorMessages(errorMessage, nonExistentPassportNumber);
    }

    @Test
    void testGetPassportOfNonexistentPersonByPassportIdNegative() {
        String nonExistentPassportNumber = generateRandomString();
        String nonExistentPersonId = UUID.randomUUID().toString();
        ErrorMessage errorMessage = getForNotFoundByPassportNumber(nonExistentPersonId, nonExistentPassportNumber);
        verifyPersonNotFoundErrorMessages(errorMessage, nonExistentPersonId);
    }

}
