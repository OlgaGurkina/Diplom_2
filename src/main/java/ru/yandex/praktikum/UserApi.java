package ru.yandex.praktikum;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class UserApi {

    public Response createUser(User user) {

          Response response =  RestAssured.with()
                    .header("Content-Type","application/json")
                    .baseUri(ApiClient.BASE_URL)
                    .body(user)
                    .post("/api/auth/register");
          return response;
    }
    public String getUserAccessToken(Response response){
        String accessToken = response.then()
                .extract().body().path("accessToken").toString();
        return accessToken;
    }
    public String getUserRefreshToken(Response response){
        String refreshToken = response.then()
                .extract().body().path("refreshToken").toString();
        return refreshToken;
    }
    public Response deleteUser(String accessToken){
        Response response = RestAssured.with()
                .header("Content-Type","application/json")
                .header("Authorization", accessToken)
                .baseUri(ApiClient.BASE_URL)
                .delete("/api/auth/user");
        return response;
    }

    public void userLogout(String refreshToken){
        LogOut logout = new LogOut(refreshToken);
        RestAssured.with()
                .header("Content-Type","application/json")
                .baseUri(ApiClient.BASE_URL)
                .body(logout)
                .post("/api/auth/logout")
                .then()
                .statusCode(200);
    }
    public Response userLogin(String email, String password){
        LogIn login = new LogIn(email, password);
        Response responce = RestAssured.with()
                .header("Content-Type","application/json")
                .baseUri(ApiClient.BASE_URL)
                .body(login)
                .post("/api/auth/login");
        return responce;
}
    public Response userUpdate(String newEmail, String newName, String accessToken){
        UserForUserUpd userForUserUpd = new UserForUserUpd(newEmail, newName);
        Response response;
        if (accessToken != null) {
            response = RestAssured.with()
                    .header("Authorization", accessToken)
                    .header("Content-Type","application/json")
                    .baseUri(ApiClient.BASE_URL)
                    .body(userForUserUpd)
                    .patch("/api/auth/user");
        } else {
            response = RestAssured.with()
                    .header("Content-Type","application/json")
                    .baseUri(ApiClient.BASE_URL)
                    .body(userForUserUpd)
                    .patch("/api/auth/user");
        }
        return response;
    }

}
