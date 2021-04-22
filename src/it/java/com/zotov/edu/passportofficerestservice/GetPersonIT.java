package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.extension.IntegrationTest;
import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.PersonResponse;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import com.zotov.edu.passportofficerestservice.util.PersonDataHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.zotov.edu.passportofficerestservice.util.DataConverter.*;
import static com.zotov.edu.passportofficerestservice.util.PersonRequests.*;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.*;
import static org.assertj.core.api.Assertions.*;

@IntegrationTest
class GetPersonIT {

    @Autowired
    private PersonDataHandler personDataHandler;

    @Test
    void testGetPersonsByPersonIdAndVerify() {
        Person person = personDataHandler.generatePersonData();
        PersonResponse expectedPersonResponse = convertToPersonResponse(person);

        PersonResponse personResponse =
                getPerson(person.getId())
                        .statusCode(200)
                        .extract()
                        .as(PersonResponse.class);

        assertThat(personResponse).isEqualTo(expectedPersonResponse);
    }

    @Test
    void testGetNonexistentPersonsByPersonIdNegative() {
        String nonexistentPersonId = generateRandomPersonId();

        ErrorMessage errorMessage =
                getPerson(nonexistentPersonId)
                        .statusCode(404)
                        .extract()
                        .as(ErrorMessage.class);

        assertThat(errorMessage.getMessages()).containsExactlyInAnyOrder(String.format("Person with id '%s' is not found", nonexistentPersonId));
    }

}
