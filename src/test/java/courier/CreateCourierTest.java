package courier;

import base.BaseCourierTest;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.junit.Test;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;

import model.CourierModel;
import static data.CourierData.*;
import static steps.CouriersSteps.*;
import static steps.CommonSteps.*;

@Epic("Курьеры")
@Feature("Создание курьера")
public class CreateCourierTest extends BaseCourierTest {

    // Отключаем логику Before на создание курьера, так как данный набор тестов для того и есть
    @Override
    public void setUpCourier() {
    }

//__ТЕСТЫ_______________________________________________________________________________________________________________

    @Test
    @DisplayName("Успешное создание курьера с валидными данными")
    @Description("Покрытие требований: \n" +
            "курьера можно создать, \n" +
            "чтобы создать курьера, нужно передать в ручку все обязательные поля, \n" +
            "запрос возвращает правильный код ответа, \n" +
            "успешный запрос возвращает ok: true")
    public void testCreateCourierSuccess() {

        courier  = randomCourier();

        Response response = createCourier(courier);

        rememberIfCreated(response, courier);

        checkStatusCode(response, 201);
        checkOkField(response, true);

    }

    @Test
    @DisplayName("При создании одинаковых курьеров появляется ошибка 409")
    @Description("Покрытие требования: \n" +
            "нельзя создать двух одинаковых курьеров")
    @Issue("BUG-1 Отличие текста ошибки")
    public void testGetConflictWhenTryCreateDoubleCourier() {

        // Генерим данные курьера
        CourierModel firstCourier = randomCourier();

        // создаем курьера, убеждаемся, что он создан
        Response firstResponse = createCourier(firstCourier);
        checkStatusCode(firstResponse, 201);
        checkOkField(firstResponse, true);

        rememberIfCreated(firstResponse, firstCourier);

        // отправляем еще раз запрос на создание курьера с теми же данными
        Response response = createCourier(firstCourier);
        checkStatusCode(response, 409);
        checkErrorMessage(response, "Этот логин уже используется");

    }

    @Test
    @DisplayName("При создании курьера c уже существующим логином появляется ошибка 409")
    @Description("Покрытие требования: \n" +
            "если создать пользователя с логином, который уже есть, возвращается ошибка")
    @Issue("BUG-1 Отличие текста ошибки")
    public void testGetConflictWhenTryCreateCourierWithCreatedLogin() {

        CourierModel firstCourier = randomCourier();

        // Создаем курьера
        Response firstResponse = createCourier(firstCourier);
        checkStatusCode(firstResponse, 201);
        checkOkField(firstResponse, true);

        rememberIfCreated(firstResponse, firstCourier);

        // создаем пароль, на входе фактический старый пароль, чтобы проверить отличия
        firstCourier.setPassword(getDifferentPassword(firstCourier.getPassword()));

        // отправляем курьера раз с новым паролем
        Response response = createCourier(firstCourier);
        checkStatusCode(response, 409);
        checkErrorMessage(response, "Этот логин уже используется");

    }

    @Test
    @DisplayName("При создании курьера без пароля появляется ошибка 400")
    @Description("Покрытие требования: \n" +
            "если одного из полей нет, запрос возвращает ошибку")
    public void testGetBadRequestWhenTryCreateCourierWithoutPassword() {

        courier = randomCourier();
        // Присваиваем паролю null
        courier.setPassword(null);

        Response response = createCourier(courier);

        rememberIfCreated(response, courier);

        checkStatusCode(response, 400);
        checkErrorMessage(response, "Недостаточно данных для создания учетной записи");

    }

    @Test
    @DisplayName("При создании курьера без логина появляется ошибка 400")
    @Description("Покрытие требования: \n" +
            "если одного из полей нет, запрос возвращает ошибку")
    public void testGetBadRequestWhenTryCreateCourierWithoutLogin() {

        courier = randomCourier();
        // Присваиваем логину null
        courier.setLogin(null);

        Response response = createCourier(courier);

        rememberIfCreated(response, courier);

        checkStatusCode(response, 400);
        checkErrorMessage(response, "Недостаточно данных для создания учетной записи");

    }
}
