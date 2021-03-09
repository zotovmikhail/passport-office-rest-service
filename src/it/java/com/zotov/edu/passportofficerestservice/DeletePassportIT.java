package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.PassportSpecification;
import com.zotov.edu.passportofficerestservice.model.PersonSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.*;
import static org.assertj.core.api.Assertions.assertThat;

class DeletePassportIT extends PassportsBaseTest {

    private Stream<Arguments> getPassportToDelete() {
        return Stream.of(
                Arguments.of(generatePassportData(generatePassport()))
        );
    }

    @ParameterizedTest
    @MethodSource("getPassportToDelete")
    void testDeletePassportAndVerify(PassportSpecification passportSpecification) {
        deletePassport(passportSpecification.getOwnerId(), passportSpecification.getNumber());
        assertThat(passportsRepository.existsByPassportNumber(passportSpecification.getNumber())).isFalse();
    }

    private Stream<Arguments> getPerson() {
        return Stream.of(
                Arguments.of(generatePersonData())
        );
    }

    @ParameterizedTest
    @MethodSource("getPerson")
    void testDeleteNonexistentPassportByPassportIdNegative(PersonSpecification personSpecification) {
        String nonexistentPassportId = UUID.randomUUID().toString();
        ErrorMessage errorMessage = deletePassportForNotFound(personSpecification.getId(), nonexistentPassportId);
        verifyPassportNotFoundErrorMessages(errorMessage, nonexistentPassportId);
    }

    @Test
    void testDeletePassportOfNonexistentPersonNegative() {
        String nonexistentPersonId = generateRandomPersonId();
        ErrorMessage errorMessage = deletePassportForNotFound(nonexistentPersonId, generateRandomString());
        verifyPersonNotFoundErrorMessages(errorMessage, nonexistentPersonId);
    }

}
