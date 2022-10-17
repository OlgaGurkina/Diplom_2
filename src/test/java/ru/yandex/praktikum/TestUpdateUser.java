package ru.yandex.praktikum;

import groovy.xml.StreamingDOMBuilder;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class TestUpdateUser {

    @Test
    @DisplayName("Check User update with auth")
    @Description("check ability to update USER with auth and correct upd_info")
    public void checkUserUpdate(){
        UserApi userApi = new UserApi();
        User testUser = new User(new RandomParamsForUser());
        Response user = userApi.createUser(testUser);
        user.then().statusCode(200);
        String accessToken = userApi.getUserAccessToken(user);
        Response updUser = userApi.userUpdate("test@te.st", "EditedName", accessToken);
        updUser.then().statusCode(200)
                .assertThat().body("user.email", equalTo("test@te.st"))
                .assertThat().body("user.name", equalTo("EditedName"));

        userApi.deleteUser(accessToken).then().statusCode(202);
    }

    @Test
    @DisplayName("Check User update with auth, update only Email")
    @Description("check ability to update USER with auth and set correct new Email")
    public void  checkUserUpdateEmail(){
        UserApi userApi = new UserApi();
        User testUser = new User(new RandomParamsForUser());
        Response user = userApi.createUser(testUser);
        user.then().statusCode(200);
        String accessToken = userApi.getUserAccessToken(user);
        Response updUser = userApi.userUpdate("test@te.st", testUser.getName(), accessToken);
        updUser.then().statusCode(200)
                .assertThat().body("user.email", equalTo("test@te.st"))
                .assertThat().body("user.name", equalTo(testUser.getName()));

        userApi.deleteUser(accessToken).then().statusCode(202);
    }

    @Test
    @DisplayName("Check User update with auth, update Email to existing")
    @Description("check ability to update USER with auth and set existing new Email")
    public void  checkUserUpdateEmailWithExistingOne(){
        UserApi userApi = new UserApi();
        User testUser1 = new User(new RandomParamsForUser()); //user to update
        User testUser2 = new User(new RandomParamsForUser()); //control user to get existing email

        Response controlUser = userApi.createUser(testUser2);
        controlUser.then().statusCode(200);
        String accessTokenOfControlUser = userApi.getUserAccessToken(controlUser);

        Response user = userApi.createUser(testUser1);
        user.then().statusCode(200);
        String accessToken = userApi.getUserAccessToken(user);
        Response updUser = userApi.userUpdate(testUser2.getEmail(), "new_name", accessToken);
        updUser.then().statusCode(403)
                .assertThat().body("message", equalTo("User with such email already exists"));

        userApi.deleteUser(accessToken).then().statusCode(202);
        userApi.deleteUser(accessTokenOfControlUser).then().statusCode(202);
    }

    @Test
    @DisplayName("Check User update with auth, update Name")
    @Description("check ability to update USER with auth and set new Name")
    public void  checkUserUpdateName(){
        UserApi userApi = new UserApi();
        User testUser = new User(new RandomParamsForUser());
        Response user = userApi.createUser(testUser);
        user.then().statusCode(200);
        String accessToken = userApi.getUserAccessToken(user);
        Response updUser = userApi.userUpdate(testUser.getEmail(), "test_new_name", accessToken);
        updUser.then().statusCode(200)
                .assertThat().body("user.email", equalTo(testUser.getEmail()))
                .assertThat().body("user.name", equalTo("test_new_name"));

        userApi.deleteUser(accessToken).then().statusCode(202);
    }

    @Test
    @DisplayName("Check User update with no auth")
    @Description("try to update user with no authorization in header")
    public void checkUserUpdateWithNoAuth(){
        UserApi userApi = new UserApi();
        User testUser = new User(new RandomParamsForUser());
        Response user = userApi.createUser(testUser);
        user.then().statusCode(200);
        String accessToken = userApi.getUserAccessToken(user);
        Response updUser = userApi.userUpdate("test@te.st", "EditedName", null);
        updUser.then().statusCode(401)
                .assertThat().body("message", equalTo("You should be authorised"));

        userApi.deleteUser(accessToken).then().statusCode(202);
    }

    @Test
    @DisplayName("Check User update with no auth, update only email")
    @Description("try to update user with no authorization in header, try upd email")
    public void checkUserUpdateWithNoAuthEmail(){
        UserApi userApi = new UserApi();
        User testUser = new User(new RandomParamsForUser());
        Response user = userApi.createUser(testUser);
        user.then().statusCode(200);
        String accessToken = userApi.getUserAccessToken(user);
        Response updUser = userApi.userUpdate("test@te.st", testUser.getName(), null);
        updUser.then().statusCode(401)
                .assertThat().body("message", equalTo("You should be authorised"));

        userApi.deleteUser(accessToken).then().statusCode(202);
    }

    @Test
    @DisplayName("Check User update with no auth, update only name")
    @Description("try to update user with no authorization in header, try upd name")
    public void checkUserUpdateWithNoAuthName(){
        UserApi userApi = new UserApi();
        User testUser = new User(new RandomParamsForUser());
        Response user = userApi.createUser(testUser);
        user.then().statusCode(200);
        String accessToken = userApi.getUserAccessToken(user);
        Response updUser = userApi.userUpdate(testUser.getEmail(), "new_name", null);
        updUser.then().statusCode(401)
                .assertThat().body("message", equalTo("You should be authorised"));

        userApi.deleteUser(accessToken).then().statusCode(202);
    }
}
