package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.repository.PersonsRepository;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static com.zotov.edu.passportofficerestservice.util.PersonRequests.deletePerson;
import static com.zotov.edu.passportofficerestservice.util.PersonRequests.deletePersonForNotFound;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.generatePerson;
import static org.assertj.core.api.Assertions.assertThat;

class DeletePersonIT extends BaseTest {

    @Autowired
    private PersonsRepository personsRepository;

    @Test
    void testDeletePersonAndVerify() {
        Person person = personsRepository.save(generatePerson());

        deletePerson(person.getId());

        assertThat(personsRepository.existsById(person.getId())).isFalse();
    }

    @Test
    void testDeleteNonexistentPersonByPersonIdNegative() {
        String nonexistentPersonId = UUID.randomUUID().toString();

        ErrorMessage errorMessage = deletePersonForNotFound(nonexistentPersonId);

        verifyNotFoundErrorMessages(errorMessage, nonexistentPersonId, "Person");
    }

}
