package com.zotov.edu.passportofficerestservice.util;

import com.zotov.edu.passportofficerestservice.model.*;
import io.restassured.common.mapper.TypeRef;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.generatePerson;
import static io.restassured.RestAssured.given;

public class PersonRequests {

    public static PageResponse<PersonResponse> getForPersonResponse(String pageSize, String pageNumber) {
        Map<String, String> queryParams = new HashMap<>();
        if (Objects.nonNull(pageSize)) {
            queryParams.put("size", pageSize);
        }
        if (Objects.nonNull(pageNumber)) {
            queryParams.put("page", pageNumber);
        }

        return given()
                .queryParams(queryParams)
                .when()
                .get("/persons")
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<>() {
                });
    }

    public static PageResponse<PersonResponse> getForPersonResponseByPassportNumber(String passportNumber) {
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

    public static PersonResponse postForPersonResponse(PersonRequest personResponse) {
        return given()
                .body(personResponse)
                .when()
                .post("/persons")
                .then()
                .statusCode(201)
                .extract()
                .as(PersonResponse.class);
    }

    public static PersonResponse getForPersonResponseById(String personId) {
        return given()
                .pathParam("personId", personId)
                .when()
                .get("/persons/{personId}")
                .then()
                .statusCode(200)
                .extract()
                .as(PersonResponse.class);
    }

    public static ErrorMessage postPersonForBadRequest(PersonRequest personRequest) {
        return given()
                .body(personRequest)
                .when()
                .post("/persons")
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorMessage.class);
    }

    public static PersonResponse putForPersonResponse(PersonPutRequest personPutRequest) {
        return given()
                .body(personPutRequest)
                .pathParam("personId", personPutRequest.getId())
                .when()
                .put("/persons/{personId}")
                .then()
                .statusCode(200)
                .extract()
                .as(PersonResponse.class);
    }

    public static ErrorMessage putPersonForBadRequest(PersonPutRequest personPutRequest) {
        return given()
                .body(personPutRequest)
                .pathParam("personId", personPutRequest.getId())
                .when()
                .put("/persons/{personId}")
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorMessage.class);
    }

    public static void deletePerson(String personId) {
        given()
                .pathParam("personId", personId)
                .when()
                .delete("/persons/{personId}")
                .then()
                .statusCode(204);
    }

    public static ErrorMessage deletePersonForNotFound(String nonexistentPersonId) {
        return given()
                .pathParam("personId", nonexistentPersonId)
                .when()
                .delete("/persons/{personId}")
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorMessage.class);
    }

    public static ErrorMessage getForPersonNotFound(String nonexistentPersonId) {
        return given()
                .pathParam("personId", nonexistentPersonId)
                .when()
                .get("/persons/{personId}")
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorMessage.class);
    }

    public static ErrorMessage putForPersonNotFound(String nonexistentPersonId) {
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
