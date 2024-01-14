package ru.praktikum.s7.client;

import io.restassured.response.Response;
import ru.praktikum.s7.dto.CourierCreateRequest;
import ru.praktikum.s7.dto.CourierLoginRequest;

public class CourierClient extends RestClient {

    public Response create (CourierCreateRequest courierCreateRequest){
        return getDefaultRequestSpecification()
                .body(courierCreateRequest)
                .when()
                .post("/courier");
    }

    public Response login (CourierLoginRequest courierLoginRequest){
        return getDefaultRequestSpecification()
                .body(courierLoginRequest)
                .when()
                .post("/courier/login");
    }

    public Response delete (int id){
        return getDefaultRequestSpecification()
                .delete("/courier/"+id);
    }
}