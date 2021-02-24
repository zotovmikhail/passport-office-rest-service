package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.model.ErrorMessage;
import com.zotov.edu.passportofficerestservice.util.ReplaceCamelCase;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DisplayNameGeneration(ReplaceCamelCase.class)
abstract class BaseTest {

    @BeforeEach
    public void setUp() {
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        RestAssured.responseSpecification  = new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .build();
    }

    void verifyErrorMessages(ErrorMessage errorMessage, List<String> expectedMessages) {
        assertThat(errorMessage.getMessages()).isEqualTo(expectedMessages);
    }

    void verifyNullValueErrorMessages(ErrorMessage errorResponse, String field) {
        verifyErrorMessages(errorResponse, List.of(String.format("%s must not be null", field)));
    }

    void verifyEmptyValueErrorMessages(ErrorMessage errorMessage, String field) {
        verifyErrorMessages(errorMessage, List.of(String.format("%s must not be empty", field)));
    }

    void verifyInvalidDateErrorMessages(ErrorMessage errorMessage, String birthday) {
        assertThat(errorMessage.getMessages().stream()
                .findFirst()
                .orElseGet(() -> fail("The Error Messages list from the response is empty.")))
                .contains(String.format("Text '%s' could not be parsed", birthday));
    }

    void verifyInvalidCountryErrorMessages(ErrorMessage errorMessage) {
        verifyErrorMessages(errorMessage, List.of("country is not found in the list of available countries."));
    }

    void verifyNotFoundErrorMessages(ErrorMessage errorMessage, String entityId, String entityName) {
        assertThat(errorMessage.getMessages()).isEqualTo(List.of(String.format("%s with id '%s' is not found", entityName, entityId)));
    }

}
