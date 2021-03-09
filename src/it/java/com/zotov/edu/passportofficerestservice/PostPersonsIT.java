package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.PersonSpecification;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.generatePerson;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.generatePersons;
import static org.assertj.core.api.Assertions.assertThat;

class PostPersonsIT extends PersonBaseTest {

    private static Stream<Arguments> getListOfPersonsToCreate() {
        return Stream.of(
                Arguments.of(generatePersons(1))
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsToCreate")
    void testPostPersonsAndVerify(List<PersonSpecification> expectedPersonSpecifications) {
        expectedPersonSpecifications.forEach(person -> {
            PersonSpecification personSpecificationResponse = postForPersonResponse(person);
            verifyIsUUID(personSpecificationResponse.getId());
            assertThat(personSpecificationResponse.getName()).isEqualTo(person.getName());
            assertThat(personSpecificationResponse.getBirthday()).isEqualTo(person.getBirthday());
            assertThat(personSpecificationResponse.getCountry()).isEqualTo(person.getCountry());
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
    void testPostPersonsWithNullValuesNegative(PersonSpecification personSpecification, String field) {
        ErrorMessage errorMessage = postPersonForBadRequest(personSpecification);
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
    void testPostPersonsWithEmptyValuesNegative(PersonSpecification personSpecification, String field) {
        ErrorMessage errorMessage = postPersonForBadRequest(personSpecification);
        verifyEmptyValueErrorMessages(errorMessage, field);
    }

    private static Stream<Arguments> getListOfPersonsWithInvalidBirthday() {
        return Stream.of(
                Arguments.of(generatePerson().withBirthday("1993-06-072"))
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsWithInvalidBirthday")
    void testPostPersonsWithInvalidBirthdayNegative(PersonSpecification personSpecification) {
        ErrorMessage errorMessage = postPersonForBadRequest(personSpecification);
        verifyInvalidDateErrorMessages(errorMessage, personSpecification.getBirthday());
    }

    private static Stream<Arguments> getListOfPersonsWithInvalidCountry() {
        return Stream.of(
                Arguments.of(generatePerson().withCountry("Invalid")),
                Arguments.of(generatePerson().withCountry(StringUtils.EMPTY))
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsWithInvalidCountry")
    void testPostPersonsWithInvalidCountryNegative(PersonSpecification personSpecification) {
        ErrorMessage errorMessage = postPersonForBadRequest(personSpecification);
        verifyInvalidCountryErrorMessages(errorMessage);
    }

}
