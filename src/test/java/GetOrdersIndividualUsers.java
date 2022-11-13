import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.*;

public class GetOrdersIndividualUsers {
    private User user;
    private UserClientSteps userClientSteps;
    private Ingredients ingredients;
    private OrderClient orderClient;
    private String bearerToken;

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
    @Description("Получение списка заказов авторизованного пользователя")
    public void orderUserInfoCanBeGetAuthUser (){
        // Создание пользователя
        userClientSteps.create(user);
        ValidatableResponse login = userClientSteps.login(UserCredentials.from(user)); // Авторизация пользователя
        bearerToken = login.extract().path("accessToken"); // Получение токена

        // Информация о заказах пользователя
        ValidatableResponse orderInfo = orderClient.userOrderInfo(bearerToken);
        // Получение тела списка заказов
        List<Map<String, Object>> ordersList = orderInfo.extract().path("orders");

        // Проверка тела ответа запроса
        orderInfo.assertThat().statusCode(200);
        orderInfo.assertThat().body("success", equalTo(true));
        assertThat("Orders list empty", ordersList, is(not(0))); // Проверка что список заказов не пустой
    }

    @Test
    @Description("Получение списка заказов не авторизованного пользователя")
    public void orderUserInfoCantBeGetNonAuthUser (){
        bearerToken = "";

        // Информация о заказах пользователя
        ValidatableResponse orderInfo = orderClient.userOrderInfo(bearerToken);

        // Проверка тела ответа запроса
        orderInfo.assertThat().statusCode(401);
        orderInfo.assertThat().body("success", equalTo(false));
        orderInfo.assertThat().body("message", equalTo("You should be authorised"));
    }
}