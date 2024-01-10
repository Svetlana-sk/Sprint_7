import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.s7.client.CourierClient;
import ru.praktikum.s7.dto.CourierCreateRequest;
import ru.praktikum.s7.dto.CourierLoginRequest;

import java.util.Arrays;
import java.util.Collection;

import static io.restassured.RestAssured.given;
import static ru.praktikum.s7.config.RestConfig.BASE_URI;

@RunWith(Parameterized.class)
public class ListOrderTest {

    private final String customerFirstName;
    private final String customerLastName;
    private final String address;
    private final int metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;

    public ListOrderTest(String customerFirstName, String customerLastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment) {
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha"},
        });
    }

    private CourierClient courierClient;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Step("Шаг 1: Создаем и авторизуем курьера")
    @Test
    public void listOrder() {
        String login = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomNumeric(4);
        String firstName = RandomStringUtils.randomAlphabetic(5);

        // Шаг 1: Создаем курьера
        createCourier(login, password, firstName);

        // Шаг 2: Авторизуем курьера
        CourierLoginRequest courierLoginRequest = authorizeCourier(login, password);

        // Шаг 3: Создаем заказ
        createOrder();

        // Шаг 4: Получаем список заказов курьера
        getOrderList();

        // Шаг 5: Удаляем курьера по его ID
        deleteCourier(courierLoginRequest);
    }

    @Step("Шаг 1: Создаем курьера")
    private void createCourier(String login, String password, String firstName) {
        CourierCreateRequest courierCreateRequest = new CourierCreateRequest();
        courierCreateRequest.setLogin(login);
        courierCreateRequest.setPassword(password);
        courierCreateRequest.setFirstName(firstName);

        courierClient.create(courierCreateRequest);
    }

    @Step("Шаг 2: Авторизуем курьера")
    private CourierLoginRequest authorizeCourier(String login, String password) {
        CourierLoginRequest courierLoginRequest = new CourierLoginRequest();
        courierLoginRequest.setLogin(login);
        courierLoginRequest.setPassword(password);

        return courierLoginRequest;
    }

    @Step("Шаг 3: Создаем заказ")
    private void createOrder() {
        String[] colors = {"BLACK", "GREY"};
        String selectedColor = colors[(int) (Math.random() * colors.length)];

        given()
                .log().all()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"firstName\": \"" + customerFirstName + "\",\n" +
                        "    \"lastName\": \"" + customerLastName + "\",\n" +
                        "    \"address\": \"" + address + "\",\n" +
                        "    \"metroStation\": " + metroStation + ",\n" +
                        "    \"phone\": \"" + phone + "\",\n" +
                        "    \"rentTime\": " + rentTime + ",\n" +
                        "    \"deliveryDate\": \"" + deliveryDate + "\",\n" +
                        "    \"comment\": \"" + comment + "\",\n" +
                        "    \"color\": [\n" +
                        "        \"" + selectedColor + "\"\n" +
                        "    ]\n" +
                        "}")
                .when()
                .post("/orders")
                .then()
                .log().all()
                .statusCode(201)
                .body("track", Matchers.notNullValue());
    }

    @Step("Шаг 4: Получаем список заказов курьера")
    private void getOrderList() {
        given()
                .log().all()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .when()
                .get("/orders")
                .then()
                .log().all()
                .statusCode(200)
                .body("orders", Matchers.notNullValue());
    }

    @Step("Шаг 5: Удаляем курьера по его ID")
    private void deleteCourier(CourierLoginRequest courierLoginRequest) {
        Object result = courierClient.login(courierLoginRequest)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .path("id");
        Assert.assertNotNull(result);

        given()
                .log().all()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .when()
                .delete("/courier/{id}", result.toString())
                .then()
                .log().all()
                .statusCode(200);
    }
}