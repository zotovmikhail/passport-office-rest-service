package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.PassportSpecification;
import com.zotov.edu.passportofficerestservice.model.PersonSpecification;
import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.generatePassport;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.generateRandomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class LosePassportIT extends PassportsBaseTest {
    private Stream<Arguments> getPassportToLose() {
        return Stream.of(
                Arguments.of(generatePassportData(generatePassport()))
        );
    }

    @ParameterizedTest
    @MethodSource("getPassportToLose")
    void testLosePassportAndVerify(PassportSpecification passportSpecification) {
        PassportSpecification lostPassportSpecification =
                losePassportForPassportResponse(passportSpecification.getOwnerId(), passportSpecification.getNumber());
        assertThat(lostPassportSpecification).isEqualTo(passportSpecification);
        Passport foundPassport = passportsRepository
                .findByPassportNumber(passportSpecification.getNumber())
                .orElseGet(() -> fail(String.format("Passport '%s' is not found in the data.", passportSpecification.getNumber())));
        assertThat(foundPassport.getState()).isEqualTo(PassportState.LOST);
    }

    private Stream<Arguments> getLostPassport() {
        return Stream.of(
                Arguments.of(generatePassportData(generatePassport().withState(PassportState.LOST)))
        );
    }

    @ParameterizedTest
    @MethodSource("getLostPassport")
    void testLoseAlreadyLostPassportNegative(PassportSpecification passportSpecification) {
        ErrorMessage errorMessage = losePassportForConflict(passportSpecification.getOwnerId(), passportSpecification.getNumber());
        verifyErrorMessages(errorMessage, List.of(String.format("Passport with number '%s' is already lost.", passportSpecification.getNumber())));
    }

    private Stream<Arguments> getListOfPersons() {
        return Stream.of(
                Arguments.of(generatePersonData())
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersons")
    void testLoseNonexistentPassportByPassportIdNegative(PersonSpecification personSpecification) {
        String nonexistentPassportId = UUID.randomUUID().toString();
        ErrorMessage errorMessage = losePassportForNotFound(personSpecification.getId(), nonexistentPassportId);
        verifyPassportNotFoundErrorMessages(errorMessage, nonexistentPassportId);
    }

    @Test
    void testLosePassportOfNonexistentPersonNegative() {
        String nonexistentPersonId = UUID.randomUUID().toString();
        ErrorMessage errorMessage = losePassportForNotFound(nonexistentPersonId, generateRandomString());
        verifyPersonNotFoundErrorMessages(errorMessage, nonexistentPersonId);
    }
}
