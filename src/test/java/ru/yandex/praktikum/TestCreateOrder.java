package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class TestCreateOrder {


    @Test
    @DisplayName("create order with correct info")
    @Description("create order with authToken and correct ingredients")
    public void checkOrderCreation(){
        User user = new User(new RandomParamsForUser());
        UserApi userApi = new UserApi();
        Response response = userApi.createUser(user);
        Order order = new Order(ApiClient.testIngredients);
        OrderApi orderApi = new OrderApi();
        orderApi.createOrder(order, userApi.getUserAccessToken(response)).then()
                .statusCode(200);

        userApi.deleteUser(userApi.getUserAccessToken(response)).then().statusCode(202);
    }
    @Test
    @DisplayName("create order with no Ingredients")
    @Description("create order with authToken and NO ingredients")
    public void checkOrderCreationWithNoIngredients(){
        User user = new User(new RandomParamsForUser());
        UserApi userApi = new UserApi();
        OrderApi orderApi = new OrderApi();
        Order order = new Order();
        Response response = userApi.createUser(user);

        Response responseOrder = orderApi.createOrder(order,userApi.getUserAccessToken(response));
        responseOrder.then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Ingredient ids must be provided"));

        userApi.deleteUser(userApi.getUserAccessToken(response)).then().statusCode(202);
    }

    @Test
    @DisplayName("create order with non-existing Ingredients")
    @Description("create order with authToken and Non-existing ingredients")
    public void checkOrderCreationWithNonExistingIngredients(){
        User user = new User(new RandomParamsForUser());
        UserApi userApi = new UserApi();
        OrderApi orderApi = new OrderApi();
        List<String> testIngredients = Arrays.asList("123f", "456");
        Order order = new Order(testIngredients);
        Response response = userApi.createUser(user);

        Response responseOrder = orderApi.createOrder(order,userApi.getUserAccessToken(response));
        responseOrder.then()
                .statusCode(500);

        userApi.deleteUser(userApi.getUserAccessToken(response)).then().statusCode(202);
    }

    @Test
    @DisplayName("create order with no Auth")
    @Description("create order with no authToken ")
    // тест падает, ожидание что произойдет редирект, но приходит код 200
    public void checkOrderCreationWithNoAuth(){
        Order order = new Order(ApiClient.testIngredients);
        OrderApi orderApi = new OrderApi();
        Response responseOrder=  orderApi.createOrder(order, null);
        responseOrder.then().statusCode(307);
        String responseHeaderLocation = responseOrder.header("Location");
        Assert.assertEquals(responseHeaderLocation, "https://stellarburgers.nomoreparties.site/login");
    }

}
