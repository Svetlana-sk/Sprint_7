import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.s7.client.CourierClient;
import ru.praktikum.s7.dto.CourierCreateRequest;
import ru.praktikum.s7.dto.CourierLoginRequest;

import static io.restassured.RestAssured.given;
import static ru.praktikum.s7.config.RestConfig.BASE_URI;

public class CreateCourierAndLoginTest {

    private CourierClient courierClient;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Step("Шаг 1: Создаем и авторизуем курьера")
    @Test
    public void createCourierAndLogin() {
        String login = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomNumeric(4);

        // Шаг 1: Создаем курьера
        Integer courierId = createCourier(login, password);

        // Шаг 2: Авторизуем курьера
        loginCourier(login, password);

        // Шаг 3: Удаляем курьера по его ID
        deleteCourier(courierId);
    }

    @Step("Шаг 1: Создаем курьера")
    private Integer createCourier(String login, String password) {
        CourierCreateRequest courierCreateRequest = new CourierCreateRequest();
        courierCreateRequest.setLogin(login);
        courierCreateRequest.setPassword(password);
        courierCreateRequest.setFirstName("saske");

        courierClient.create(courierCreateRequest);

        return loginCourier(login, password);
    }

    @Step("Шаг 2: Авторизуем курьера")
    private Integer loginCourier(String login, String password) {
        CourierLoginRequest courierLoginRequest = new CourierLoginRequest();
        courierLoginRequest.setLogin(login);
        courierLoginRequest.setPassword(password);

        // Логин и получение ID курьера
        return courierClient.login(courierLoginRequest)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .path("id");
    }

    @Step("Шаг 3: Удаляем курьера по его ID")
    private void deleteCourier(Integer courierId) {
        given()
                .log().all()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .when()
                .delete("/courier/{id}", courierId)
                .then()
                .log().all()
                .statusCode(200);
    }
}