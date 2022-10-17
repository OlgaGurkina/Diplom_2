package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class TestUserLogin {

    @Test
    @DisplayName("Check User Login")
    @Description("check Login with correct params")
    public void checkUserLogin(){
       UserApi userApi = new UserApi();
       User testUser = new User(new RandomParamsForUser());
       Response user = userApi.createUser(testUser);
       user.then().statusCode(200);
       String accessToken = userApi.getUserAccessToken(user);
       String refreshToken = userApi.getUserRefreshToken(user);
       userApi.userLogout(refreshToken);

       userApi.userLogin(testUser.getEmail(), testUser.getPassword()).then()
              .statusCode(200);

       userApi.deleteUser(accessToken).then().statusCode(202);
    }

    @Test
    @DisplayName("Check User Login with no email")
    @Description("check Login with incorrect params: no email")
    public void checkUserLoginWithNoEmail(){
        UserApi userApi = new UserApi();
        User testUser = new User(new RandomParamsForUser());
        Response user = userApi.createUser(testUser);
        user.then().statusCode(200);
        String accessToken = userApi.getUserAccessToken(user);
        String refreshToken = userApi.getUserRefreshToken(user);
        userApi.userLogout(refreshToken);

        userApi.userLogin(null, testUser.getPassword()).then()
                .statusCode(401 )
                .assertThat().body("message", equalTo("email or password are incorrect"));

        userApi.deleteUser(accessToken).then().statusCode(202);
    }

    @Test
    @DisplayName("Check User Login with no password")
    @Description("check Login with incorrect params: no password")
    public void checkUserLoginWithNoPass(){
        UserApi userApi = new UserApi();
        User testUser = new User(new RandomParamsForUser());
        Response user = userApi.createUser(testUser);
        user.then().statusCode(200);
        String accessToken = userApi.getUserAccessToken(user);
        String refreshToken = userApi.getUserRefreshToken(user);
        userApi.userLogout(refreshToken);

        userApi.userLogin(testUser.getEmail(), null).then()
                .statusCode(401 )
                .assertThat().body("message", equalTo("email or password are incorrect"));

        userApi.deleteUser(accessToken).then().statusCode(202);
    }

    @Test
    @DisplayName("Check User Login with wrong Password")
    @Description("check Login with incorrect params: wrong password")
    public void checkUserLoginWithIncorrectPass(){
        UserApi userApi = new UserApi();
        User testUser = new User(new RandomParamsForUser());
        Response user = userApi.createUser(testUser);
        user.then().statusCode(200);
        String accessToken = userApi.getUserAccessToken(user);
        String refreshToken = userApi.getUserRefreshToken(user);
        userApi.userLogout(refreshToken);

        userApi.userLogin(testUser.getEmail(), "123456").then()
                .statusCode(401 )
                .assertThat().body("message", equalTo("email or password are incorrect"));
        userApi.deleteUser(accessToken).then().statusCode(202);
    }
}
