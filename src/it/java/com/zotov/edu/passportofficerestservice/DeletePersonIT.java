package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.extension.IntegrationTest;
import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.repository.PersonsRepository;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static com.zotov.edu.passportofficerestservice.util.PersonRequests.*;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.*;
import static org.assertj.core.api.Assertions.*;

@IntegrationTest
class DeletePersonIT {

    @Autowired
    private PersonsRepository personsRepository;

    @Test
    void testDeletePersonAndVerify() {
        Person person = personsRepository.create(generatePerson());

        deletePerson(person.getId())
                .statusCode(204);

        assertThat(personsRepository.existsById(person.getId())).isFalse();
    }

    @Test
    void testDeleteNonexistentPersonByPersonIdNegative() {
        String nonexistentPersonId = UUID.randomUUID().toString();

        ErrorMessage errorMessage =
                deletePerson(nonexistentPersonId)
                        .statusCode(404)
                        .extract()
                        .as(ErrorMessage.class);

        assertThat(errorMessage.getMessages()).containsExactlyInAnyOrder(String.format("Person with id '%s' is not found", nonexistentPersonId));
    }

}
