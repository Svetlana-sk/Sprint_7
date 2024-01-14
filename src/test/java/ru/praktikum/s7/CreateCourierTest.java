package ru.praktikum.s7;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.containsString;
import ru.praktikum.s7.client.CourierClient;
import ru.praktikum.s7.steps.CourierSteps;
import static org.hamcrest.core.Is.is;

public class CreateCourierTest {
    private CourierSteps courierSteps;
    // Генерируем случайные данные
    String login = RandomStringUtils.randomAlphabetic(10);
    String password = RandomStringUtils.randomNumeric(4);
    String firstName = RandomStringUtils.randomAlphabetic(5);

    @Before
    public void setUp() {

        courierSteps = new CourierSteps(new CourierClient());
    }

    @Test
    @DisplayName("Создание курьера с уникальным логином")
    public void CourierCreateRequest() {
        courierSteps
                .create(login, password, firstName)
                .statusCode(201)
                .body("ok", is(true));
    }


    @Test
    @DisplayName("Попытка создания двух одинаковых курьеров")
    public void sameCourier() {
        courierSteps
                .create(login, password, firstName);
        courierSteps
                .create(login, password, firstName)
                .statusCode(409)
                .body("message", containsString("Этот логин уже используется"));
    }


    @Test
    @DisplayName("Попытка создать курьера без логина")
    public void createCourierWithoutLogin() {
        courierSteps
                .create(login, password, firstName);

        String login = "";
        courierSteps
                .create(login, password, firstName)
                .statusCode(400)
                .body("message", containsString("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Попытка создать курьера без пароля")
    public void createCourierWithoutPassword() {
        courierSteps
                .create(login, password, firstName);

        String password = "";
        courierSteps
                .create(login, password, firstName)
                .statusCode(400)
                .body("message", containsString("Недостаточно данных для создания учетной записи"));

    }

    @After
    public void deleteall() {
        courierSteps.deleteTestCourier(login, password);
    }

}