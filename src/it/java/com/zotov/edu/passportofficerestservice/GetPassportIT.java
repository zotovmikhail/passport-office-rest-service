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

import static com.zotov.edu.passportofficerestservice.util.DataConverter.*;
import static com.zotov.edu.passportofficerestservice.util.PassportRequests.*;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.*;
import static org.assertj.core.api.Assertions.*;

class GetPassportIT extends BaseTest {

    @Autowired
    private PersonDataHandler personDataHandler;

    @Autowired
    private PassportDataHandler passportDataHandler;

    @Test
    void testGetPassportByPassportId() {
        Person person = personDataHandler.generatePersonData();
        Passport passport = passportDataHandler.generatePassportData(generatePassport(person.getId()).withState(PassportState.ACTIVE));
        PassportResponse expectedPassportResponse = convertToPassportResponse(passport);

        PassportResponse passportResponse =
                getPassport(passport.getOwnerId(), passport.getNumber())
                        .statusCode(200)
                        .extract()
                        .as(PassportResponse.class);

        assertThat(passportResponse).isEqualTo(expectedPassportResponse);
    }

    @Test
    void testGetNonexistentPassportByPassportIdNegative() {
        String nonExistentPassportNumber = generateRandomPassportNumber();
        Person person = personDataHandler.generatePersonData();

        ErrorMessage errorMessage =
                getPassport(person.getId(), nonExistentPassportNumber)
                        .statusCode(404)
                        .extract()
                        .as(ErrorMessage.class);

        verifyErrorMessages(errorMessage, String.format("Passport with id '%s' is not found", nonExistentPassportNumber));
    }

    @Test
    void testGetPassportOfNonexistentPersonByPassportIdNegative() {
        String nonExistentPersonId = generateRandomPersonId();

        ErrorMessage errorMessage =
                getPassport(nonExistentPersonId, generateRandomPassportNumber())
                        .statusCode(404)
                        .extract()
                        .as(ErrorMessage.class);

        verifyErrorMessages(errorMessage, String.format("Person with id '%s' is not found", nonExistentPersonId));
    }

}
