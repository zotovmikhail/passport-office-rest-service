package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.model.PassportSpecification;
import com.zotov.edu.passportofficerestservice.repository.PassportsRepository;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

import static com.zotov.edu.passportofficerestservice.util.DataConverter.convertToPassportEntities;
import static com.zotov.edu.passportofficerestservice.util.DataConverter.convertToPassportEntity;
import static io.restassured.RestAssured.given;

public abstract class PassportsBaseTest extends PersonBaseTest {

    @Autowired
    PassportsRepository passportsRepository;

    Arguments generatePassportsData(PassportSpecification... passportSpecifications) {
        String createdPersonId = generatePersonData().getId();
        Set<PassportSpecification> createdPassportSpecifications = new HashSet<>();
        for (PassportSpecification passportSpecification : passportSpecifications) {
            createdPassportSpecifications.add(passportSpecification.withOwnerId(createdPersonId));
        }
        passportsRepository.saveAll(convertToPassportEntities(createdPassportSpecifications));
        return Arguments.of(createdPassportSpecifications, createdPersonId);
    }

    PassportSpecification generatePassportData(PassportSpecification passportSpecifications) {
        String createdPersonId = generatePersonData().getId();
        PassportSpecification createdPassportSpecification = passportSpecifications.withOwnerId(createdPersonId);
        passportsRepository.save(convertToPassportEntity(createdPassportSpecification));
        return createdPassportSpecification;
    }

    void verifyPassportNotFoundErrorMessages(ErrorMessage errorMessage, String nonExistentPassportNumber) {
        verifyNotFoundErrorMessages(errorMessage, nonExistentPassportNumber, "Passport");
    }

    PassportSpecification postForPassportResponse(String personId, PassportSpecification passportSpecification) {
        return given()
                    .body(passportSpecification)
                    .pathParam("personId", personId)
                .when()
                 .post("/persons/{personId}/passports")
                .then()
                    .statusCode(201)
                    .extract()
                    .as(PassportSpecification.class);
    }

    ErrorMessage postPassportForBadRequest(String personId, PassportSpecification passportSpecification) {
        return given()
                    .body(passportSpecification)
                    .pathParam("personId", personId)
                .when()
                    .post("/persons/{personId}/passports")
                .then()
                    .statusCode(400)
                    .extract()
                    .as(ErrorMessage.class);
    }

    Set<PassportSpecification> getForPassportResponse(String personId) {
        return Set.of(given()
                    .pathParam("personId", personId)
                .when()
                    .get("/persons/{personId}/passports")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(PassportSpecification[].class)
                    .clone());
    }

    PassportSpecification getForPassportResponseByPassportNumber(String personId, String passportNumber) {
        return given()
                    .pathParam("personId", personId)
                    .pathParam("passportNumber", passportNumber)
                .when()
                    .get("/persons/{personId}/passports/{passportNumber}")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(PassportSpecification.class);
    }

    Set<PassportSpecification> getForPassportResponseByPassportState(String personId, String passportState) {
        return Set.of(given()
                    .pathParam("personId", personId)
                    .queryParam("state", passportState)
                .when()
                    .get("/persons/{personId}/passports")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(PassportSpecification[].class)
                    .clone());
    }

    Set<PassportSpecification> getForPassportResponseByMaxAndMinGivenDates(String personId, String minGivenDate, String maxGivenDate) {
        return Set.of(given()
                    .pathParam("personId", personId)
                    .queryParam("minGivenDate", minGivenDate)
                    .queryParam("maxGivenDate", maxGivenDate)
                .when()
                    .get("/persons/{personId}/passports")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(PassportSpecification[].class)
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

    ErrorMessage postForNotFoundByPassportNumber(String nonexistentPersonId, PassportSpecification passportSpecification) {
        return given()
                    .body(passportSpecification)
                    .pathParam("personId", nonexistentPersonId)
                .when()
                    .post("/persons/{personId}/passports/")
                .then()
                    .statusCode(404)
                    .extract()
                    .as(ErrorMessage.class);
    }

    ErrorMessage postForConflictPassport(String personId, PassportSpecification existingPassportSpecification) {
        return given()
                    .body(existingPassportSpecification)
                    .pathParam("personId", personId)
                .when()
                    .post("/persons/{personId}/passports/")
                .then()
                    .statusCode(409)
                    .extract()
                    .as(ErrorMessage.class);
    }

    PassportSpecification losePassportForPassportResponse(String personId, String passportNumber) {
        return given()
                    .pathParam("personId", personId)
                    .pathParam("passportNumber", passportNumber)
                .when()
                    .post("/persons/{personId}/passports/{passportNumber}/loss")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(PassportSpecification.class);
    }


    PassportSpecification putForPassportResponse(String personId, PassportSpecification passportSpecification) {
        return given()
                    .body(passportSpecification)
                    .pathParam("personId", personId)
                    .pathParam("passportNumber", passportSpecification.getNumber())
                .when()
                    .put("/persons/{personId}/passports/{passportNumber}")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(PassportSpecification.class);
    }

    ErrorMessage putPassportForBadRequest(String personId, PassportSpecification passportSpecification) {
        return given()
                    .body(passportSpecification)
                    .pathParam("personId", personId)
                    .pathParam("passportNumber", passportSpecification.getNumber())
                .when()
                    .put("/persons/{personId}/passports/{passportNumber}")
                .then()
                    .statusCode(400)
                    .extract()
                    .as(ErrorMessage.class);
    }

    ErrorMessage putForNotFoundByPassportNumber(String nonexistentPersonId, PassportSpecification passportSpecification) {
        return given()
                    .body(passportSpecification)
                    .pathParam("personId", nonexistentPersonId)
                    .pathParam("passportNumber", passportSpecification.getNumber())
                .when()
                    .put("/persons/{personId}/passports/{passportNumber}")
                .then()
                    .statusCode(404)
                    .extract()
                    .as(ErrorMessage.class);
    }


    ErrorMessage deletePassportForNotFound(String personId, String nonexistentPassportId) {
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

    ErrorMessage losePassportForNotFound(String personId, String nonexistentPassportId) {
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

    ErrorMessage losePassportForConflict(String personId, String nonexistentPassportId) {
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
