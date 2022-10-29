package ru.yandex.praktikum;

import api.client.ApiClient;
import api.client.OrderApi;
import api.client.UserApi;
import api.model.Order;
import api.model.User;
import api.util.RandomParamsForUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;


import static org.hamcrest.Matchers.equalTo;

public class TestGetOrdersList {
    @Test
    @DisplayName("get order list for authorized user")
    public void checkGetOrderList() {
        User user = new User(new RandomParamsForUser());
        UserApi userApi = new UserApi();
        Response response = userApi.createUser(user);
        Order order1 = new Order(ApiClient.testIngredients);
        OrderApi orderApi = new OrderApi();
        orderApi.createOrder(order1, userApi.getUserAccessToken(response)).then()
                .statusCode(200);
        Order order2 = new Order(ApiClient.testIngredients);
        orderApi.createOrder(order2, userApi.getUserAccessToken(response)).then()
                .statusCode(200);

        RestAssured.with()
                .header("Content-Type", "application/json")
                .header("Authorization", userApi.getUserAccessToken(response))
                .baseUri(ApiClient.BASE_URL)
                .get("/api/orders")
                .then()
                .statusCode(200)
                .assertThat().body("success", equalTo(true));

        userApi.deleteUser(userApi.getUserAccessToken(response)).then().statusCode(202);
    }

    @Test
    @DisplayName("get order list for NOT authorized user")
    public void checkGetOrderListNoAuth() {
        RestAssured.with()
                .header("Content-Type", "application/json")
                .baseUri(ApiClient.BASE_URL)
                .get("/api/orders")
                .then()
                .statusCode(401)
                .assertThat().body("message", equalTo("You should be authorised"));
    }
}
