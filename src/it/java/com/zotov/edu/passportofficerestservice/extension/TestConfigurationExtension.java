package com.zotov.edu.passportofficerestservice.extension;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class TestConfigurationExtension implements BeforeEachCallback, BeforeAllCallback {

    @Override
    public void beforeEach(ExtensionContext extensionContext){
        int port = SpringExtension.getApplicationContext(extensionContext)
                .getBean(Environment.class).getProperty("local.server.port", Integer.class, 8080);
        RestAssured.requestSpecification.port(port);
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        RestAssured.responseSpecification = new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .build();
    }
}
