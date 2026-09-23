package order;

import base.BaseOrderTest;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.restassured.response.Response;

import model.CourierModel;
import static steps.CommonSteps.*;
import static steps.OrderSteps.*;
import static data.CourierData.*;
import static steps.CouriersSteps.*;

/*  Логика принятия заказов
    - В Before BaseOrderTest:
      создается стандартный заказ из OrderData,
      validTrackId записывает значение track
  В местном Before:
    - Берем номер заказа через его трек
    - Создается курьер:
      сгенерированы рандом данные
      курьер логинится
      берем id курьера в validCourierId
*/

@Epic("Курьер + заказ")
@Feature("Принятие заказа курьером")
public class AcceptOrderTest extends BaseOrderTest {

    protected int validCourierId;
    protected int createdOrderId;

    @Before
    @Override
    @Step("Создание и логин курьера")
    public void setUpOrder() {
        super.setUpOrder();

        // Получаем id заказа
        Response orderResponse = getOrderByTrack(validTrackId);
        checkStatusCode(orderResponse, 200);
        this.createdOrderId = orderResponse.path("order.id");

        // Создаем курьера, чтобы был точный существующий id
        CourierModel newCourier = randomCourier();
        Response courierResponse = createCourier(newCourier);
        checkStatusCode(courierResponse, 201);

        // Логинимся, чтобы получить id курьера
        Response loginResponse = loginCourier(newCourier);
        checkStatusCode(loginResponse, 200);
        this.validCourierId = loginResponse.path("id");

    }

    //Данный After на удаление курьера применим только для этих тестов, не уносим в base
    @After
    @Step("Удаление тестового курьера")
    public void tearDown() {
        if (validCourierId != 0) {
            Response response = deleteCourier(validCourierId);
            checkStatusCode(response, 200);
        }
    }

//__ТЕСТЫ_______________________________________________________________________________________________________________

    @Test
    @DisplayName("Успешное принятие заказа")
    public void testAcceptOrderSuccess() {

        Response response = acceptOrder(createdOrderId, validCourierId);

        checkStatusCode(response, 200);
        checkOkField(response, true);
    }

    @Test
    @DisplayName("Появляется ошибка 400 при отсутствии id курьера")
    public void testAcceptOrderWithoutCourierId() {

        Response response = acceptOrderWithoutCourierId(null);

        checkStatusCode(response, 400);
        checkErrorMessage(response, "Недостаточно данных для поиска");
    }

    @Test
    @DisplayName("Появляется ошибка 404 при передаче несуществующего id курьера")
    public void testAcceptOrderInvalidCourierId() {

        Response response = acceptOrder(createdOrderId, 999999);

        checkStatusCode(response, 404);
        checkErrorMessage(response, "Курьера с таким id не существует");
    }

    @Test
    @DisplayName("Появляется ошибка 400 при отсутствии id заказа")
    @Issue("BUG-5 Ошибка 404 \"Not Found.\" вместо 400")
    public void testAcceptOrderWithoutOrderId() {

        Response response = acceptOrderWithoutOrderId(validCourierId);

        checkStatusCode(response, 400);
        checkErrorMessage(response, "Недостаточно данных для поиска");
    }

    @Test
    @DisplayName("Появляется ошибка 404 при передаче несуществующего id заказа")
    public void testAcceptOrderInvalidOrderId() {
        Response response = acceptOrder(999999, validCourierId);

        checkStatusCode(response, 404);
        checkErrorMessage(response, "Заказа с таким id не существует");
    }
}
