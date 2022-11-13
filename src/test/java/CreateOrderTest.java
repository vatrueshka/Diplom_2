import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CreateOrderTest {
    private User user;
    private UserClientSteps userClientSteps;
    private Ingredients ingredients;
    public OrderClient orderClient;
    String bearerToken;

    // Создание рандомного пользователя и бургера
    @Before
    public void setUp(){
        user = User.getRandom();
        userClientSteps = new UserClientSteps();
        ingredients = Ingredients.getRandomBurger();
        orderClient = new OrderClient();
    }

    @After
    public void tearDown() {
        userClientSteps.delete(bearerToken);
    }

    @Test
    @DisplayName("Создание заказа. Зарегистрированный пользователь")
    @Description("Тест /api/orders")
    public void orderCanBeCreatedRegisteredUser (){
        // Создание пользователя
        ValidatableResponse userResponse = userClientSteps.create(user);
        bearerToken = userResponse.extract().path("accessToken");

        // Создание заказа
        ValidatableResponse orderResponse = orderClient.create(bearerToken,ingredients);
        int orderNumber = orderResponse.extract().path("order.number"); // Получение номера созданного заказа

        // Проверка тела ответа запроса
        orderResponse.assertThat().statusCode(200);
        orderResponse.assertThat().body("success", equalTo(true));
        assertThat("The order number is missing", orderNumber, is(not(0))); // Проверка, что присвоен номер заказа
    }

    @Test
    @DisplayName("Создание заказа. Не зарегистрированный пользователь")
    @Description("Тест /api/orders")
    public void orderCanBeCreatedNonRegisteredUser (){
        bearerToken = "";

        // Создание заказа
        ValidatableResponse orderResponse = orderClient.create(bearerToken, ingredients);
        int orderNumber = orderResponse.extract().path("order.number"); // Получение номера созданного заказа

        // Проверка тела ответа запроса
        orderResponse.assertThat().statusCode(200);
        orderResponse.assertThat().body("success", equalTo(true));
        assertThat("The order number is missing", orderNumber, is(not(0))); // Проверка, что присвоен номер заказа
    }

    @Test
    @DisplayName ("Создание заказа без ингредиентов")
    @Description("Тест /api/orders")
    public void orderCanNotBeCreatedWithOutIngredients (){
        // Создание пользователя
        ValidatableResponse userResponse = userClientSteps.create(user);
        bearerToken = userResponse.extract().path("accessToken");

        // Создание заказа без ингредиентов
        ValidatableResponse orderResponse = orderClient.create(bearerToken,Ingredients.getNullIngredients());

        // Проверка тела ответа запроса
        orderResponse.assertThat().statusCode(400);
        orderResponse.assertThat().body("success", equalTo(false));
        orderResponse.assertThat().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName ("Создание заказа с невалидными ингредиентами")
    @Description("Тест /api/orders")
    public void orderCanNotBeCreatedWithIncorrectIngredients (){
        // Создание пользователя
        ValidatableResponse userResponse = userClientSteps.create(user);
        bearerToken = userResponse.extract().path("accessToken");

        // Создание заказа
        ValidatableResponse orderResponse = orderClient.create(bearerToken,Ingredients.getIncorrectIngredients());

        // Проверка тела ответа запроса
        orderResponse.assertThat().statusCode(500);
    }
}