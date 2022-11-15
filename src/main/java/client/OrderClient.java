package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.Ingredients;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestAssuredClient {
    private static final String ORDER_PATH = "api/orders";

    @Step("Создание заказа")
    public ValidatableResponse create(String token, Ingredients ingredients) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token.replace("Bearer ", ""))
                .body(ingredients)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Получение списка всех заказов")
    public ValidatableResponse orderInfo() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH + "/all")
                .then();
    }

    @Step("Получение списка заказов пользователя")
    public ValidatableResponse userOrderInfo(String token) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token.replace("Bearer ", ""))
                .when()
                .get(ORDER_PATH)
                .then();
    }
}