package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.PassportResponse;
import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import com.zotov.edu.passportofficerestservice.util.PassportDataHandler;
import com.zotov.edu.passportofficerestservice.util.PersonDataHandler;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Stream;

import static com.zotov.edu.passportofficerestservice.util.DataConverter.*;
import static com.zotov.edu.passportofficerestservice.util.PassportRequests.*;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.*;
import static org.assertj.core.api.Assertions.*;

class GetPassportsIT extends BaseTest {

    @Autowired
    private PersonDataHandler personDataHandler;

    @Autowired
    private PassportDataHandler passportDataHandler;

    private static Stream<Arguments> getListOfPassportsAndOwnerId() {
        return Stream.of(
                Arguments.of(PassportState.ACTIVE, null, "Without the passport state query param"),
                Arguments.of(PassportState.ACTIVE, "ACTIVE", "With the passport state query param = ACTIVE"),
                Arguments.of(PassportState.LOST, "LOST", "With the passport state query param = LOST")
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPassportsAndOwnerId")
    void testGetPassportsByPassportStateAndVerify(PassportState passportState, String passportStateQueryParam, String description) {
        Person person = personDataHandler.generatePersonData();
        Passport passport = passportDataHandler.generatePassportData(generatePassport(person.getId()).withState(passportState));
        PassportResponse expectedPassportResponse = convertToPassportResponse(passport);

        Set<PassportResponse> passportsResponse = Set.of(
                getPassports(person.getId(), passportStateQueryParam)
                        .statusCode(200)
                        .extract()
                        .as(PassportResponse[].class));

        assertThat(passportsResponse).isEqualTo(Set.of(expectedPassportResponse));
    }

    private static Stream<Arguments> getListOfPassportsWithStateToVerifyEmptyResponse() {
        return Stream.of(
                Arguments.of(PassportState.ACTIVE, "LOST", "With the passport state query param = ACTIVE"),
                Arguments.of(PassportState.LOST, "ACTIVE", "With the passport state query param = LOST")
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPassportsWithStateToVerifyEmptyResponse")
    void testGetLostPassportsByPassportStateAndVerifyEmptyResponse(PassportState passportState, String passportStateQueryParam, String description) {
        Person person = personDataHandler.generatePersonData();
        passportDataHandler.generatePassportData(generatePassport(person.getId()).withState(passportState));

        Set<PassportResponse> passportsResponse = Set.of(
                getPassports(person.getId(), passportStateQueryParam)
                        .statusCode(200)
                        .extract()
                        .as(PassportResponse[].class));

        assertThat(passportsResponse).isEqualTo(Set.of());
    }


    private static Stream<Arguments> getListOfPassportsForMaxAndMinGivenDate() {
        return Stream.of(
                Arguments.of(generatePassportResponse().withGivenDate("2021-01-01"), null, null, "Without min and max given dates"),
                Arguments.of(generatePassportResponse().withGivenDate("2021-01-01"), "2020-01-01", null, "With min given date"),
                Arguments.of(generatePassportResponse().withGivenDate("2021-01-01"), "2020-01-01", "2021-01-02", "With min and max given dates"),
                Arguments.of(generatePassportResponse().withGivenDate("2021-01-01"), null, "2021-01-02", "With max given date")
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPassportsForMaxAndMinGivenDate")
    void testGetPassportsByMinAndMaxGivenDate(PassportResponse expectedPassportResponse, String minGivenDateParam,
                                              String maxGivenDateParam, String description) {
        Person person = personDataHandler.generatePersonData();
        Passport passport = passportDataHandler.generatePassportData(convertToPassportEntity(expectedPassportResponse, PassportState.ACTIVE, person.getId()));

        Set<PassportResponse> passportsResponse = Set.of(
                getPassports(passport.getOwnerId(), minGivenDateParam, maxGivenDateParam)
                        .statusCode(200)
                        .extract()
                        .as(PassportResponse[].class));

        assertThat(passportsResponse).isEqualTo(Set.of(expectedPassportResponse));
    }

    private static Stream<Arguments> getListOfPassportsForMaxAndMinGivenDateEmpty() {
        return Stream.of(
                Arguments.of(generatePassportResponse().withGivenDate("2021-01-01"), "2021-01-02", null, "Less than min given date"),
                Arguments.of(generatePassportResponse().withGivenDate("2021-01-01"), "2021-01-02", "2021-01-02", "Less than min given date, max is not empty"),
                Arguments.of(generatePassportResponse().withGivenDate("2021-01-01"), null, "2021-01-01", "Greater than max given date")
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPassportsForMaxAndMinGivenDateEmpty")
    void testGetPassportsByMinAndMaxGivenDateAndVerifyEmpty(PassportResponse expectedPassportResponse, String minGivenDateParam,
                                                            String maxGivenDateParam, String description) {
        Person person = personDataHandler.generatePersonData();
        Passport passport = passportDataHandler.generatePassportData(convertToPassportEntity(expectedPassportResponse, PassportState.ACTIVE, person.getId()));

        Set<PassportResponse> passportsResponse = Set.of(
                getPassports(passport.getOwnerId(), minGivenDateParam, maxGivenDateParam)
                        .statusCode(200)
                        .extract()
                        .as(PassportResponse[].class));

        assertThat(passportsResponse).isEqualTo(Set.of());
    }


}
