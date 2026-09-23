package order;

import base.BaseOrderTest;

import io.qameta.allure.Epic;
import io.restassured.response.Response;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Feature;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static steps.CommonSteps.*;
import static steps.OrderSteps.*;

@Epic("Заказы")
@Feature("Получение заказа по его номеру")
public class GetOrderByIdTest extends BaseOrderTest {

    @Test
    @DisplayName("Получение заказа по номеру заказа возвращает объект с заказом")
    public void testGetOrderByTrackSuccess() {
        // id создан в Before (validTrackId)
        Response response = getOrderByTrack(validTrackId);

        checkStatusCode(response, 200);
        // Проверяем, что есть корневой объект "order"
        checkOrderObjectPresent(response);

        // Проверка, что id заказа в ответе есть
        int returnedId = response.path("order.id");
        assertTrue("id заказа должен быть положительным числом", returnedId > 0);

        // Проверка, что трек в ответе совпадает с запрошенным
        int returnedTrack = response.path("order.track");
        assertEquals("Трек искомого заказа в ответе не совпадает с созданным",
                validTrackId, returnedTrack);
    }

    @Test
    @DisplayName("Запрос без номера заказа возвращает ошибку 400")
    public void testGetOrderByTrackMissingTrack() {

        Response response = getOrderByTrack(null);

        checkStatusCode(response, 400);
        checkErrorMessage(response, "Недостаточно данных для поиска");

    }

    @Test
    @DisplayName("Запрос с несуществующим заказом возвращает ошибку 404")
    public void testGetOrderByTrackNonExistentTrack() {
        // Берем заведомо несуществующий трек
        int nonExistentTrack = 0;

        Response response = getOrderByTrack(nonExistentTrack);

        checkStatusCode(response, 404);
        checkErrorMessage(response, "Заказ не найден");
    }
}
