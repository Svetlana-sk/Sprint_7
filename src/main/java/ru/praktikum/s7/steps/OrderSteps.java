package ru.praktikum.s7.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.praktikum.s7.client.OrderClient;
import ru.praktikum.s7.dto.CreateOrderRequest;

import java.util.List;

public class OrderSteps {
    private final OrderClient orderClient;
    public OrderSteps(OrderClient orderClient) {
        this.orderClient = orderClient;
    }
    @Step("Создание заказа")
    public ValidatableResponse createOrder (String firstName, String lastName, String address, String metroStation, String phone, String rentTime, String deliveryDate, String comment, List<String> color){
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setFirstName(firstName);
        createOrderRequest.setLastName(lastName);
        createOrderRequest.setAddress(address);
        createOrderRequest.setMetroStation(metroStation);
        createOrderRequest.setPhone(phone);
        createOrderRequest.setRentTime(rentTime);
        createOrderRequest.setDeliveryDate(deliveryDate);
        createOrderRequest.setComment(comment);
        createOrderRequest.setColor(color);
        return orderClient.createdOrder(createOrderRequest).then();

    }

    @Step("Получить список заказов")
    public  ValidatableResponse getOrder(){
        return orderClient.getOrders().then();
    }

}