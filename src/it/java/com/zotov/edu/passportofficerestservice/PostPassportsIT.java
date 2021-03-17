package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.PassportRequest;
import com.zotov.edu.passportofficerestservice.model.PassportResponse;
import com.zotov.edu.passportofficerestservice.repository.PassportsRepository;
import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import com.zotov.edu.passportofficerestservice.util.PassportDataHandler;
import com.zotov.edu.passportofficerestservice.util.PersonDataHandler;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

import static com.zotov.edu.passportofficerestservice.util.DataConverter.*;
import static com.zotov.edu.passportofficerestservice.util.PassportRequests.*;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.*;
import static org.assertj.core.api.Assertions.*;

class PostPassportsIT extends BaseTest {

    @Autowired
    private PersonDataHandler personDataHandler;

    @Autowired
    private PassportDataHandler passportDataHandler;

    @Autowired
    private PassportsRepository passportsRepository;

    @Test
    void testPostPassportAndVerify() {
        Person person = personDataHandler.generatePersonData();
        PassportRequest passportRequest = generatePassportRequest();

        PassportResponse passportResponse =
                postPassport(person.getId(), passportRequest)
                        .statusCode(201)
                        .extract()
                        .as(PassportResponse.class);

        assertThat(passportResponse.getNumber()).isEqualTo(passportRequest.getNumber());
        assertThat(passportResponse.getDepartmentCode()).isEqualTo(passportRequest.getDepartmentCode());
        assertThat(passportResponse.getGivenDate()).isEqualTo(passportRequest.getGivenDate());

        Passport passportFromDB = passportsRepository
                .findByPassportNumber(passportRequest.getNumber())
                .orElseGet(() -> fail(String.format("Passport '%s' is not found in the database", passportRequest.getNumber())));

        assertThat(passportFromDB.getOwnerId()).isEqualTo(person.getId());
        assertThat(passportFromDB.getNumber()).isEqualTo(passportRequest.getNumber());
        assertThat(passportFromDB.getDepartmentCode()).isEqualTo(passportRequest.getDepartmentCode());
        assertThat(passportFromDB.getGivenDate()).isEqualTo(passportRequest.getGivenDate());
        assertThat(passportFromDB.getState()).isEqualTo(PassportState.ACTIVE);
    }

    private static Stream<Arguments> getPersonAndPassportWithNullValues() {
        return Stream.of(
                Arguments.of(generatePassportRequest().withGivenDate(null), "givenDate", "Null given date value"),
                Arguments.of(generatePassportRequest().withGivenDate(StringUtils.EMPTY), "givenDate", "Empty given date value")
        );
    }

    @ParameterizedTest
    @MethodSource("getPersonAndPassportWithNullValues")
    void testPostPassportWithNullValuesNegative(PassportRequest passportRequest, String field, String description) {
        Person person = personDataHandler.generatePersonData();

        ErrorMessage errorMessage =
                postPassport(person.getId(), passportRequest)
                        .statusCode(400)
                        .extract()
                        .as(ErrorMessage.class);

        verifyNullValueErrorMessages(errorMessage, field);
    }

    private static Stream<Arguments> getPersonAndPassportWithEmptyValues() {
        return Stream.of(
                Arguments.of(generatePassportRequest().withNumber(StringUtils.EMPTY), "number", "Empty passport number value"),
                Arguments.of(generatePassportRequest().withDepartmentCode(StringUtils.EMPTY), "departmentCode", "Empty department code value")
        );
    }

    @ParameterizedTest
    @MethodSource("getPersonAndPassportWithEmptyValues")
    void testPostPassportWithEmptyValuesNegative(PassportRequest passportRequest, String field, String description) {
        Person person = personDataHandler.generatePersonData();

        ErrorMessage errorMessage =
                postPassport(person.getId(), passportRequest)
                        .statusCode(400)
                        .extract()
                        .as(ErrorMessage.class);

        verifyEmptyValueErrorMessages(errorMessage, field);
    }

    @Test
    void testPostPassportWithInvalidGivenDateNegative() {
        Person person = personDataHandler.generatePersonData();
        PassportRequest passportRequestWithInvalidGivenDate = generatePassportRequest().withGivenDate("1993-06-072");

        ErrorMessage errorMessage =
                postPassport(person.getId(), passportRequestWithInvalidGivenDate)
                        .statusCode(400)
                        .extract()
                        .as(ErrorMessage.class);

        verifyInvalidDateErrorMessages(errorMessage, passportRequestWithInvalidGivenDate.getGivenDate());
    }

    @Test
    void testPostPassportOfNonexistentPersonNegative() {
        String nonExistentPersonId = generateRandomPersonId();

        ErrorMessage errorMessage =
                postPassport(nonExistentPersonId, generatePassportRequest())
                        .statusCode(404)
                        .extract()
                        .as(ErrorMessage.class);

        verifyNotFoundErrorMessages(errorMessage, nonExistentPersonId, "Person");
    }

    @Test
    void testPostAlreadyExistsPassportNegative() {
        Person person = personDataHandler.generatePersonData();
        Passport passport = passportDataHandler.generatePassportData(person.getId());

        ErrorMessage errorMessage =
                postPassport(person.getId(), convertToPassportRequest(passport))
                        .statusCode(409)
                        .extract()
                        .as(ErrorMessage.class);

        verifyErrorMessages(errorMessage, List.of(String.format("Passport with id '%s' already exists in the data", passport.getNumber())));
    }

}
