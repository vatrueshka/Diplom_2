package pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.javafaker.Faker;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCredentials {
    private static Faker faker = new Faker();

    private String email;
    private String password;

    public UserCredentials() {
    }

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserCredentials from(User user) {
        return new UserCredentials(user.getEmail(), user.getPassword());
    }

    public static UserCredentials getUserWithEmail(User user) {
        return new UserCredentials().setEmail(user.getEmail());
    }

    public static UserCredentials getUserWithRandomEmail() {
        return new UserCredentials().setEmail(faker.internet().emailAddress());
    }

    public static UserCredentials getUserWithRandomPassword() {
        return new UserCredentials().setPassword(faker.internet().password());
    }

    public String getEmail() {
        return email;
    }

    public UserCredentials setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserCredentials setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String toString() {
        return String.format("Пользователь { Email:%s, Пароль:%s }", this.email, this.password);
    }
}