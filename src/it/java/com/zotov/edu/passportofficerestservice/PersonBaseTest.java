package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.Person;
import com.zotov.edu.passportofficerestservice.model.PersonsPageResponse;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class PersonBaseTest extends BaseTest {

    void verifyPersonsPageResponse(List<Person> expectedPersons, PersonsPageResponse personsPageResponse,
                                   int pageSize, int pageNumber) {
        int totalElements = expectedPersons.size();
        int totalPages = totalElements == 0 ? 0 : totalElements <= pageSize ? 1 : totalElements / pageSize;
        List<Person> expectedPersonsPage = expectedPersons
                .stream()
                .skip((long) pageNumber * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());

        assertThat(personsPageResponse.getSize()).isEqualTo(pageSize);
        assertThat(personsPageResponse.getTotalPages()).isEqualTo(totalPages);
        assertThat(personsPageResponse.getTotalElements()).isEqualTo(totalElements);
        assertThat(personsPageResponse.getNumber()).isEqualTo(pageNumber);
        assertThat(personsPageResponse.getContent()).isEqualTo(expectedPersonsPage);
    }

    void verifyPersonNotFoundErrorMessages(ErrorMessage errorMessage,String entityId) {
        verifyNotFoundErrorMessages(errorMessage, entityId, "Person");
    }

    PersonsPageResponse getForPersonResponse() {
        return when()
                .get("/persons")
                .then()
                .statusCode(200)
                .extract()
                .as(PersonsPageResponse.class);
    }

    PersonsPageResponse getForPersonResponseByPassportNumber(String pageSize, String pageNumber) {
        return given()
                .queryParam("size", pageSize)
                .queryParam("page", pageNumber)
                .when()
                .get("/persons")
                .then()
                .statusCode(200)
                .extract()
                .as(PersonsPageResponse.class);
    }

    PersonsPageResponse getForPersonResponseByPassportNumber(String passportNumber) {
        return given()
                .queryParam("passportNumber", passportNumber)
                .when()
                .get("/persons")
                .then()
                .statusCode(200)
                .extract()
                .as(PersonsPageResponse.class);
    }

    Person postForPersonResponse(Person person) {
        return given()
                .body(person)
                .when()
                .post("/persons")
                .then()
                .statusCode(201)
                .extract()
                .as(Person.class);
    }

    Person getForPersonResponseById(String personId) {
        return given()
                .pathParam("personId", personId)
                .when()
                .get("/persons/{personId}")
                .then()
                .statusCode(200)
                .extract()
                .as(Person.class);
    }

    ErrorMessage postPersonForBadRequest(Person person) {
        return given()
                .body(person)
                .when()
                .post("/persons")
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorMessage.class);
    }

    Person putForPersonResponse(Person person) {
        return given()
                .body(person)
                .pathParam("personId", person.getId())
                .when()
                .put("/persons/{personId}")
                .then()
                .statusCode(200)
                .extract()
                .as(Person.class);
    }

    ErrorMessage putPersonForBadRequest(Person person) {
        return given()
                .body(person)
                .pathParam("personId", person.getId())
                .when()
                .put("/persons/{personId}")
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorMessage.class);
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
}
