package order;

import base.BaseApiTest;

import io.qameta.allure.Epic;
import io.restassured.response.Response;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static steps.CommonSteps.*;
import static steps.OrderSteps.*;

@Epic("Курьер + заказ")
@Feature("Получение списка заказов")
public class GetOrdersListTest extends BaseApiTest {

    @Test
    @DisplayName("Успешное получение списка заказов без courierId")
    public void testGetOrdersListReturnsList() {

        Response response = getOrdersList(null);

        checkStatusCode(response, 200);
        checkOrdersListPresent(response);
    }

    @Test
    @DisplayName("Появление ошибки 404 при запросе списка для несуществующего курьера")
    public void testGetOrdersListForNonExistentCourier() {

        int courierId = 999999999;
        Response response = getOrdersList(courierId);

        checkStatusCode(response, 404);
        checkErrorMessage(response, "Курьер с идентификатором " + courierId + " не найден");
    }
}
