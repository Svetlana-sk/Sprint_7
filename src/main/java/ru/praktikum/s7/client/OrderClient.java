package ru.praktikum.s7.client;

import io.restassured.response.Response;
import ru.praktikum.s7.dto.CreateOrderRequest;

public class OrderClient extends RestClient {
    public Response createdOrder (CreateOrderRequest createOrderRequest) {
        return getDefaultRequestSpecification()
                .body(createOrderRequest)
                .when()
                .post("/orders");
    }

    public Response getOrders (){
        return getDefaultRequestSpecification()
                .when()
                .get("/orders");
    }
}