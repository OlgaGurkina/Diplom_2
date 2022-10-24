package ru.yandex.praktikum;

import api.client.ApiClient;
import api.client.OrderApi;
import api.client.UserApi;
import api.model.Order;
import api.model.User;
import api.util.RandomParamsForUser;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class TestCreateOrder {
    User user;
    UserApi userApi;
    Response response;

    @Before
    public void setUp() {
        user = new User(new RandomParamsForUser());
        userApi = new UserApi();
        response = userApi.createUser(user);
    }

    @Test
    @DisplayName("create order with correct info")
    @Description("create order with authToken and correct ingredients")
    public void checkOrderCreation() {
        Order order = new Order(ApiClient.testIngredients);
        OrderApi orderApi = new OrderApi();
        orderApi.createOrder(order, userApi.getUserAccessToken(response)).then()
                .statusCode(200);
    }

    @Test
    @DisplayName("create order with no Ingredients")
    @Description("create order with authToken and NO ingredients")
    public void checkOrderCreationWithNoIngredients() {
        OrderApi orderApi = new OrderApi();
        Order order = new Order();
        Response responseOrder = orderApi.createOrder(order, userApi.getUserAccessToken(response));
        responseOrder.then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("create order with non-existing Ingredients")
    @Description("create order with authToken and Non-existing ingredients")
    public void checkOrderCreationWithNonExistingIngredients() {
        List<String> testIngredients = Arrays.asList("123f", "456");
        OrderApi orderApi = new OrderApi();
        Order order = new Order(testIngredients);
        Response responseOrder = orderApi.createOrder(order, userApi.getUserAccessToken(response));
        responseOrder.then()
                .statusCode(500);
    }

    @Test
    @DisplayName("create order with no Auth")
    @Description("create order with no authToken ")
    // тест падает, ожидание что произойдет редирект, но приходит код 200
    public void checkOrderCreationWithNoAuth() {
        Order order = new Order(ApiClient.testIngredients);
        OrderApi orderApi = new OrderApi();
        Response responseOrder = orderApi.createOrder(order, null);
        responseOrder.then().statusCode(307);
        String responseHeaderLocation = responseOrder.header("Location");
        Assert.assertEquals(responseHeaderLocation, "https://stellarburgers.nomoreparties.site/login");
    }

    @After
    public void clearTestData() {
        userApi.deleteUser(userApi.getUserAccessToken(response)).then().statusCode(202);
    }
}
