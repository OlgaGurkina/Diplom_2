package ru.yandex.praktikum;

import api.client.UserApi;
import api.model.User;
import api.util.RandomParamsForUser;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;


public class TestUserCreation {
    @Test
    @DisplayName("Check User creation")
    @Description("check ability to create USER with correct params")
    public void checkUserCreation() {
        UserApi userApi = new UserApi();
        User testUser = new User(new RandomParamsForUser());
        Response responseUser = userApi.createUser(testUser);
        responseUser.then().statusCode(200);

        String accessToken = responseUser.then().extract().body().path("accessToken").toString();
        userApi.deleteUser(accessToken).then().statusCode(202);
    }

    @Test
    @DisplayName("Check 2 Users with same parameters")
    @Description("check ability to create USER with same params")
    public void checkTwoEqualUsersCreation() {
        UserApi userApi = new UserApi();
        User testUser = new User(new RandomParamsForUser());
        Response responseUser1 = userApi.createUser(testUser);
        responseUser1.then().statusCode(200);
        Response responseUser2 = userApi.createUser(testUser);
        responseUser2.then().statusCode(403)
                .assertThat().body("message", equalTo("User already exists"));
        String accessToken = userApi.getUserAccessToken(responseUser1);
        userApi.deleteUser(accessToken).then().statusCode(202);
    }

    @Test
    @DisplayName("Check  Users with no Email")
    @Description("check ability to create USER with no Email")
    public void checkUsersCreationWithNoEmail() {
        UserApi userApi = new UserApi();
        User testUser = new User(new RandomParamsForUser());
        testUser.setEmail(null);
        Response responseUser1 = userApi.createUser(testUser);
        responseUser1.then().statusCode(403)
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Check  Users with no Password")
    @Description("check ability to create USER with no Password")
    public void checkUsersCreationWithNoPassword() {
        UserApi userApi = new UserApi();
        User testUser = new User(new RandomParamsForUser());
        testUser.setPassword(null);
        Response responseUser1 = userApi.createUser(testUser);
        responseUser1.then().statusCode(403)
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Check  Users with no Name")
    @Description("check ability to create USER with no Name")
    public void checkUsersCreationWithNoName() {
        UserApi userApi = new UserApi();
        User testUser = new User(new RandomParamsForUser());
        testUser.setName(null);
        Response responseUser1 = userApi.createUser(testUser);
        responseUser1.then().statusCode(403)
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }
}
