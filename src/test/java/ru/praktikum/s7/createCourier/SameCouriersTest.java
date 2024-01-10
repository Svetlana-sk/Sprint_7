package ru.praktikum.s7.createCourier;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static ru.praktikum.s7.config.RestConfig.BASE_URI;

//Проверяем, что если создать курьера с существующим логином, вернется ошибка

public class SameCouriersTest {

    @Step("Шаг 1: Создание курьера с уникальным логином")
    @Test
    public void createUniqueCourier() {
        given()
                .log().all()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"login\": \"ni88888nja\",\n" +
                        "    \"password\": \"1234\",\n" +
                        "    \"firstName\": \"saske\"\n" +
                        "}")
                .when()
                .post("/courier")
                .then()
                .log().all();
    }

    @Step("Шаг 2: Попытка создания курьера с существующим логином")
    @Test
    public void createCourierWithExistingLogin() {
        int statusCode  = given()
                .log().all()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"login\": \"ni88888nja\",\n" +
                        "    \"password\": \"12345\",\n" +
                        "    \"firstName\": \"saske\"\n" +
                        "}")
                .when()
                .post("/courier")
                .then()
                .log().all()
                .body("message", Matchers.containsString("Этот логин уже используется"))
                .extract()
                .statusCode();

        Assert.assertEquals(409, statusCode);
    }
}