package com.zotov.edu.passportofficerestservice.util;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.PassportPutRequest;
import com.zotov.edu.passportofficerestservice.model.PassportRequest;
import com.zotov.edu.passportofficerestservice.model.PassportResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static io.restassured.RestAssured.given;

public class PassportRequests {
    public static PassportResponse postForPassportResponse(String personId, PassportRequest passportRequest) {
        return given()
                .body(passportRequest)
                .pathParam("personId", personId)
                .when()
                .post("/persons/{personId}/passports")
                .then()
                .statusCode(201)
                .extract()
                .as(PassportResponse.class);
    }

    public static ErrorMessage postPassportForBadRequest(String personId, PassportRequest personRequest) {
        return given()
                .body(personRequest)
                .pathParam("personId", personId)
                .when()
                .post("/persons/{personId}/passports")
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorMessage.class);
    }

    public static PassportResponse getForPassportResponseByPassportNumber(String personId, String passportNumber) {
        return given()
                .pathParam("personId", personId)
                .pathParam("passportNumber", passportNumber)
                .when()
                .get("/persons/{personId}/passports/{passportNumber}")
                .then()
                .statusCode(200)
                .extract()
                .as(PassportResponse.class);
    }

    public static Set<PassportResponse> getForPassportResponseByPassportState(String personId, String passportState) {
        Map<String,String> queryParams = new HashMap<>();
        if (Objects.nonNull(passportState)) {
            queryParams.put("state", passportState);
        }

        return Set.of(given()
                .pathParam("personId", personId)
                .queryParams(queryParams)
                .when()
                .get("/persons/{personId}/passports")
                .then()
                .statusCode(200)
                .extract()
                .as(PassportResponse[].class)
                .clone());
    }

    public static Set<PassportResponse> getForPassportResponseByMaxAndMinGivenDates(String personId, String minGivenDate, String maxGivenDate) {
        Map<String,String> queryParams = new HashMap<>();
        if (Objects.nonNull(minGivenDate)) {
            queryParams.put("minGivenDate", minGivenDate);
        }
        if (Objects.nonNull(maxGivenDate)) {
            queryParams.put("maxGivenDate", maxGivenDate);
        }

        return Set.of(given()
                .pathParam("personId", personId)
                .queryParams(queryParams)
                .when()
                .get("/persons/{personId}/passports")
                .then()
                .statusCode(200)
                .extract()
                .as(PassportResponse[].class)
                .clone());
    }

    public static ErrorMessage getForNotFoundByPassportNumber(String personId, String nonExistentPassportNumber) {
        return given()
                .pathParam("personId", personId)
                .pathParam("passportNumber", nonExistentPassportNumber)
                .when()
                .get("/persons/{personId}/passports/{passportNumber}")
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorMessage.class);
    }

    public static ErrorMessage postForNotFoundByPassportNumber(String nonexistentPersonId, PassportRequest passportRequest) {
        return given()
                .body(passportRequest)
                .pathParam("personId", nonexistentPersonId)
                .when()
                .post("/persons/{personId}/passports/")
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorMessage.class);
    }

    public static ErrorMessage postForConflictPassport(String personId, PassportRequest passportRequest) {
        return given()
                .body(passportRequest)
                .pathParam("personId", personId)
                .when()
                .post("/persons/{personId}/passports/")
                .then()
                .statusCode(409)
                .extract()
                .as(ErrorMessage.class);
    }

    public static PassportResponse losePassportForPassportResponse(String personId, String passportNumber) {
        return given()
                .pathParam("personId", personId)
                .pathParam("passportNumber", passportNumber)
                .when()
                .post("/persons/{personId}/passports/{passportNumber}/loss")
                .then()
                .statusCode(200)
                .extract()
                .as(PassportResponse.class);
    }


    public static PassportResponse putForPassportResponse(String personId, String passportNumber, PassportPutRequest passportRequest) {
        return given()
                .body(passportRequest)
                .pathParam("personId", personId)
                .pathParam("passportNumber", passportNumber)
                .when()
                .put("/persons/{personId}/passports/{passportNumber}")
                .then()
                .statusCode(200)
                .extract()
                .as(PassportResponse.class);
    }

    public static ErrorMessage putPassportForBadRequest(String personId, String passportNumber, PassportPutRequest passportPutRequest) {
        return given()
                .body(passportPutRequest)
                .pathParam("personId", personId)
                .pathParam("passportNumber", passportNumber)
                .when()
                .put("/persons/{personId}/passports/{passportNumber}")
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorMessage.class);
    }

    public static ErrorMessage putForNotFoundByPassportNumber(String nonexistentPersonId, String passportNumber, PassportPutRequest passportPutRequest) {
        return given()
                .body(passportPutRequest)
                .pathParam("personId", nonexistentPersonId)
                .pathParam("passportNumber", passportNumber)
                .when()
                .put("/persons/{personId}/passports/{passportNumber}")
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorMessage.class);
    }

    public static void deletePassport(String ownerId, String passportNumber) {
        given()
                .pathParam("personId", ownerId)
                .pathParam("passportNumber", passportNumber)
                .when()
                .delete("/persons/{personId}/passports/{passportNumber}")
                .then()
                .statusCode(204);
    }

    public static ErrorMessage deletePassportForNotFound(String personId, String nonexistentPassportId) {
        return given()
                .pathParam("personId", personId)
                .pathParam("passportNumber", nonexistentPassportId)
                .when()
                .delete("/persons/{personId}/passports/{passportNumber}")
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorMessage.class);
    }

    public static ErrorMessage losePassportForNotFound(String personId, String nonexistentPassportId) {
        return given()
                .pathParam("personId", personId)
                .pathParam("passportNumber", nonexistentPassportId)
                .when()
                .post("/persons/{personId}/passports/{passportNumber}/loss")
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorMessage.class);
    }

    public static ErrorMessage losePassportForConflict(String personId, String nonexistentPassportId) {
        return given()
                .pathParam("personId", personId)
                .pathParam("passportNumber", nonexistentPassportId)
                .when()
                .post("/persons/{personId}/passports/{passportNumber}/loss")
                .then()
                .statusCode(409)
                .extract()
                .as(ErrorMessage.class);
    }
}
