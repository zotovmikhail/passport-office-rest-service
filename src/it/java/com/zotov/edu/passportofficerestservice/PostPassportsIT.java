package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.extension.IntegrationTest;
import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.PassportPostRequest;
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
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.stream.Stream;

import static com.zotov.edu.passportofficerestservice.util.DataConverter.*;
import static com.zotov.edu.passportofficerestservice.util.PassportRequests.*;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.*;
import static org.assertj.core.api.Assertions.*;

@IntegrationTest
class PostPassportsIT {

    @Autowired
    private PersonDataHandler personDataHandler;

    @Autowired
    private PassportDataHandler passportDataHandler;

    @Autowired
    private PassportsRepository passportsRepository;

    @Container
    public static final PostgreSQLContainer<?> postgreSqlContainer = new PostgreSQLContainer<>("postgres");

    @Test
    void testPostPassportAndVerify() {
        Person person = personDataHandler.generatePersonData();
        PassportPostRequest passportPostRequest = generatePassportRequest();

        PassportResponse passportResponse =
                postPassport(person.getId(), passportPostRequest)
                        .statusCode(201)
                        .extract()
                        .as(PassportResponse.class);

        assertThat(passportResponse.getNumber()).isEqualTo(passportPostRequest.getNumber());
        assertThat(passportResponse.getDepartmentCode()).isEqualTo(passportPostRequest.getDepartmentCode());
        assertThat(passportResponse.getGivenDate()).isEqualTo(passportPostRequest.getGivenDate());

        Passport passportFromDB = passportsRepository
                .findByPassportNumber(passportPostRequest.getNumber())
                .orElseGet(() -> fail(String.format("Passport '%s' is not found in the database", passportPostRequest.getNumber())));

        assertThat(passportFromDB.getOwnerId()).isEqualTo(person.getId());
        assertThat(passportFromDB.getNumber()).isEqualTo(passportPostRequest.getNumber());
        assertThat(passportFromDB.getDepartmentCode()).isEqualTo(passportPostRequest.getDepartmentCode());
        assertThat(passportFromDB.getGivenDate()).isEqualTo(passportPostRequest.getGivenDate());
        assertThat(passportFromDB.getState()).isEqualTo(PassportState.ACTIVE);
    }

    private static Stream<Arguments> getPersonAndPassportWithNullValues() {
        return Stream.of(
                Arguments.of(generatePassportRequest().withGivenDate(null), "givenDate must not be null"),
                Arguments.of(generatePassportRequest().withGivenDate(StringUtils.EMPTY), "givenDate must not be null"),
                Arguments.of(generatePassportRequest().withNumber(StringUtils.EMPTY), "number must not be empty"),
                Arguments.of(generatePassportRequest().withDepartmentCode(StringUtils.EMPTY), "departmentCode must not be empty"),
                Arguments.of(generatePassportRequest().withGivenDate("1993-06-072"), "Text '1993-06-072' could not be parsed, unparsed text found at index 10")
        );
    }

    @ParameterizedTest
    @MethodSource("getPersonAndPassportWithNullValues")
    void testPostPassportNegative(PassportPostRequest passportPostRequest, String expectedErrorMessage) {
        Person person = personDataHandler.generatePersonData();

        ErrorMessage errorMessage =
                postPassport(person.getId(), passportPostRequest)
                        .statusCode(400)
                        .extract()
                        .as(ErrorMessage.class);

        assertThat(errorMessage.getMessages()).containsExactlyInAnyOrder(expectedErrorMessage);
    }

    @Test
    void testPostPassportOfNonexistentPersonNegative() {
        String nonExistentPersonId = generateRandomPersonId();

        ErrorMessage errorMessage =
                postPassport(nonExistentPersonId, generatePassportRequest())
                        .statusCode(404)
                        .extract()
                        .as(ErrorMessage.class);

        assertThat(errorMessage.getMessages()).containsExactlyInAnyOrder(String.format("Person with id '%s' is not found", nonExistentPersonId));
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

        assertThat(errorMessage.getMessages()).containsExactlyInAnyOrder(String.format("Passport with id '%s' already exists in the data", passport.getNumber()));
    }

}
