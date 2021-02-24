package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.Person;
import com.zotov.edu.passportofficerestservice.model.PersonsPageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.generatePersons;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GetPersonsIT extends PersonBaseTest {

    private static final int DEFAULT_PAGE_SIZE = 100;
    private static final int DEFAULT_PAGE_NUMBER = 0;

    private static Stream<Arguments> getListOfPersons() {
        return Stream.of(
                Arguments.of(List.of()),
                Arguments.of(generatePersons(100))
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersons")
    void testGetPersonsAndVerifyDefaultValues(List<Person> expectedPersons) {
        expectedPersons.forEach(person -> person.setId(postForPersonResponse(person).getId()));
        PersonsPageResponse personsPageResponse = getForPersonResponse();
        verifyPersonsPageResponse(expectedPersons, personsPageResponse, DEFAULT_PAGE_SIZE, DEFAULT_PAGE_NUMBER);
    }

    private static Stream<Arguments> getListOfPersonsWithPageSizeAndPageNumber() {
        return Stream.of(
                Arguments.of(generatePersons(250), 50, 4),
                Arguments.of(generatePersons(50), 50, 2)
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsWithPageSizeAndPageNumber")
    void testGetPersonsAndVerify(List<Person> expectedPersons, int pageSize, int pageNumber) {
        expectedPersons.forEach(person -> person.setId(postForPersonResponse(person).getId()));
        PersonsPageResponse personsPageResponse = getForPersonResponseByPassportNumber(String.valueOf(pageSize), String.valueOf(pageNumber));
        verifyPersonsPageResponse(expectedPersons, personsPageResponse, pageSize, pageNumber);
    }

    private static Stream<Arguments> getListOfPersonsWithPassportNumber() {
        return Stream.of(
                Arguments.of(List.of(), "NonexistentPassport")
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsWithPassportNumber")
    void testGetPersonsByPassportNumberAndVerify(List<Person> expectedPersons, String passportNumber) {
        expectedPersons.forEach(person -> person.setId(postForPersonResponse(person).getId()));
        PersonsPageResponse personsPageResponse = getForPersonResponseByPassportNumber(passportNumber);
        verifyPersonsPageResponse(expectedPersons, personsPageResponse, DEFAULT_PAGE_SIZE, DEFAULT_PAGE_NUMBER);
    }

    private static Stream<Arguments> getListOfPersonsById() {
        return Stream.of(
                Arguments.of(generatePersons(1))
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsById")
    void testGetPersonsByPersonIdAndVerify(List<Person> expectedPersons) {
        expectedPersons.forEach(person -> {
            person.setId(postForPersonResponse(person).getId());
            Person personResponse = getForPersonResponseById(person.getId());
            assertThat(personResponse).isEqualTo(person);
        });
    }

    @Test
    void testGetNonexistentPersonsByPersonIdNegative() {
        String nonexistentPersonId = UUID.randomUUID().toString();
        ErrorMessage errorMessage =
                given()
                    .pathParam("personId", nonexistentPersonId)
                .when()
                    .get("/persons/{personId}")
                .then()
                    .statusCode(404)
                    .extract()
                    .as(ErrorMessage.class);
        verifyPersonNotFoundErrorMessages(errorMessage, nonexistentPersonId);
    }

    private static Stream<Arguments> getListOfPersonsWithInvalidPageSizeAndPageNumber() {
        return Stream.of(
                Arguments.of(List.of(), "invalidPageSize", "invalidPageNumber"),
                Arguments.of(List.of(), "100000000000000000", "100000000000000000")
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsWithInvalidPageSizeAndPageNumber")
    void testGetPersonsByInvalidPageSizeAndNumberNegative(List<Person> expectedPersons, String pageSize, String pageNumber) {
        expectedPersons.forEach(person -> person.setId(postForPersonResponse(person).getId()));
        PersonsPageResponse personsPageResponse = getForPersonResponseByPassportNumber(pageSize, pageNumber);
        verifyPersonsPageResponse(expectedPersons, personsPageResponse, DEFAULT_PAGE_SIZE, DEFAULT_PAGE_NUMBER);
    }

}
