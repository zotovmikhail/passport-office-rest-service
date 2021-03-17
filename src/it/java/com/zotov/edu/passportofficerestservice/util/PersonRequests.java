package com.zotov.edu.passportofficerestservice.util;

import com.zotov.edu.passportofficerestservice.model.PersonPutRequest;
import com.zotov.edu.passportofficerestservice.model.PersonRequest;
import io.restassured.response.ValidatableResponse;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static io.restassured.RestAssured.*;

@UtilityClass
public class PersonRequests {

    public ValidatableResponse getPersons(String pageSize, String pageNumber) {
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
                .then();
    }

    public ValidatableResponse getPersons(String passportNumber) {
        return given()
                .queryParam("passportNumber", passportNumber)
                .when()
                .get("/persons")
                .then();
    }

    public ValidatableResponse postPerson(PersonRequest personRequest) {
        return given()
                .body(personRequest)
                .when()
                .post("/persons")
                .then();
    }

    public ValidatableResponse getPerson(String personId) {
        return given()
                .pathParam("personId", personId)
                .when()
                .get("/persons/{personId}")
                .then();
    }

    public ValidatableResponse putPerson(String personId, PersonPutRequest personPutRequest) {
        return given()
                .body(personPutRequest)
                .pathParam("personId", personId)
                .when()
                .put("/persons/{personId}")
                .then();
    }

    public ValidatableResponse deletePerson(String personId) {
        return given()
                .pathParam("personId", personId)
                .when()
                .delete("/persons/{personId}")
                .then();
    }
}
