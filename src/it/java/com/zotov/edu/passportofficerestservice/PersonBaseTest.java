package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.PageResponse;
import com.zotov.edu.passportofficerestservice.model.PersonSpecification;
import com.zotov.edu.passportofficerestservice.repository.PersonsRepository;
import io.restassured.common.mapper.TypeRef;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static com.zotov.edu.passportofficerestservice.util.DataConverter.convertToPersonEntities;
import static com.zotov.edu.passportofficerestservice.util.DataConverter.convertToPersonEntity;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class PersonBaseTest extends BaseTest {

    @Autowired
    PersonsRepository personsRepository;

    PageResponse<PersonSpecification> generatePersonsPageData(int numberOfPersons, int size, int number) {
        List<PersonSpecification> personSpecifications = generatePersonsWithIds(numberOfPersons);
        personsRepository.saveAll(convertToPersonEntities(personSpecifications));
        return generatePersonsPage(personSpecifications, size, number);
    }

    PersonSpecification generatePersonData() {
        PersonSpecification personSpecification = generatePerson().withId(UUID.randomUUID().toString());
        personsRepository.save(convertToPersonEntity(personSpecification));
        return personSpecification;
    }

    void verifyPersonsPageSizeAndNumber(PageResponse<PersonSpecification> expectedPage, PageResponse<PersonSpecification> actualPageResponse) {
        assertThat(actualPageResponse.getSize()).isEqualTo(expectedPage.getSize());
        assertThat(actualPageResponse.getNumber()).isEqualTo(expectedPage.getNumber());
    }

    void verifyPersonNotFoundErrorMessages(ErrorMessage errorMessage, String entityId) {
        verifyNotFoundErrorMessages(errorMessage, entityId, "Person");
    }

    PageResponse<PersonSpecification> getForPersonResponse() {
        return when()
                    .get("/persons")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(new TypeRef<>() {
                    });
    }

    PageResponse<PersonSpecification> getForPersonResponseByPassportNumber(String pageSize, String pageNumber) {
        return given()
                    .queryParam("size", pageSize)
                    .queryParam("page", pageNumber)
                .when()
                    .get("/persons")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(new TypeRef<>() {
                    });
    }

    PageResponse<PersonSpecification> getForPersonResponseByPassportNumber(String passportNumber) {
        return given()
                    .queryParam("passportNumber", passportNumber)
                .when()
                    .get("/persons")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(new TypeRef<>() {
                    });
    }

    PersonSpecification postForPersonResponse(PersonSpecification personSpecification) {
        return given()
                    .body(personSpecification)
                .when()
                    .post("/persons")
                .then()
                    .statusCode(201)
                    .extract()
                    .as(PersonSpecification.class);
    }

    PersonSpecification getForPersonResponseById(String personId) {
        return given()
                    .pathParam("personId", personId)
                .when()
                    .get("/persons/{personId}")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(PersonSpecification.class);
    }

    ErrorMessage postPersonForBadRequest(PersonSpecification personSpecification) {
        return given()
                    .body(personSpecification)
                .when()
                    .post("/persons")
                .then()
                    .statusCode(400)
                    .extract()
                    .as(ErrorMessage.class);
    }

    PersonSpecification putForPersonResponse(PersonSpecification personSpecification) {
        return given()
                    .body(personSpecification)
                    .pathParam("personId", personSpecification.getId())
                .when()
                    .put("/persons/{personId}")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(PersonSpecification.class);
    }

    ErrorMessage putPersonForBadRequest(PersonSpecification personSpecification) {
        return given()
                    .body(personSpecification)
                    .pathParam("personId", personSpecification.getId())
                .when()
                    .put("/persons/{personId}")
                .then()
                    .statusCode(400)
                    .extract()
                    .as(ErrorMessage.class);
    }

    void deletePerson(String personId) {
        given()
                    .pathParam("personId", personId)
                .when()
                    .delete("/persons/{personId}")
                .then()
                    .statusCode(204);
    }

    ErrorMessage deletePersonForNotFound(String nonexistentPersonId) {
        return given()
                    .pathParam("personId", nonexistentPersonId)
                .when()
                    .delete("/persons/{personId}")
                .then()
                    .statusCode(404)
                    .extract()
                    .as(ErrorMessage.class);
    }

    ErrorMessage getForPersonNotFound(String nonexistentPersonId) {
        return given()
                    .pathParam("personId", nonexistentPersonId)
                .when()
                    .get("/persons/{personId}")
                .then()
                    .statusCode(404)
                    .extract()
                    .as(ErrorMessage.class);
    }

    ErrorMessage putForPersonNotFound(String nonexistentPersonId) {
        return given()
                    .body(generatePerson())
                    .pathParam("personId", nonexistentPersonId)
                .when()
                    .put("/persons/{personId}")
                .then()
                    .statusCode(404)
                    .extract()
                    .as(ErrorMessage.class);
    }
}
