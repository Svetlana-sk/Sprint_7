package ru.praktikum.s7.client;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.praktikum.s7.dto.CourierCreateRequest;
import ru.praktikum.s7.dto.CourierLoginRequest;

import static io.restassured.RestAssured.given;
import static ru.praktikum.s7.config.RestConfig.BASE_URI;

public class CourierClient extends RestClient {

    public Response create(CourierCreateRequest courierCreateRequest) {
        return getDefaultRequestSpecification()
                .body(courierCreateRequest)
                .when()
                .post("/courier");
    }

    public Response login(CourierLoginRequest courierLoginRequest) {
        return getDefaultRequestSpecification()
                .body(courierLoginRequest)
                .when()
                .post("/courier/login");
    }

    public void deleteCourierById(String courierId) {
        getDefaultRequestSpecification()
                .when()
                .delete("/courier/{id}", courierId)
                .then()
                .log().all()
                .statusCode(200);
    }

    public String loginAndReturnId(CourierLoginRequest courierLoginRequest) {
        Response response = login(courierLoginRequest);

        response.then()
                .log().all()
                .statusCode(200);

        return response.jsonPath().getString("id");
    }
}