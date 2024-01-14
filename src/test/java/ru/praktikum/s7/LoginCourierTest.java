package ru.praktikum.s7;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.containsString;
import ru.praktikum.s7.client.CourierClient;
import ru.praktikum.s7.steps.CourierSteps;

import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierTest {
    private CourierSteps courierSteps;
    String login = RandomStringUtils.randomAlphabetic(10);
    String password = RandomStringUtils.randomNumeric(4);
    String firstName = RandomStringUtils.randomAlphabetic(5);


    @Before
    public void setUp() {

        courierSteps = new CourierSteps(new CourierClient());

    }

    @Test
    @DisplayName("Авторизуем курьера")
    public void courierLogin() {
        courierSteps
                .create(login, password, firstName);
        courierSteps
                .login(login, password)
                .statusCode(200)
                .body("id", notNullValue());
    }


    @Test
    @DisplayName("Проверяем авторизацию с неверным логином")
    public void checkInvalidLoginWithIncorrectLogin() {
        String randomLogin = RandomStringUtils.randomAlphabetic(10);
        courierSteps
                .create(login, password, firstName);
        courierSteps
                .login(randomLogin, password)
                .statusCode(404)
                .body("message", containsString("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Проверяем авторизацию с неверным паролем")
    public void checkInvalidLoginWithIncorrectPassword() {
        String randomPassword = RandomStringUtils.randomAlphabetic(10);
        courierSteps
                .create(login, password, firstName);
        courierSteps
                .login(login, randomPassword)
                .statusCode(404)
                .body("message", containsString("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Проверяем авторизацию с отсутствующим логином")
    public void checkLoginWithMissingLogin() {
        courierSteps
                .create(login, password, firstName);

        String login = "";
        courierSteps
                .login(login, password)
                .log().all()
                .statusCode(400)
                .body("message", containsString("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Проверяем авторизацию с отсутствующим паролем")
    public void checkLoginWithMissingPassword() {
        courierSteps
                .create(login, password, firstName);


        String password = "";
        courierSteps
                .login(login, password)
                .statusCode(400)
                .body("message", containsString("Недостаточно данных для входа"));
    }


    @After
    public void deleteall() {
        courierSteps.deleteTestCourier(login, password);
    }
}