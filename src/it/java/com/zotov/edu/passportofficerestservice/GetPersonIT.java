package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.PersonSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


class GetPersonIT extends PersonBaseTest {

    private Stream<Arguments> getListOfPersonsById() {
        return Stream.of(
                Arguments.of(generatePersonData())
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersonsById")
    void testGetPersonsByPersonIdAndVerify(PersonSpecification expectedPersonSpecification) {
        PersonSpecification personSpecificationResponse = getForPersonResponseById(expectedPersonSpecification.getId());
        assertThat(personSpecificationResponse).isEqualTo(expectedPersonSpecification);
    }

    @Test
    void testGetNonexistentPersonsByPersonIdNegative() {
        String nonexistentPersonId = UUID.randomUUID().toString();
        ErrorMessage errorMessage = getForPersonNotFound(nonexistentPersonId);
        verifyPersonNotFoundErrorMessages(errorMessage, nonexistentPersonId);
    }

}
