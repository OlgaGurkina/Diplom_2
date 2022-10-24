package ru.yandex.praktikum;

import api.client.UserApi;
import api.model.User;
import api.util.RandomParamsForUser;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class TestUserLogin {
    UserApi userApi;
    User testUser;
    Response user;

    @Before
    public void setUp() {
        userApi = new UserApi();
        testUser = new User(new RandomParamsForUser());
        user = userApi.createUser(testUser);
    }

    @Test
    @DisplayName("Check User Login")
    @Description("check Login with correct params")
    public void checkUserLogin() {
        user.then().statusCode(200);
        String refreshToken = userApi.getUserRefreshToken(user);
        userApi.userLogout(refreshToken);

        userApi.userLogin(testUser.getEmail(), testUser.getPassword()).then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Check User Login with no email")
    @Description("check Login with incorrect params: no email")
    public void checkUserLoginWithNoEmail() {
        user.then().statusCode(200);
        String refreshToken = userApi.getUserRefreshToken(user);
        userApi.userLogout(refreshToken);

        userApi.userLogin(null, testUser.getPassword()).then()
                .statusCode(401)
                .assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Check User Login with no password")
    @Description("check Login with incorrect params: no password")
    public void checkUserLoginWithNoPass() {
        user.then().statusCode(200);
        String refreshToken = userApi.getUserRefreshToken(user);
        userApi.userLogout(refreshToken);

        userApi.userLogin(testUser.getEmail(), null).then()
                .statusCode(401)
                .assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Check User Login with wrong Password")
    @Description("check Login with incorrect params: wrong password")
    public void checkUserLoginWithIncorrectPass() {
        user.then().statusCode(200);
        String refreshToken = userApi.getUserRefreshToken(user);
        userApi.userLogout(refreshToken);

        userApi.userLogin(testUser.getEmail(), "123456").then()
                .statusCode(401)
                .assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void clearTestData() {
        userApi.deleteUser(userApi.getUserAccessToken(user)).then().statusCode(202);
    }
}
