package api.client;

import api.model.LogIn;
import api.model.LogOut;
import api.model.User;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import api.model.UserForUserUpd;

public class UserApi {
    @Step("create user")
    public Response createUser(User user) {
        Response response = RestAssured.with()
                .header("Content-Type", "application/json")
                .baseUri(ApiClient.BASE_URL)
                .body(user)
                .post("/api/auth/register");
        return response;
    }

    @Step("get user's access token")
    public String getUserAccessToken(Response response) {
        String accessToken = response.then()
                .extract().body().path("accessToken").toString();
        return accessToken;
    }

    @Step("get user's refresh token")
    public String getUserRefreshToken(Response response) {
        String refreshToken = response.then()
                .extract().body().path("refreshToken").toString();
        return refreshToken;
    }

    @Step("delete user, who was used in tests")
    public Response deleteUser(String accessToken) {
        Response response = RestAssured.with()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .baseUri(ApiClient.BASE_URL)
                .delete("/api/auth/user");
        return response;
    }

    @Step("perform log out for user")
    public void userLogout(String refreshToken) {
        LogOut logout = new LogOut(refreshToken);
        RestAssured.with()
                .header("Content-Type", "application/json")
                .baseUri(ApiClient.BASE_URL)
                .body(logout)
                .post("/api/auth/logout")
                .then()
                .statusCode(200);
    }

    @Step("perform log in fro user")
    public Response userLogin(String email, String password) {
        LogIn login = new LogIn(email, password);
        Response responce = RestAssured.with()
                .header("Content-Type", "application/json")
                .baseUri(ApiClient.BASE_URL)
                .body(login)
                .post("/api/auth/login");
        return responce;
    }

    @Step("update user info with newEmail - {0}, newName - {1}")
    public Response userUpdate(String newEmail, String newName, String accessToken) {
        UserForUserUpd userForUserUpd = new UserForUserUpd(newEmail, newName);
        Response response;
        if (accessToken != null) {
            response = RestAssured.with()
                    .header("Authorization", accessToken)
                    .header("Content-Type", "application/json")
                    .baseUri(ApiClient.BASE_URL)
                    .body(userForUserUpd)
                    .patch("/api/auth/user");
        } else {
            response = RestAssured.with()
                    .header("Content-Type", "application/json")
                    .baseUri(ApiClient.BASE_URL)
                    .body(userForUserUpd)
                    .patch("/api/auth/user");
        }
        return response;
    }
}
