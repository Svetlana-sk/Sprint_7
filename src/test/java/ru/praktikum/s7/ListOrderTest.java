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


public class ListOrderTest {


    private OrderSteps orderSteps;


    @Before
    public void setUp() {
        orderSteps = new OrderSteps(new OrderClient());
    }


    @Test
    @DisplayName("получение списка заказов")
    public void getOrder() {
        orderSteps.getOrder()
                .statusCode(200)
                .body("orders", notNullValue());

    }
}