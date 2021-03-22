package com.zotov.edu.passportofficerestservice;

import com.zotov.edu.passportofficerestservice.extension.TestConfigurationExtension;
import com.zotov.edu.passportofficerestservice.extension.TestExecutionLoggerExtension;
import com.zotov.edu.passportofficerestservice.model.PageResponse;
import com.zotov.edu.passportofficerestservice.model.PersonResponse;
import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import com.zotov.edu.passportofficerestservice.util.PassportDataHandler;
import com.zotov.edu.passportofficerestservice.util.PersonDataHandler;
import com.zotov.edu.passportofficerestservice.util.ReplaceCamelCase;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static com.zotov.edu.passportofficerestservice.util.DataConverter.*;
import static com.zotov.edu.passportofficerestservice.util.PersonRequests.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(ReplaceCamelCase.class)
@ExtendWith({TestExecutionLoggerExtension.class, TestConfigurationExtension.class})
class GetPersonsIT {

    @Autowired
    private PersonDataHandler personDataHandler;

    @Autowired
    private PassportDataHandler passportDataHandler;

    private static Stream<Arguments> getListOfPersons() {
        return Stream.of(
                Arguments.of(null, null, 100, 0, "Default page size and page number"),
                Arguments.of("100", "0", 100, 0, "Valid page size and page number"),
                Arguments.of("50", "4", 50, 4, "Page other than the first one"),
                Arguments.of("invalidPageSize", "invalidPageNumber", 100, 0, "Invalid page size and page number"),
                Arguments.of("100000000000000", "10000000000000000", 100, 0, "Too big page size and page number"),
                Arguments.of("-1", "-1", 100, 0, "Negative page size and page number"),
                Arguments.of("0", "0", 100, 0, "Zero page size and page number")
        );
    }

    @ParameterizedTest
    @MethodSource("getListOfPersons")
    void testGetPersonsAndVerifyDefaultValues(String pageSize, String pageNumber, int expectedPageSize, int expectedPageNumber, String description) {
        personDataHandler.generatePersonsData(200);

        PageResponse<PersonResponse> pageResponse =
                getPersons(pageSize, pageNumber)
                        .statusCode(200)
                        .extract()
                        .as(new TypeRef<>() {
                        });

        assertThat(pageResponse.getSize()).isEqualTo(expectedPageSize);
        assertThat(pageResponse.getNumber()).isEqualTo(expectedPageNumber);
        assertThat(pageResponse.getContent().size()).isEqualTo(expectedPageSize);
    }

    @Test
    void testGetPersonsByNonexistentPassportNumber() {
        PageResponse<PersonResponse> pageResponse =
                getPersons("NonexistentPassportNumber")
                        .statusCode(200)
                        .extract()
                        .as(new TypeRef<>() {
                        });

        assertThat(pageResponse.getSize()).isEqualTo(100);
        assertThat(pageResponse.getNumber()).isZero();
        assertThat(pageResponse.getContent().size()).isZero();
        assertThat(pageResponse.getContent()).isEqualTo(List.of());
    }

    @Test
    void testGetPersonsByPassportNumber() {
        Person person = personDataHandler.generatePersonData();
        PersonResponse expectedPersonResponse = convertToPersonResponse(person);
        Passport passport = passportDataHandler.generatePassportData(person.getId());

        PageResponse<PersonResponse> pageResponse =
                getPersons(passport.getNumber())
                        .statusCode(200)
                        .extract()
                        .as(new TypeRef<>() {
                        });

        assertThat(pageResponse.getSize()).isEqualTo(100);
        assertThat(pageResponse.getNumber()).isZero();
        assertThat(pageResponse.getContent().size()).isEqualTo(1);
        assertThat(pageResponse.getContent()).isEqualTo(List.of(expectedPersonResponse));
    }

}