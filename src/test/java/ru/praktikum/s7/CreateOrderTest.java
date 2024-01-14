package ru.praktikum.s7;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.s7.client.OrderClient;
import ru.praktikum.s7.steps.OrderSteps;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    @Parameterized.Parameter
    public String colorTest;

    @Parameterized.Parameters

    public static Object[][] testData() {
        return new Object[][]{
                {"BLACK"},
                {"GREY"},
                {"BLACK, GREY"},
                {""}

        };
    }


    private OrderSteps orderSteps;


    @Before
    public void setUp() {
        orderSteps = new OrderSteps(new OrderClient());
    }


    @Test
    @DisplayName("Создание заказа с одним/двумя и без указания цвета")
    public void createOrder() {
        String firstName = "Naruto";
        String lastName = "Uchiha";
        String address = "Konoha, 142 apt.";
        String metroStation = "4";
        String phone = "+7 800 355 35 35";
        String rentTime = "5";
        String deliveryDate = "2020-06-06";
        String comment = "Saske, come back to Konoha";
        List<String> color = new ArrayList<String>();
        color.add(colorTest);

        orderSteps
                .createOrder(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color)
                .statusCode(201)
                .body("track", notNullValue());


    }
}