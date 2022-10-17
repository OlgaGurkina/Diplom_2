package ru.yandex.praktikum;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class OrderApi {

    public Response createOrder(Order order, String accessToken){
        Response response;
        if (accessToken != null) {
            response = RestAssured.with()
                    .header("Authorization", accessToken)
                    .header("Content-Type","application/json")
                    .baseUri(ApiClient.BASE_URL)
                    .body(order)
                    .post("/api/orders");
        } else {
            response = RestAssured.with()
                    .header("Content-Type","application/json")
                    .baseUri(ApiClient.BASE_URL)
                    .body(order)
                    .post("/api/orders");
        }
        return response;
    }


}
