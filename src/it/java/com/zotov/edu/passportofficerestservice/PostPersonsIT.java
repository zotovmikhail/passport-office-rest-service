package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.PersonRequest;
import com.zotov.edu.passportofficerestservice.model.PersonResponse;
import com.zotov.edu.passportofficerestservice.repository.PersonsRepository;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static com.zotov.edu.passportofficerestservice.util.PersonRequests.postForPersonResponse;
import static com.zotov.edu.passportofficerestservice.util.PersonRequests.postPersonForBadRequest;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.generatePersonRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@Slf4j
class PostPersonsIT extends BaseTest {

    @Autowired
    private PersonsRepository personsRepository;

    @Test
    void testPostPersonAndVerify() {
        PersonRequest personRequest = generatePersonRequest();

        PersonResponse personResponse = postForPersonResponse(personRequest);

        verifyIsUUID(personResponse.getId());
        assertThat(personResponse.getName()).isEqualTo(personRequest.getName());
        assertThat(personResponse.getBirthday()).isEqualTo(personRequest.getBirthday());
        assertThat(personResponse.getCountry()).isEqualTo(personRequest.getCountry());

        Person personFromDB = personsRepository
                .findById(personResponse.getId())
                .orElseGet(() -> fail(String.format("Person '%s' is not found in the database", personResponse.getId())));

        assertThat(personFromDB.getName()).isEqualTo(personRequest.getName());
        assertThat(personFromDB.getBirthday()).isEqualTo(personRequest.getBirthday());
        assertThat(personFromDB.getCountry()).isEqualTo(personRequest.getCountry());
    }

    private static Stream<Arguments> getListOfPersonsWithNullValues() {
        return Stream.of(
                Arguments.of(generatePersonRequest().withBirthday(null), "birthday", "Null birthday value"),
                Arguments.of(generatePersonRequest().withBirthday(StringUtils.EMPTY), "birthday", "Empty birthday value"),
                Arguments.of(generatePersonRequest().withCountry(null), "country", "Null country value")
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsWithNullValues")
    void testPostPersonsWithNullValuesNegative(PersonRequest personRequest, String fieldName, String description) {
        log.info(description);

        ErrorMessage errorMessage = postPersonForBadRequest(personRequest);

        verifyNullValueErrorMessages(errorMessage, fieldName);
    }

    private static Stream<Arguments> getListOfPersonsWithEmptyValues() {
        return Stream.of(
                Arguments.of(generatePersonRequest().withName(null), "name", "Null name value"),
                Arguments.of(generatePersonRequest().withName(StringUtils.EMPTY), "name", "Empty name value")
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsWithEmptyValues")
    void testPostPersonsWithEmptyValuesNegative(PersonRequest personRequest, String field, String description) {
        log.info(description);

        ErrorMessage errorMessage = postPersonForBadRequest(personRequest);

        verifyEmptyValueErrorMessages(errorMessage, field);
    }

    @Test
    void testPostPersonsWithInvalidBirthdayNegative() {
        String invalidBirthday = "1993-06-072";

        ErrorMessage errorMessage = postPersonForBadRequest(generatePersonRequest().withBirthday(invalidBirthday));

        verifyInvalidDateErrorMessages(errorMessage, invalidBirthday);
    }

    private static Stream<Arguments> getListOfPersonsWithInvalidCountry() {
        return Stream.of(
                Arguments.of(generatePersonRequest().withCountry("Invalid"), "Invalid country value"),
                Arguments.of(generatePersonRequest().withCountry(StringUtils.EMPTY), "Empty country value")
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsWithInvalidCountry")
    void testPostPersonsWithInvalidCountryNegative(PersonRequest personRequest, String description) {
        log.info(description);

        ErrorMessage errorMessage = postPersonForBadRequest(personRequest);

        verifyInvalidCountryErrorMessages(errorMessage);
    }

}
