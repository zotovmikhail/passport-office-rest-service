package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.PassportResponse;
import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import com.zotov.edu.passportofficerestservice.util.PassportDataHandler;
import com.zotov.edu.passportofficerestservice.util.PersonDataHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.zotov.edu.passportofficerestservice.util.DataConverter.convertToPassportResponse;
import static com.zotov.edu.passportofficerestservice.util.PassportRequests.*;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class LosePassportIT extends BaseTest {

    @Autowired
    private PersonDataHandler personDataHandler;

    @Autowired
    private PassportDataHandler passportDataHandler;

    @Test
    void testLosePassportAndVerify() {
        Person person = personDataHandler.generatePersonData();
        Passport passport = passportDataHandler.generatePassportData(generatePassport(person.getId()));
        PassportResponse expectedPassportResponse = convertToPassportResponse(passport);

        PassportResponse lostPassportSpecification =
                losePassportForPassportResponse(passport.getOwnerId(), passport.getNumber());

        assertThat(lostPassportSpecification).isEqualTo(expectedPassportResponse);

        Passport foundPassport = passportDataHandler.getPassportsRepository()
                .findByPassportNumber(passport.getNumber())
                .orElseGet(() -> fail(String.format("Passport '%s' is not found in the data.", passport.getNumber())));

        assertThat(foundPassport.getState()).isEqualTo(PassportState.LOST);
    }

    @Test
    void testLoseAlreadyLostPassportNegative() {
        Person person = personDataHandler.generatePersonData();
        Passport passport = passportDataHandler.generatePassportData(generatePassport(person.getId()).withState(PassportState.LOST));

        ErrorMessage errorMessage = losePassportForConflict(passport.getOwnerId(), passport.getNumber());

        verifyErrorMessages(errorMessage, List.of(String.format("Passport with number '%s' is already lost.", passport.getNumber())));
    }

    @Test
    void testLoseNonexistentPassportByPassportIdNegative() {
        String nonexistentPassportNumber = generateRandomPassportNumber();
        Person person = personDataHandler.generatePersonData();

        ErrorMessage errorMessage = losePassportForNotFound(person.getId(), nonexistentPassportNumber);

        verifyNotFoundErrorMessages(errorMessage, nonexistentPassportNumber, "Passport");
    }

    @Test
    void testLosePassportOfNonexistentPersonNegative() {
        String nonexistentPersonId = generateRandomPersonId();

        ErrorMessage errorMessage = losePassportForNotFound(nonexistentPersonId, generateRandomString());

        verifyNotFoundErrorMessages(errorMessage, nonexistentPersonId, "Person");
    }
}
