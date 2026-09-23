package courier;

import base.BaseCourierTest;

import io.qameta.allure.*;
import io.restassured.response.Response;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static steps.CouriersSteps.*;
import static steps.CommonSteps.*;

@Epic("Курьеры")
@Feature("Удаление курьера")
public class DeleteCourierTest extends BaseCourierTest {

    // After не отключаем, чтобы созданные в Before и измененные далее для теста курьеры удалились

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
    @DisplayName("Успешное удаление существующего курьера")
    @Description("Покрытие требования: \n" +
            "успешный запрос возвращает ok: true")
    public void testDeleteCourierSuccess() {

        // Логинимся и получаем id курьера
        Response loginResponse = loginCourier(courier);
        int courierId = getIdCourier(loginResponse);

        // Удаляемся и проверяемся
        Response response = deleteCourier(courierId);

        checkStatusCode(response, 200);
        checkOkField(response, true);

    }

    @Test
    @DisplayName("При попытке удаления курьера без id появляется ошибка 400")
    @Description("Покрытие требований: \n" +
            "если отправить запрос без id, вернётся ошибка, \n" +
            "неуспешный запрос возвращает соответствующую ошибку")
    @Issue("BUG-3 Ошибка 404 \"Not Found.\" вместо 400")
    public void testDeleteCourierWithoutId() {

        // Отправляем запрос на ручку без параметра id
        Response response = deleteCourierWithoutId();

        checkStatusCode(response, 400);
        checkErrorMessage(response, "Недостаточно данных для удаления курьера");
    }

    @Test
    @DisplayName("При попытке повторного удаления курьера появляется ошибка 404")
    @Description("Покрытие требований: \n" +
            "если отправить запрос с несуществующим id, вернётся ошибка, \n" +
            "неуспешный запрос возвращает соответствующую ошибку")
    @Issue("BUG-4 Лишняя точка в конце текста ошибки")
    public void testDeleteCourierAlreadyDeleted() {

        // Логинимся, получаем id этого курьера
        Response loginResponse = loginCourier(courier);
        int courierId = getIdCourier(loginResponse);

        // Первое удаление — должно быть успешным
        Response firstDeleteResponse = deleteCourier(courierId);
        checkStatusCode(firstDeleteResponse, 200);
        checkOkField(firstDeleteResponse, true);

        // Попытка удалить того же курьера второй раз
        Response secondDeleteResponse = deleteCourier(courierId);

        checkStatusCode(secondDeleteResponse, 404);
        checkErrorMessage(secondDeleteResponse, "Курьера с таким id нет");
    }
}
