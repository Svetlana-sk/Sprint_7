package ru.praktikum.s7.createCourier;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.s7.client.CourierClient;
import ru.praktikum.s7.dto.CourierCreateRequest;
import ru.praktikum.s7.dto.CourierLoginRequest;

import static io.restassured.RestAssured.given;
import static ru.praktikum.s7.config.RestConfig.BASE_URI;

public class CourierTest {
    private CourierClient courierClient;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Step("Шаг 1: Создание курьера с логином, паролем и именем")
    @Test
    public void createAndDeleteCourier() {
        // Генерируем случайные данные
        String login = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomNumeric(4);
        String firstName = RandomStringUtils.randomAlphabetic(5);

        // Создаем запрос на создание курьера
        CourierCreateRequest request = new CourierCreateRequest();
        request.setLogin(login);
        request.setPassword(password);
        request.setFirstName(firstName);

        // Создаем курьера
        courierClient.create(request);

        // Создаем запрос на авторизацию, чтобы получить id
        CourierLoginRequest loginRequest = new CourierLoginRequest();
        loginRequest.setLogin(login);
        loginRequest.setPassword(password);

        // Авторизуем курьера и получаем его id
        String courierId = courierClient.loginAndReturnId(loginRequest);

        // Проверяем, что курьер успешно авторизован и получено id
        Assert.assertNotNull(courierId);

        // Шаг 3: Удаление курьера по его ID
        deleteCourier(courierId);
    }

    @Step("Шаг 2: Удаление курьера по его ID")
    private void deleteCourier(String courierId) {
        given()
                .log().all()
                .baseUri(BASE_URI)
                .contentType("application/json")
                .when()
                .delete("/courier/{id}", courierId)
                .then()
                .log().all()
                .statusCode(200);
    }
}