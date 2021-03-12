package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.repository.PassportsRepository;
import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import com.zotov.edu.passportofficerestservice.util.PersonDataHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.zotov.edu.passportofficerestservice.util.PassportRequests.deletePassport;
import static com.zotov.edu.passportofficerestservice.util.PassportRequests.deletePassportForNotFound;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.*;
import static org.assertj.core.api.Assertions.assertThat;

class DeletePassportIT extends BaseTest {

    @Autowired
    private PersonDataHandler personDataHandler;

    @Autowired
    private PassportsRepository passportsRepository;

    @Test
    void testDeletePassportAndVerify() {
        Person person = personDataHandler.generatePersonData();
        Passport passport = passportsRepository.save((generatePassport(person.getId())));

        deletePassport(passport.getOwnerId(), passport.getNumber());

        assertThat(passportsRepository.existsByPassportNumber(passport.getNumber())).isFalse();
    }

    @Test
    void testDeleteNonexistentPassportByPassportIdNegative() {
        String nonexistentPassportId = generateRandomPassportNumber();
        Person person = personDataHandler.generatePersonData();

        ErrorMessage errorMessage = deletePassportForNotFound(person.getId(), nonexistentPassportId);

        verifyNotFoundErrorMessages(errorMessage, nonexistentPassportId, "Passport");
    }

    @Test
    void testDeletePassportOfNonexistentPersonNegative() {
        String nonexistentPersonId = generateRandomPersonId();

        ErrorMessage errorMessage = deletePassportForNotFound(nonexistentPersonId, generateRandomString());

        verifyNotFoundErrorMessages(errorMessage, nonexistentPersonId, "Person");
    }

}
