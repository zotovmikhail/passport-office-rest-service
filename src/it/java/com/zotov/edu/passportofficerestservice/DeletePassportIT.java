package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.repository.PassportsRepository;
import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import com.zotov.edu.passportofficerestservice.util.PersonDataHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.zotov.edu.passportofficerestservice.util.PassportRequests.*;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.*;
import static org.assertj.core.api.Assertions.*;

class DeletePassportIT extends BaseTest {

    @Autowired
    private PersonDataHandler personDataHandler;

    @Autowired
    private PassportsRepository passportsRepository;

    @Test
    void testDeletePassportAndVerify() {
        Person person = personDataHandler.generatePersonData();
        Passport passport = passportsRepository.save((generatePassport(person.getId())));

        deletePassport(passport.getOwnerId(), passport.getNumber())
                .statusCode(204);

        assertThat(passportsRepository.existsByPassportNumber(passport.getNumber())).isFalse();
    }

    @Test
    void testDeleteNonexistentPassportByPassportIdNegative() {
        String nonexistentPassportId = generateRandomPassportNumber();
        Person person = personDataHandler.generatePersonData();

        ErrorMessage errorMessage =
                deletePassport(person.getId(), nonexistentPassportId)
                        .statusCode(404)
                        .extract()
                        .as(ErrorMessage.class);

        verifyNotFoundErrorMessages(errorMessage, nonexistentPassportId, "Passport");
    }

    @Test
    void testDeletePassportOfNonexistentPersonNegative() {
        String nonexistentPersonId = generateRandomPersonId();

        ErrorMessage errorMessage =
                deletePassport(nonexistentPersonId, generateRandomString())
                        .statusCode(404)
                        .extract()
                        .as(ErrorMessage.class);

        verifyNotFoundErrorMessages(errorMessage, nonexistentPersonId, "Person");
    }

}
