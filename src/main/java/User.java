import com.github.javafaker.Faker;

public class User {
    public final String email;
    public final String password;
    public final String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static User getRandom() {
        return User.getRandom(true, true, true);
    }

    public static User getRandom(boolean useEmail, boolean usePassword, boolean useName) {

        Faker faker = new Faker();

        String password = "";
        String email = "";
        String name = "";

        if (useEmail) {
            email = faker.internet().emailAddress();
        }
        if (usePassword) {
            password = faker.internet().password();
        }
        if (useName) {
            name = faker.name().firstName();
        }

        return new User(email, password, name);
    }

    @Override
    public String toString() {
        return String.format("Пользователь: { Имя:%s, Email:%s, Пароль:%s }", this.name, this.email, this.password);
    }
}