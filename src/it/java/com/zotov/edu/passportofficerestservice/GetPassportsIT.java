package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.PassportSpecification;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.generatePassport;
import static org.assertj.core.api.Assertions.assertThat;

class GetPassportsIT extends PassportsBaseTest {
    private Stream<Arguments> getListOfPassportsAndOwnerId() {
        return Stream.of(
                generatePassportsData(),
                generatePassportsData(generatePassport(), generatePassport())
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPassportsAndOwnerId")
    void testGetPassportsAndVerify(Set<PassportSpecification> expectedPassportSpecifications, String ownerId) {
        Set<PassportSpecification> passportsResponse = getForPassportResponse(ownerId);
        assertThat(passportsResponse).isEqualTo(expectedPassportSpecifications);
    }

    private Stream<Arguments> getListOfPassportsWithStateActive() {
        return Stream.of(
                generatePassportsData(),
                generatePassportsData(generatePassport(), generatePassport())
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPassportsWithStateActive")
    void testGetActivePassportsByPassportStateAndVerify(Set<PassportSpecification> expectedPassportSpecifications, String ownerId) {
        Set<PassportSpecification> passportsResponse = getForPassportResponseByPassportState(ownerId, "ACTIVE");
        assertThat(passportsResponse).isEqualTo(expectedPassportSpecifications);
    }

    @ParameterizedTest
    @MethodSource("getListOfPassportsWithStateActive")
    void testGetLostPassportsByPassportStateAndVerifyEmptyResponse(Set<PassportSpecification> expectedPassportSpecifications, String ownerId) {
        Set<PassportSpecification> passportsResponse = getForPassportResponseByPassportState(ownerId, "LOST");
        assertThat(passportsResponse).isEqualTo(Set.of());
    }

    private Stream<Arguments> getListOfPassportsWithStateLost() {
        return Stream.of(
                generatePassportsData(generatePassport().withState(PassportState.LOST))
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPassportsWithStateLost")
    void testGetLostPassportsByPassportStateAndVerify(Set<PassportSpecification> expectedPassportSpecifications, String ownerId) {
        Set<PassportSpecification> passportsResponse = getForPassportResponseByPassportState(ownerId, "LOST");
        assertThat(passportsResponse).isEqualTo(expectedPassportSpecifications);
    }

    private Stream<Arguments> getListOfPassportsForMaxAndMinGivenDate() {
        return Stream.of(
                Arguments.of(generatePassportData(generatePassport().withGivenDate("2021-01-01")), null, null),
                Arguments.of(generatePassportData(generatePassport().withGivenDate("2021-01-01")), "2020-01-01", null),
                Arguments.of(generatePassportData(generatePassport().withGivenDate("2021-01-01")), "2020-01-01", "2021-01-02"),
                Arguments.of(generatePassportData(generatePassport().withGivenDate("2021-01-01")), null, "2021-01-02")
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPassportsForMaxAndMinGivenDate")
    void testGetPassportsByMinAndMaxGivenDate(PassportSpecification expectedPassportSpecification, String minGivenDate, String maxGivenDate) {
        Set<PassportSpecification> passportsResponse =
                getForPassportResponseByMaxAndMinGivenDates(expectedPassportSpecification.getOwnerId(), minGivenDate, maxGivenDate);
        assertThat(passportsResponse).isEqualTo(Set.of(expectedPassportSpecification));
    }

    private Stream<Arguments> getListOfPassportsForMaxAndMinGivenDateEmpty() {
        return Stream.of(
                Arguments.of(generatePassportData(generatePassport().withGivenDate("2021-01-01")), "2021-01-02", null),
                Arguments.of(generatePassportData(generatePassport().withGivenDate("2021-01-01")), "2021-01-02", "2021-01-02"),
                Arguments.of(generatePassportData(generatePassport().withGivenDate("2021-01-01")), null, "2021-01-01")
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPassportsForMaxAndMinGivenDateEmpty")
    void testGetPassportsByMinAndMaxGivenDateAndVerifyEmpty(PassportSpecification expectedPassportSpecification, String minGivenDate, String maxGivenDate) {
        Set<PassportSpecification> passportsResponse =
                getForPassportResponseByMaxAndMinGivenDates(expectedPassportSpecification.getOwnerId(), minGivenDate, maxGivenDate);
        assertThat(passportsResponse).isEqualTo(Set.of());
    }

}
