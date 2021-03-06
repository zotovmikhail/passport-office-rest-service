package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.extension.IntegrationTest;
import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.PassportResponse;
import com.zotov.edu.passportofficerestservice.repository.PassportsRepository;
import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import com.zotov.edu.passportofficerestservice.util.PassportDataHandler;
import com.zotov.edu.passportofficerestservice.util.PersonDataHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.zotov.edu.passportofficerestservice.util.DataConverter.*;
import static com.zotov.edu.passportofficerestservice.util.PassportRequests.*;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.*;
import static org.assertj.core.api.Assertions.*;

@IntegrationTest
class LosePassportIT {

    @Autowired
    private PersonDataHandler personDataHandler;

    @Autowired
    private PassportDataHandler passportDataHandler;

    @Autowired
    private PassportsRepository passportsRepository;

    @Test
    void testLosePassportAndVerify() {
        Person person = personDataHandler.generatePersonData();
        Passport passport = passportDataHandler.generatePassportData(generatePassport(person.getId()));
        PassportResponse expectedPassportResponse = convertToPassportResponse(passport);

        PassportResponse passportResponse =
                losePassport(passport.getOwnerId(), passport.getNumber())
                        .statusCode(200)
                        .extract()
                        .as(PassportResponse.class);

        assertThat(passportResponse).isEqualTo(expectedPassportResponse);

        Passport foundPassport = passportsRepository
                .findByPassportNumber(passport.getNumber())
                .orElseGet(() -> fail(String.format("Passport '%s' is not found in the data.", passport.getNumber())));

        assertThat(foundPassport.getState()).isEqualTo(PassportState.LOST);
    }

    @Test
    void testLoseAlreadyLostPassportNegative() {
        Person person = personDataHandler.generatePersonData();
        Passport passport = passportDataHandler.generatePassportData(generatePassport(person.getId()).withState(PassportState.LOST));

        ErrorMessage errorMessage =
                losePassport(passport.getOwnerId(), passport.getNumber())
                        .statusCode(409)
                        .extract()
                        .as(ErrorMessage.class);

        assertThat(errorMessage.getMessages()).containsExactlyInAnyOrder(String.format("Passport with number '%s' is already lost.", passport.getNumber()));
    }

    @Test
    void testLoseNonexistentPassportByPassportIdNegative() {
        String nonexistentPassportNumber = generateRandomPassportNumber();
        Person person = personDataHandler.generatePersonData();

        ErrorMessage errorMessage =
                losePassport(person.getId(), nonexistentPassportNumber)
                        .statusCode(404)
                        .extract()
                        .as(ErrorMessage.class);

        assertThat(errorMessage.getMessages()).containsExactlyInAnyOrder(String.format("Passport with id '%s' is not found", nonexistentPassportNumber));
    }

    @Test
    void testLosePassportOfNonexistentPersonNegative() {
        String nonexistentPersonId = generateRandomPersonId();

        ErrorMessage errorMessage =
                losePassport(nonexistentPersonId, generateRandomString())
                        .statusCode(404)
                        .extract()
                        .as(ErrorMessage.class);

        assertThat(errorMessage.getMessages()).containsExactlyInAnyOrder(String.format("Person with id '%s' is not found", nonexistentPersonId));
    }
}
