package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.PageResponse;
import com.zotov.edu.passportofficerestservice.model.PersonSpecification;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class GetPersonsIT extends PersonBaseTest {

    private Stream<Arguments> getListOfPersons() {
        return Stream.of(
                Arguments.of(generatePersonsPageData(100, 100, 0))
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersons")
    void testGetPersonsAndVerifyDefaultValues(PageResponse<PersonSpecification> expectedPersonsPage) {
        PageResponse<PersonSpecification> pageResponse = getForPersonResponse();
        verifyPersonsPageSizeAndNumber(expectedPersonsPage, pageResponse);
        assertThat(pageResponse.getContent().size()).isEqualTo(expectedPersonsPage.getSize());
    }

    private Stream<Arguments> getListOfPersonsWithPageSizeAndPageNumber() {
        return Stream.of(
                Arguments.of(generatePersonsPageData(250, 50, 4))
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsWithPageSizeAndPageNumber")
    void testGetPersonsAndVerify(PageResponse<PersonSpecification> expectedPersonsPage) {
        PageResponse<PersonSpecification> pageResponse = getForPersonResponseByPassportNumber(
                String.valueOf(expectedPersonsPage.getSize()),
                String.valueOf(expectedPersonsPage.getNumber()));
        verifyPersonsPageSizeAndNumber(expectedPersonsPage, pageResponse);
        assertThat(pageResponse.getContent().size()).isEqualTo(expectedPersonsPage.getSize());
    }

    private Stream<Arguments> getListOfPersonsWithPassportNumber() {
        return Stream.of(
                Arguments.of(generatePersonsPageData(0, 100, 0), "NonexistentPassport")
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsWithPassportNumber")
    void testGetPersonsByPassportNumberAndVerify(PageResponse<PersonSpecification> expectedPersonsPage, String passportNumber) {
        PageResponse<PersonSpecification> pageResponse = getForPersonResponseByPassportNumber(passportNumber);
        verifyPersonsPageSizeAndNumber(expectedPersonsPage, pageResponse);
        assertThat(pageResponse.getContent()).isEmpty();
    }

    private Stream<Arguments> getListOfPersonsWithInvalidPageSizeAndPageNumber() {
        return Stream.of(
                Arguments.of(generatePersonsPageData(0, 100, 0), "invalidPageSize", "invalidPageNumber"),
                Arguments.of(generatePersonsPageData(0, 100, 0), "100000000000000000", "100000000000000000")
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsWithInvalidPageSizeAndPageNumber")
    void testGetPersonsByInvalidPageSizeAndNumberNegative(PageResponse<PersonSpecification> expectedPersonsPage, String pageSize, String pageNumber) {
        PageResponse<PersonSpecification> pageResponse = getForPersonResponseByPassportNumber(pageSize, pageNumber);
        verifyPersonsPageSizeAndNumber(expectedPersonsPage, pageResponse);
    }

}
