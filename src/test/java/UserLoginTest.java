import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserLoginTest {
    private Faker faker = new Faker();
    private UserClientSteps userClientSteps;
    private User user;
    private String refreshToken;
    private ValidatableResponse validatableResponse;

    @Before
    public void setUp() {
        userClientSteps = new UserClientSteps();
        user = User.getRandom();
        userClientSteps.create(user);
    }

    @After
    public void tearDown() {
        String bearerToken = validatableResponse.extract().path("accessToken");
        userClientSteps.delete(bearerToken);
    }

    @Test
    @DisplayName("Проверить, что пользователь может авторизоваться")
    @Description("Тест /api/auth/login")
    public void checkUserLogin() {
        validatableResponse = userClientSteps.login(UserCredentials.from(user));
        refreshToken = validatableResponse.extract().path("refreshToken");

        assertThat("Courier ID incorrect", refreshToken, is(not(0)));
        validatableResponse.assertThat().statusCode(200);
        validatableResponse.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Проверить, что пользователь не может авторизоваться без email")
    @Description("Тест /api/auth/login")
    public void checkUserLoginWithoutUserName() {

        validatableResponse = userClientSteps.login(new UserCredentials(null, user.password));

        validatableResponse.assertThat().statusCode(401);
        validatableResponse.assertThat().body("success", equalTo(false));
        validatableResponse.assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Проверить, что пользователь не может авторизоваться без пароля")
    @Description("Тест /api/auth/login")
    public void checkUserLoginNull() {


        validatableResponse = userClientSteps.login(new UserCredentials(user.email, null));

        validatableResponse.assertThat().statusCode(401);
        validatableResponse.assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Проверить, что пользователь не может авторизоваться с неверным логином")
    @Description("Тест /api/auth/login")
    public void checkLoginWithInvalidEmail() {

        String incorrectEmail = faker.internet().emailAddress();

        validatableResponse = userClientSteps.login(new UserCredentials(incorrectEmail, user.password));

        validatableResponse.assertThat().statusCode(401);
        validatableResponse.assertThat().body("success", equalTo(false));
        validatableResponse.assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Проверить, что пользователь не может авторизоваться с неверным паролем")
    @Description("Тест /api/auth/login")
    public void checkLoginWithInvalidPassword() {

        String incorrectPassword = faker.internet().password();

        validatableResponse = userClientSteps.login(new UserCredentials(user.email, incorrectPassword));

        validatableResponse.assertThat().statusCode(401);
        validatableResponse.assertThat().body("message", equalTo("email or password are incorrect"));
    }
}