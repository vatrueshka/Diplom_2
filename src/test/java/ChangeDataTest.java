import client.UserClientSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;
import pojo.UserCredentials;

import static org.hamcrest.Matchers.equalTo;

public class ChangeDataTest {

    private User user;
    private String bearerToken;
    private UserClientSteps userClientSteps;

    @Before
    public void setUp() {
        user = User.getRandom();
        userClientSteps = new UserClientSteps();
    }

    @After
    public void tearDown() {
        userClientSteps.delete(bearerToken);
    }


    @Test
    @DisplayName("Редактирование данных у авторизованного пользователя")
    @Description("Изменение данных пользователя. Смена пароля")
    public void userInfoCanBeChangePasswordTest() {
        userClientSteps.create(user); // Создание пользователя
        ValidatableResponse login = userClientSteps.login(UserCredentials.from(user)); // Авторизация пользователя
        bearerToken = login.extract().path("accessToken"); // сохраняем токен

        // Изменение информации о пользователе
        ValidatableResponse info = userClientSteps.userInfoChange(bearerToken, UserCredentials.getUserWithRandomPassword());

        // Проверка тела сообщения
        info.assertThat().statusCode(200);
        info.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Редактирование данных у авторизованного пользователя")
    @Description("Изменение данных пользователя. Смена email")
    public void userInfoCanBeChangeEmailTest() {
        userClientSteps.create(user); // Создание пользователя
        ValidatableResponse login = userClientSteps.login(UserCredentials.from(user)); // Авторизация пользователя
        bearerToken = login.extract().path("accessToken"); // сохраняем токен

        // Изменение информации о пользователе
        ValidatableResponse info = userClientSteps.userInfoChange(bearerToken, UserCredentials.getUserWithRandomEmail());

        // Проверка тела сообщения
        info.assertThat().statusCode(200);
        info.assertThat().body("success", equalTo(true));
    }


    @Test
    @DisplayName("Редактирование данных на email, который уже есть в базе, у авторизованного пользователя")
    @Description("Изменение данных пользователя. Одинаковый email")
    public void userInfoCanNotBeChangeWithSameEmailTest() {
        userClientSteps.create(user); // Создание первого юзера

        User changeEmailTestUser = User.getRandom();
        userClientSteps.create(changeEmailTestUser); // Создание второго юзера

        ValidatableResponse login = userClientSteps.login(UserCredentials.from(user)); // Авторизация пользователя
        bearerToken = login.extract().path("accessToken"); // сохраняем токен

        // Изменение информации о пользователе
        ValidatableResponse info = userClientSteps.userInfoChange(bearerToken, UserCredentials.from(changeEmailTestUser));

        // Проверка тела сообщения
        info.assertThat().statusCode(403);
        info.assertThat().body("success", equalTo(false));
        info.assertThat().body("message", equalTo("User with such email already exists"));

        ValidatableResponse loginAsUser2 = userClientSteps.login(UserCredentials.from(changeEmailTestUser)); // Получение токена второго юзера
        userClientSteps.delete(loginAsUser2.extract().path("accessToken")); //Удаление второго юзера
    }

    @Test
    @DisplayName("Редактирование данных у неавторизованного пользователя")
    @Description("Изменение данных неавторизованного пользователя. Смена пароля")
    public void userInfoCanNotBeChangePasswordTest() {
        bearerToken = "";

        // Изменение информации о пользователе
        ValidatableResponse info = userClientSteps.userInfoChange(bearerToken, UserCredentials.getUserWithRandomPassword());

        // Проверка тела сообщения
        info.assertThat().statusCode(401);
        info.assertThat().body("success", equalTo(false));
        info.assertThat().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Редактирование данных у неавторизованного пользователя")
    @Description("Изменение данных неавторизованного пользователя. Смена email")
    public void userInfoCanNotBeChangeEmailTest() {
        bearerToken = "";

        // Изменение информации о пользователе
        ValidatableResponse info = userClientSteps.userInfoChange(bearerToken, UserCredentials.getUserWithRandomEmail());

        // Проверка тела сообщения
        info.assertThat().statusCode(401);
        info.assertThat().body("success", equalTo(false));
        info.assertThat().body("message", equalTo("You should be authorised"));
    }
}