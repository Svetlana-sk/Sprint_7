package ru.praktikum.s7.createCourier;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static ru.praktikum.s7.config.RestConfig.BASE_URI;

//Проверяем, что если одного из полей нет, запрос возвращает ошибку

public class RequiredFieldsTest {

    @Step("Шаг 1: Попытка создать курьера без логина")
    @Test
    public void createCourierWithoutLogin() {
        String password = RandomStringUtils.randomNumeric(4);
        String firstName = RandomStringUtils.randomAlphabetic(5);

        String responseWithoutLogin = given()
                .log().all()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"password\": \"" + password + "\",\n" +
                        "    \"firstName\": \"" + firstName + "\"\n" +
                        "}")
                .when()
                .post("/courier")
                .then()
                .log().all()
                .statusCode(400)
                .extract()
                .path("message");

        Assert.assertEquals("Недостаточно данных для создания учетной записи", responseWithoutLogin);
    }

    @Step("Шаг 2: Попытка создать курьера без пароля")
    @Test
    public void createCourierWithoutPassword() {
        String login = RandomStringUtils.randomAlphabetic(10);
        String firstName = RandomStringUtils.randomAlphabetic(5);

        String responseWithoutPassword = given()
                .log().all()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"login\": \"" + login + "\",\n" +
                        "    \"firstName\": \"" + firstName + "\"\n" +
                        "}")
                .when()
                .post("/courier")
                .then()
                .log().all()
                .statusCode(400)
                .extract()
                .path("message");

        Assert.assertEquals("Недостаточно данных для создания учетной записи", responseWithoutPassword);
    }
}