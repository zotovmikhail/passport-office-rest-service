package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.Person;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.generatePerson;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.generatePersons;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class PostPersonsIT extends PersonBaseTest {

    private static Stream<Arguments> getListOfPersons() {
        return Stream.of(
                Arguments.of(generatePersons(1))
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersons")
    void testPostPersonsAndVerify(List<Person> expectedPersons) {
        expectedPersons.forEach(person -> {
            Person personResponse = postForPersonResponse(person);
            try {
                UUID.fromString(personResponse.getId());
            } catch (IllegalArgumentException exception) {
                fail(exception.getMessage());
            }
            assertThat(personResponse.getName()).isEqualTo(person.getName());
            assertThat(personResponse.getBirthday()).isEqualTo(person.getBirthday());
            assertThat(personResponse.getCountry()).isEqualTo(person.getCountry());
        });
    }

    private static Stream<Arguments> getListOfPersonsWithNullValues() {
        return Stream.of(
                Arguments.of(generatePerson().withBirthday(null), "birthday"),
                Arguments.of(generatePerson().withBirthday(StringUtils.EMPTY), "birthday"),
                Arguments.of(generatePerson().withCountry(null), "country")
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsWithNullValues")
    void testPostPersonsWithNullValuesNegative(Person person, String field) {
        ErrorMessage errorMessage = postPersonForBadRequest(person);
        verifyNullValueErrorMessages(errorMessage, field);
    }

    private static Stream<Arguments> getListOfPersonsWithEmptyValues() {
        return Stream.of(
                Arguments.of(generatePerson().withName(null), "name"),
                Arguments.of(generatePerson().withName(StringUtils.EMPTY), "name")
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsWithEmptyValues")
    void testPostPersonsWithEmptyValuesNegative(Person person, String field) {
        ErrorMessage errorMessage = postPersonForBadRequest(person);
        verifyEmptyValueErrorMessages(errorMessage, field);
    }

    private static Stream<Arguments> getListOfPersonsWithInvalidBirthday() {
        return Stream.of(
                Arguments.of(generatePerson().withBirthday("1993-06-072"))
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsWithInvalidBirthday")
    void testPostPersonsWithInvalidBirthdayNegative(Person person) {
        ErrorMessage errorMessage = postPersonForBadRequest(person);
        verifyInvalidDateErrorMessages(errorMessage, person.getBirthday());
    }

    private static Stream<Arguments> getListOfPersonsWithInvalidCountry() {
        return Stream.of(
                Arguments.of(generatePerson().withCountry("Invalid")),
                Arguments.of(generatePerson().withCountry(StringUtils.EMPTY))
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsWithInvalidCountry")
    void testPostPersonsWithInvalidCountryNegative(Person person) {
        ErrorMessage errorMessage = postPersonForBadRequest(person);
        verifyInvalidCountryErrorMessages(errorMessage);
    }

}
