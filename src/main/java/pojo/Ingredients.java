package pojo;

import client.RestAssuredClient;
import com.github.javafaker.Faker;
import io.restassured.response.ValidatableResponse;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class Ingredients extends RestAssuredClient {
    private static final String INGREDIENTS_PATH = "api/ingredients";
    private static Faker faker = new Faker();

    private ArrayList<Object> ingredients;

    public Ingredients(ArrayList<Object> ingredients) {
        this.ingredients = ingredients;
    }

    public Ingredients() {
    }

    public static Ingredients getRandomBurger() {
        //Запрос данных об ингредиентах
        ValidatableResponse response = given()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENTS_PATH)
                .then()
                .statusCode(200);

        // Создаем список
        ArrayList<Object> ingredients = new ArrayList<>();

        // Генерируем рандомный индекс в диапазоне
        int bunIndex = faker.random().nextInt(0, 1);
        int mainIndex = faker.random().nextInt(0, 8);
        int sauceIndex = faker.random().nextInt(0, 3);

        // Получение списка по типу ингредиентов
        List<Object> bunIngredients = response.extract().jsonPath().getList("data.findAll{it.type == 'bun'}._id");
        List<Object> mainIngredients = response.extract().jsonPath().getList("data.findAll{it.type == 'main'}._id");
        List<Object> sauceIngredients = response.extract().jsonPath().getList("data.findAll{it.type == 'sauce'}._id");

        // Добавление ингредиента в массив по индексу
        ingredients.add(bunIngredients.get(bunIndex));
        ingredients.add(mainIngredients.get(mainIndex));
        ingredients.add(sauceIngredients.get(sauceIndex));

        return new Ingredients(ingredients);
    }

    public static Ingredients getNullIngredients() {
        ArrayList<Object> ingredients = new ArrayList<>();
        return new Ingredients(ingredients);
    }

    public static Ingredients getIncorrectIngredients() {
        ArrayList<Object> ingredients = new ArrayList<>();
        ingredients.add(faker.internet().uuid());
        ingredients.add(faker.internet().uuid());
        return new Ingredients(ingredients);
    }

    public ArrayList<Object> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Object> ingredients) {
        this.ingredients = ingredients;
    }

}