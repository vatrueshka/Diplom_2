import client.UserClientSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;

import static org.hamcrest.Matchers.*;

public class UserCreationTest {
    private UserClientSteps userClientSteps;
    private User user;
    private ValidatableResponse validatableResponse;

    @Before
    public void setUp() {
        userClientSteps = new UserClientSteps();
    }

    @After
    public void tearDown() {
        String bearerToken = validatableResponse.extract().path("accessToken");
        userClientSteps.delete(bearerToken);
    }

    @Test
    @DisplayName("Проверка,что пользователя можно создать.")
    @Description("Тест /api/auth/register")
    public void checkUserCanBeCreated(){
        // Arrange - Подготовка данных
        user = User.getRandom();
        //System.out.println(user.email + user.name + user.password);

        // Act - Создать клиента
        validatableResponse = userClientSteps.create(user);

        // Assert - Проверка
        validatableResponse.assertThat().statusCode(200);
        validatableResponse.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Проверьте, что нельзя создать пользователя, который уже зарегистрирован.")
    @Description("Тест /api/auth/register")
    public void checkUserCannotTwoIdenticalCreated(){
        // Arrange
        user = User.getRandom();

        // Act
        userClientSteps.create(user);
        validatableResponse = userClientSteps.create(user);

        // Assert
        validatableResponse.assertThat().statusCode(403);
        validatableResponse.assertThat().body("success", equalTo(false));
        validatableResponse.assertThat().body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Проверка регистрации пользователя без пароля.")
    @Description("Тест /api/auth/register")
    public void checkCreateUserWithoutPassword() {
        user = User.getRandom(true,false, true);

        validatableResponse = userClientSteps.create(user);

        validatableResponse.assertThat().statusCode(403);
        validatableResponse.assertThat().body("success", equalTo(false));
        validatableResponse.assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Проверка регистрации пользователя без имени.")
    @Description("Тест /api/auth/register")
    public void checkCreateUserWithoutName() {
        user = User.getRandom(true,true, false);

        validatableResponse = userClientSteps.create(user);

        validatableResponse.assertThat().statusCode(403);
        validatableResponse.assertThat().body("success", equalTo(false));
        validatableResponse.assertThat().body("message", equalTo("Email, password and name are required fields"));
    }
}