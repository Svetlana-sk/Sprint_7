package ru.praktikum.s7.client;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static ru.praktikum.s7.config.RestConfig.BASE_URI;

public abstract class RestClient {
    protected RequestSpecification getDefaultRequestSpecification(){
        return given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON);
    }
}
