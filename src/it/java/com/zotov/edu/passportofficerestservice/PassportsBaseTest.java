package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.Passport;

import java.util.Set;

import static io.restassured.RestAssured.given;

public abstract class PassportsBaseTest extends PersonBaseTest {

    void verifyPassportNotFoundErrorMessages(ErrorMessage errorMessage, String nonExistentPassportNumber) {
        verifyNotFoundErrorMessages(errorMessage, nonExistentPassportNumber, "Passport");
    }

    Passport postForPassportResponse(String personId, Passport passport) {
        return given()
                    .body(passport)
                    .pathParam("personId", personId)
                .when()
                 .post("/persons/{personId}/passports")
                .then()
                    .statusCode(201)
                    .extract()
                    .as(Passport.class);
    }

    ErrorMessage postPassportForBadRequest(String personId, Passport passport) {
        return given()
                    .body(passport)
                    .pathParam("personId", personId)
                .when()
                    .post("/persons/{personId}/passports")
                .then()
                    .statusCode(400)
                    .extract()
                    .as(ErrorMessage.class);
    }

    Set<Passport> getForPassportResponse(String personId) {
        return Set.of(given()
                    .pathParam("personId", personId)
                .when()
                    .get("/persons/{personId}/passports")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(Passport[].class)
                    .clone());
    }

    Passport getForPassportResponseByPassportNumber(String personId, String passportNumber) {
        return given()
                    .pathParam("personId", personId)
                    .pathParam("passportNumber", passportNumber)
                .when()
                    .get("/persons/{personId}/passports/{passportNumber}")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(Passport.class);
    }

    Set<Passport> getForPassportResponseByPassportState(String personId, String passportState) {
        return Set.of(given()
                    .pathParam("personId", personId)
                    .queryParam("state", passportState)
                .when()
                    .get("/persons/{personId}/passports")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(Passport[].class)
                    .clone());
    }

    Set<Passport> getForPassportResponseByMaxAndMinGivenDates(String personId, String minGivenDate, String maxGivenDate) {
        return Set.of(given()
                    .pathParam("personId", personId)
                    .queryParam("minGivenDate", minGivenDate)
                    .queryParam("maxGivenDate", maxGivenDate)
                .when()
                    .get("/persons/{personId}/passports")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(Passport[].class)
                    .clone());
    }

    ErrorMessage getForNotFoundByPassportNumber(String personId, String nonExistentPassportNumber) {
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

    ErrorMessage postForNotFoundByPassportNumber(String nonexistentPersonId, Passport passport) {
        return given()
                    .body(passport)
                    .pathParam("personId", nonexistentPersonId)
                .when()
                    .post("/persons/{personId}/passports/")
                .then()
                    .statusCode(404)
                    .extract()
                    .as(ErrorMessage.class);
    }

    ErrorMessage postForConflictPassport(String personId, Passport existingPassport) {
        return given()
                    .body(existingPassport)
                    .pathParam("personId", personId)
                .when()
                    .post("/persons/{personId}/passports/")
                .then()
                    .statusCode(409)
                    .extract()
                    .as(ErrorMessage.class);
    }

    Passport losePassportForPassportResponse(String personId, Passport passport) {
        return given()
                    .body(passport)
                    .pathParam("personId", personId)
                    .pathParam("passportNumber", passport.getNumber())
                .when()
                    .post("/persons/{personId}/passports/{passportNumber}/loss")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(Passport.class);
    }
}
