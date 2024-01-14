package ru.praktikum.s7.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.praktikum.s7.client.CourierClient;
import ru.praktikum.s7.dto.CourierCreateRequest;
import ru.praktikum.s7.dto.CourierLoginRequest;

public class CourierSteps {
    private final CourierClient courierClient;


    public CourierSteps(CourierClient courierClient) {
        this.courierClient = courierClient;

    }

    @Step("Создание курьера")
    public ValidatableResponse create(String login, String password, String firstName) {
        CourierCreateRequest courierCreateRequest = new CourierCreateRequest();
        courierCreateRequest.setLogin(login);
        courierCreateRequest.setPassword(password);
        courierCreateRequest.setFirstName(firstName);
        return courierClient.create(courierCreateRequest).then();

    }

    @Step("Авторизация курьера")
    public ValidatableResponse login(String login, String password) {
        CourierLoginRequest courierLoginRequest = new CourierLoginRequest();
        courierLoginRequest.setLogin(login);
        courierLoginRequest.setPassword(password);
        return courierClient.login(courierLoginRequest).then();

    }

    @Step("Удаление курьера")
    public void deleteTestCourier(String login, String password) {
        CourierLoginRequest courierLoginRequest = new CourierLoginRequest();
        courierLoginRequest.setLogin(login);
        courierLoginRequest.setPassword(password);
        int id = courierClient.login(courierLoginRequest).then().extract().body().path("id");
        courierClient.delete(id);
    }


}