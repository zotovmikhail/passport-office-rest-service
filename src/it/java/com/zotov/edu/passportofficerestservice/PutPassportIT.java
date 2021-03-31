package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.extension.IntegrationTest;
import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.PassportPutRequest;
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
class PutPassportIT {

    @Autowired
    private PersonDataHandler personDataHandler;

    @Autowired
    private PassportDataHandler passportDataHandler;

    @Autowired
    private PassportsRepository passportsRepository;

    @Container
    public static final PostgreSQLContainer<?> postgreSqlContainer = new PostgreSQLContainer<>("postgres");

    @Test
    void testPutPersonAndVerify() {
        Person person = personDataHandler.generatePersonData();
        Passport initialPassport = passportDataHandler.generatePassportData(person.getId());
        PassportPutRequest passportRequestToUpdate = generatePassportPutRequest();

        PassportResponse passportResponse =
                putPassport(person.getId(), initialPassport.getNumber(), passportRequestToUpdate)
                        .statusCode(200)
                        .extract()
                        .as(PassportResponse.class);
        assertThat(passportResponse.getNumber()).isEqualTo(initialPassport.getNumber());
        assertThat(passportResponse.getGivenDate()).isEqualTo(passportRequestToUpdate.getGivenDate());
        assertThat(passportResponse.getDepartmentCode()).isEqualTo(passportRequestToUpdate.getDepartmentCode());

        Passport passportFromDB = passportsRepository
                .findByPassportNumber(initialPassport.getNumber())
                .orElseGet(() -> fail(String.format("Passport '%s' is not found in the data.", initialPassport.getNumber())));

        assertThat(passportFromDB.getNumber()).isEqualTo(initialPassport.getNumber());
        assertThat(passportFromDB.getGivenDate()).isEqualTo(passportRequestToUpdate.getGivenDate());
        assertThat(passportFromDB.getDepartmentCode()).isEqualTo(passportRequestToUpdate.getDepartmentCode());
        assertThat(passportFromDB.getOwnerId()).isEqualTo(person.getId());
        assertThat(passportFromDB.getState()).isEqualTo(PassportState.ACTIVE);
    }

    private static Stream<Arguments> getPassportWithNullValuesToUpdate() {
        return Stream.of(
                Arguments.of(generatePassportPutRequest().withGivenDate(null), "givenDate must not be null"),
                Arguments.of(generatePassportPutRequest().withGivenDate(StringUtils.EMPTY), "givenDate must not be null"),
                Arguments.of(generatePassportPutRequest().withDepartmentCode(StringUtils.EMPTY), "departmentCode must not be empty"),
                Arguments.of(generatePassportPutRequest().withGivenDate("1993-06-072"), "Text '1993-06-072' could not be parsed, unparsed text found at index 10")

        );
    }

    @ParameterizedTest
    @MethodSource("getPassportWithNullValuesToUpdate")
    void testPutPassportNegative(PassportPutRequest passportRequest, String expectedErrorMessage) {
        Person person = personDataHandler.generatePersonData();
        Passport initialPassport = passportDataHandler.generatePassportData(person.getId());

        ErrorMessage errorMessage =
                putPassport(person.getId(), initialPassport.getNumber(), passportRequest)
                        .statusCode(400)
                        .extract()
                        .as(ErrorMessage.class);

        assertThat(errorMessage.getMessages()).containsExactlyInAnyOrder(expectedErrorMessage);
    }

    @Test
    void testPutPassportOfNonexistentPersonNegative() {
        String nonExistentPersonId = generateRandomPersonId();

        ErrorMessage errorMessage =
                putPassport(nonExistentPersonId, generateRandomPassportNumber(), generatePassportPutRequest())
                        .statusCode(404)
                        .extract()
                        .as(ErrorMessage.class);

        assertThat(errorMessage.getMessages()).containsExactlyInAnyOrder(String.format("Person with id '%s' is not found", nonExistentPersonId));
    }

    @Test
    void testPutPassportOfNonexistentPassportNegative() {
        Person person = personDataHandler.generatePersonData();
        String nonExistentPassportNumber = generateRandomPassportNumber();

        ErrorMessage errorMessage =
                putPassport(person.getId(), nonExistentPassportNumber, generatePassportPutRequest())
                        .statusCode(404)
                        .extract()
                        .as(ErrorMessage.class);

        assertThat(errorMessage.getMessages()).containsExactlyInAnyOrder(String.format("Passport with id '%s' is not found", nonExistentPassportNumber));
    }

}
