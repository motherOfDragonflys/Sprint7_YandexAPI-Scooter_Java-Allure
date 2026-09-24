package base;

import io.restassured.response.Response;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Before;

import model.CourierModel;
import static data.CourierData.*;
import static steps.CouriersSteps.*;

/*  Логика курьеров
    - Генерируется курьер originalCourier с рандом данными
    - Данные отправляются на создание курьера
    - Сохраняется экземпляр созданного курьера в originalCourier, для удаления его в After
    - Работа в тестах происходит с курьером courier
    - After удаляет и originalCourier, который создавался в Before,
    курьера courier, если он есть и отличается от originalCourier
*/

public class BaseCourierTest extends BaseApiTest {

    protected CourierModel courier;
    protected CourierModel originalCourier;

//__Создание курьера____________________________________________________________________________________________________
    @Before
    public void setUpCourier() {
        // По умолчанию создаем валидного курьера для каждого теста с курьером
        courier = randomCourier();
        createCourier(courier);

        rememberForCleanup(courier);
    }

    // сохраняем исходного курьера, менять будем у courier
    protected void rememberIfCreated(Response response, CourierModel c) {
        if (response.statusCode() == 201) {
            rememberForCleanup(c);
        } else {
            // Если курьер не создан — originalCourier остаётся null, After ничего не сделает
            originalCourier = null;
        }
    }
    protected void rememberForCleanup(CourierModel c) {
        originalCourier = new CourierModel(
                c.getLogin(),
                c.getPassword(),
                c.getFirstName()
        );
    }

//__Удаление курьера____________________________________________________________________________________________________
    @After
    @Step("Удаление тестового курьера")
    public void tearDown() {

        deleteOriginal(originalCourier);

        // Подстрахуемся, если courier менялся, его тоже удалим, вызвав шаг deleteSafety
        if (courier != null && !courier.equals(originalCourier)) {
            deleteSafety(courier);
        }
    }

    // Разделены для понятных шагов в отчете
    @Step("Удаление оригинального курьера")
    private void deleteOriginal(CourierModel c) {
        executeDelete(c);
    }

    @Step("Страховочное удаление измененного курьера")
    private void deleteSafety(CourierModel c) {
        executeDelete(c);
    }

    // Удаление измененных курьеров
    protected void executeDelete(CourierModel c) {
        // Если курьера нет или у него нет логина/пароля, удалять нечего
        if (c == null || c.getLogin() == null || c.getPassword() == null) return;
        try {
            Response loginResponse = loginCourier(c);
            if (loginResponse.statusCode() == 200) {
                deleteCourier(getIdCourier(loginResponse));
            }
        } catch (Exception e) {
            Allure.addAttachment("Ошибка очистки", e.toString());
        }
    }
}
