package com.zotov.edu.passportofficerestservice.util;

import com.zotov.edu.passportofficerestservice.model.PassportPutRequest;
import com.zotov.edu.passportofficerestservice.model.PassportRequest;
import io.restassured.response.ValidatableResponse;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static io.restassured.RestAssured.*;

@UtilityClass
public class PassportRequests {
    public ValidatableResponse postPassport(String personId, PassportRequest passportRequest) {
        return given()
                .body(passportRequest)
                .pathParam("personId", personId)
                .when()
                .post("/persons/{personId}/passports")
                .then();
    }

    public ValidatableResponse getPassport(String personId, String passportNumber) {
        return given()
                .pathParam("personId", personId)
                .pathParam("passportNumber", passportNumber)
                .when()
                .get("/persons/{personId}/passports/{passportNumber}")
                .then();
    }

    public ValidatableResponse getPassports(String personId, String passportState) {
        Map<String, String> queryParams = new HashMap<>();
        if (Objects.nonNull(passportState)) {
            queryParams.put("state", passportState);
        }

        return given()
                .pathParam("personId", personId)
                .queryParams(queryParams)
                .when()
                .get("/persons/{personId}/passports")
                .then();
    }

    public static ValidatableResponse getPassports(String personId, String minGivenDate, String maxGivenDate) {
        Map<String, String> queryParams = new HashMap<>();
        if (Objects.nonNull(minGivenDate)) {
            queryParams.put("minGivenDate", minGivenDate);
        }
        if (Objects.nonNull(maxGivenDate)) {
            queryParams.put("maxGivenDate", maxGivenDate);
        }

        return given()
                .pathParam("personId", personId)
                .queryParams(queryParams)
                .when()
                .get("/persons/{personId}/passports")
                .then();
    }

    public ValidatableResponse losePassport(String personId, String passportNumber) {
        return given()
                .pathParam("personId", personId)
                .pathParam("passportNumber", passportNumber)
                .when()
                .post("/persons/{personId}/passports/{passportNumber}/loss")
                .then();
    }


    public ValidatableResponse putPassport(String personId, String passportNumber, PassportPutRequest passportRequest) {
        return given()
                .body(passportRequest)
                .pathParam("personId", personId)
                .pathParam("passportNumber", passportNumber)
                .when()
                .put("/persons/{personId}/passports/{passportNumber}")
                .then();
    }

    public ValidatableResponse deletePassport(String ownerId, String passportNumber) {
        return given()
                .pathParam("personId", ownerId)
                .pathParam("passportNumber", passportNumber)
                .when()
                .delete("/persons/{personId}/passports/{passportNumber}")
                .then();
    }

}
