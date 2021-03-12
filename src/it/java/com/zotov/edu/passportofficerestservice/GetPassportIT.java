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

import static com.zotov.edu.passportofficerestservice.util.DataConverter.convertToPassportResponse;
import static com.zotov.edu.passportofficerestservice.util.PassportRequests.getForNotFoundByPassportNumber;
import static com.zotov.edu.passportofficerestservice.util.PassportRequests.getForPassportResponseByPassportNumber;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.*;
import static org.assertj.core.api.Assertions.assertThat;

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

        PassportResponse passportSpecificationResponse =
                getForPassportResponseByPassportNumber(passport.getOwnerId(), passport.getNumber());

        assertThat(passportSpecificationResponse).isEqualTo(expectedPassportResponse);
    }

    @Test
    void testGetNonexistentPassportByPassportIdNegative() {
        String nonExistentPassportNumber = generateRandomPassportNumber();
        Person person = personDataHandler.generatePersonData();

        ErrorMessage errorMessage = getForNotFoundByPassportNumber(person.getId(), nonExistentPassportNumber);

        verifyNotFoundErrorMessages(errorMessage, nonExistentPassportNumber, "Passport");
    }

    @Test
    void testGetPassportOfNonexistentPersonByPassportIdNegative() {
        String nonExistentPersonId = generateRandomPersonId();

        ErrorMessage errorMessage = getForNotFoundByPassportNumber(nonExistentPersonId, generateRandomPassportNumber());

        verifyNotFoundErrorMessages(errorMessage, nonExistentPersonId, "Person");
    }

}
