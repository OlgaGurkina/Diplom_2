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

public class TestUpdateUser {
    User user;
    UserApi userApi;
    Response response;

    @Before
    public void setUp() {
        user = new User(new RandomParamsForUser());
        userApi = new UserApi();
        response = userApi.createUser(user);
        response.then().statusCode(200);
    }

    @Test
    @DisplayName("Check User update with auth")
    @Description("check ability to update USER with auth and correct upd_info")
    public void checkUserUpdate() {
        String accessToken = userApi.getUserAccessToken(response);
        String newEmail = RandomParamsForUser.randomEmail();
        Response updUser = userApi.userUpdate(newEmail, "EditedName", accessToken);
        updUser.then().statusCode(200)
                .assertThat().body("user.email", equalTo(newEmail))
                .assertThat().body("user.name", equalTo("EditedName"));
    }

    @Test
    @DisplayName("Check User update with auth, update only Email")
    @Description("check ability to update USER with auth and set correct new Email")
    public void checkUserUpdateEmail() {
        String accessToken = userApi.getUserAccessToken(response);
        String newEmail = RandomParamsForUser.randomEmail();
        Response updUser = userApi.userUpdate(newEmail, user.getName(), accessToken);
        updUser.then().statusCode(200)
                .assertThat().body("user.email", equalTo(newEmail))
                .assertThat().body("user.name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Check User update with auth, update Email to existing")
    @Description("check ability to update USER with auth and set existing new Email")
    public void checkUserUpdateEmailWithExistingOne() {
        UserApi userApi = new UserApi();
        User testUser1 = new User(new RandomParamsForUser()); //user to update
        User testUser2 = new User(new RandomParamsForUser()); //control user to get existing email

        Response controlUser = userApi.createUser(testUser2);
        controlUser.then().statusCode(200);
        String accessTokenOfControlUser = userApi.getUserAccessToken(controlUser);

        Response user = userApi.createUser(testUser1);
        user.then().statusCode(200);
        String accessToken = userApi.getUserAccessToken(user);
        Response updUser = userApi.userUpdate(testUser2.getEmail(), testUser1.getName(), accessToken);
        updUser.then().statusCode(403)
                .assertThat().body("message", equalTo("User with such email already exists"));

        userApi.deleteUser(accessToken).then().statusCode(202);
        userApi.deleteUser(accessTokenOfControlUser).then().statusCode(202);
    }

    @Test
    @DisplayName("Check User update with auth, update Name")
    @Description("check ability to update USER with auth and set new Name")
    public void checkUserUpdateName() {
        String accessToken = userApi.getUserAccessToken(response);
        Response updUser = userApi.userUpdate(user.getEmail(), "test_new_name", accessToken);
        updUser.then().statusCode(200)
                .assertThat().body("user.email", equalTo(user.getEmail()))
                .assertThat().body("user.name", equalTo("test_new_name"));
    }

    @Test
    @DisplayName("Check User update with no auth")
    @Description("try to update user with no authorization in header")
    public void checkUserUpdateWithNoAuth() {
        String newEmail = RandomParamsForUser.randomEmail();
        Response updUser = userApi.userUpdate(newEmail, "EditedName", null);
        updUser.then().statusCode(401)
                .assertThat().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Check User update with no auth, update only email")
    @Description("try to update user with no authorization in header, try upd email")
    public void checkUserUpdateWithNoAuthEmail() {
        String newEmail = RandomParamsForUser.randomEmail();
        Response updUser = userApi.userUpdate(newEmail, user.getName(), null);
        updUser.then().statusCode(401)
                .assertThat().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Check User update with no auth, update only name")
    @Description("try to update user with no authorization in header, try upd name")
    public void checkUserUpdateWithNoAuthName() {
        Response updUser = userApi.userUpdate(user.getEmail(), "new_name", null);
        updUser.then().statusCode(401)
                .assertThat().body("message", equalTo("You should be authorised"));
    }

    @After
    public void clearTestData() {
        userApi.deleteUser(userApi.getUserAccessToken(response)).then().statusCode(202);
    }
}
