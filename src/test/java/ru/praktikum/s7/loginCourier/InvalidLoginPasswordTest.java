import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.s7.client.CourierClient;
import ru.praktikum.s7.dto.CourierCreateRequest;

import static io.restassured.RestAssured.given;
import static ru.praktikum.s7.config.RestConfig.BASE_URI;

// Проверяем, что система вернет ошибку, если неправильно указать логин или пароль
public class InvalidLoginPasswordTest {

    private CourierClient courierClient;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Step("Шаг 1: Создаем курьера")
    @Test
    public void loginCourierWithRequiredFields() {
        String login = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.random(4);

        // Шаг 1: Создаем курьера
        createCourier(login, password);

        // Шаг 2: Проверяем авторизацию с неверным паролем
        checkInvalidLoginWithIncorrectPassword(login);

        // Шаг 3: Проверяем авторизацию с неверным логином
        checkInvalidLoginWithIncorrectLogin(password);
    }

    @Step("Шаг 1: Создаем курьера")
    private void createCourier(String login, String password) {
        CourierCreateRequest request = new CourierCreateRequest();
        request.setLogin(login);
        request.setPassword(password);
        request.setFirstName("saske");

        courierClient.create(request);
    }

    @Step("Шаг 2: Проверяем авторизацию с неверным паролем")
    private void checkInvalidLoginWithIncorrectPassword(String login) {
        given()
                .log().all()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"login\": \"" + login + "\",\n" +
                        "    \"password\": \"" + RandomStringUtils.random(8) + "\"\n" +
                        "}")
                .when()
                .post("/courier/login")
                .then()
                .log().all()
                .statusCode(404)
                .body("message", Matchers.equalTo("Учетная запись не найдена"));
    }

    @Step("Шаг 3: Проверяем авторизацию с неверным логином")
    private void checkInvalidLoginWithIncorrectLogin(String password) {
        given()
                .log().all()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"login\": \"" + RandomStringUtils.randomAlphabetic(10) + "\",\n" +
                        "    \"password\": \"" + password + "\"\n" +
                        "}")
                .when()
                .post("/courier/login")
                .then()
                .log().all()
                .statusCode(404)
                .body("message", Matchers.equalTo("Учетная запись не найдена"));
    }
}