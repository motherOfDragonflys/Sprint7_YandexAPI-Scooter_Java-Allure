package courier;

import base.BaseCourierTest;

import io.restassured.response.Response;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import model.CourierModel;
import static data.CourierData.*;
import static steps.CouriersSteps.*;
import static steps.CommonSteps.*;

@Epic("Курьеры")
@Feature("Логин курьера")
public class LoginCourierTest extends BaseCourierTest {

    @Override
    @Before
    @Step("Создание курьера с login + password")
    public void setUpCourier() {
        super.setUpCourier();
        // Для запроса логина передаём только login и password, firstName не участвует в аутентификации
        courier.setFirstName(null);
    }

//__ТЕСТЫ_______________________________________________________________________________________________________________
    @Test
    @DisplayName("Успешный логин существующего курьера")
    public void testLoginCourierSuccess() {

        Response response = loginCourier(courier);

        checkStatusCode(response, 200);
        checkIdIsNotNull(response);
    }

    @Test
    @DisplayName("При попытке входа с неверным паролем для существующего логина появляется 404")
    public void testLoginCourierWithWrongPassword() {

        // Меняем пароль на неверный — только в courier
        courier.setPassword(
                getDifferentPassword(courier.getPassword())
        );

        Response response = loginCourier(courier);

        checkStatusCode(response, 404);
        checkErrorMessage(response, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("При попытке входа с неверным логином, но существующим паролем появляется ошибка 404")
    public void testLoginCourierWithWrongLogin() {

        // Меняем логин на несуществующий — только в courier
        courier.setLogin(
                getDifferentLogin(courier.getLogin())
        );

        Response response = loginCourier(courier);

        checkStatusCode(response, 404);
        checkErrorMessage(response, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("При попытке входа c несуществующей парой логин+пароль курьера появляется ошибка 404")
    public void testLoginCourierNonExistent() {

        // Генерируем случайного курьера, но не создаём его в API (в before создастся, но его не используем)
        CourierModel nonExistentCourier = randomCourier();
        nonExistentCourier.setFirstName(null);

        // Пытаемся залогиниться
        Response response = loginCourier(nonExistentCourier);

        checkStatusCode(response, 404);
        checkErrorMessage(response, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("При попытке входа без пароля появляется ошибка 400")
    @Issue("BUG-2 Ответ 504 вместо 400 ")
    public void testLoginCourierWithoutPassword() {

        courier.setPassword(null);

        Response response = loginCourier(courier);

        checkStatusCode(response, 400);
        checkErrorMessage(response, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("При попытке входа без логина появляется ошибка 400")
    public void testLoginCourierWithoutLogin() {

        courier.setLogin(null);

        Response response = loginCourier(courier);

        checkStatusCode(response, 400);
        checkErrorMessage(response, "Недостаточно данных для входа");
    }
}
