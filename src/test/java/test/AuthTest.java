package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import static data.DataGenerator.Registration.getRegisteredUser;
import static data.DataGenerator.getRandomLogin;
import static data.DataGenerator.getRandomPassword;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;


public class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }


    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        given()
                .body(registeredUser)
                .when()
                .then()
                .statusCode(200);
        $(withText("Личный кабинет")).shouldBe(visible);

    }


    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var registeredUser = getRegisteredUser("active");
        given()
                .body(registeredUser)
                .when()
                .then()
                .statusCode(200)
        ;
        $(".notification__content").shouldBe(visible)
                .shouldHave(exactText("Неверно указан логин или пароль"));
    }


    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        given()
                .body(blockedUser)
                .when()
                .then()
                .statusCode(200)
        ;
        $(".notification__content").shouldBe(visible)
                .shouldHave(exactText("Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        given()
                .body(registeredUser)
                .when()
                .then()
                .statusCode(200);
        $(".input_type_text").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".input_type_text").setValue(wrongLogin);
        $(".notification__content").shouldBe(visible)
                .shouldHave(exactText("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        given()
                .body(registeredUser)
                .when()
                .then()
                .statusCode(200);
        $(".input_type_password").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".input_type_password").setValue(wrongPassword);
        $(".notification__content").shouldBe(visible)
                .shouldHave(exactText("Неверно указан логин или пароль"));
    }
}



