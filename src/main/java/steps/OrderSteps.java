package steps;

import java.util.List;
import io.restassured.response.Response;
import io.qameta.allure.Step;
import io.restassured.specification.RequestSpecification;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.notNullValue;

import model.OrderModel;
import static data.OrderData.*;
import static steps.CommonSteps.baseRequest;


public class OrderSteps {

    @Step("Отправка POST запроса создания заказа " + CREATE_ORDER)
    public static Response createOrder(OrderModel body) {
        return baseRequest()
                .body(body)
                .when()
                .post(CREATE_ORDER)
                .then()
                .extract().response();
    }

    @Step("Отправка PUT запроса принятия заказа " + ACCEPT_ORDER)
    public static Response acceptOrder(int orderId, int courierId) {
        return baseRequest()
                .queryParam("courierId", courierId)
                .when()
                .put(ACCEPT_ORDER + orderId)
                .then()
                .extract().response();
    }

    @Step("Отправка GET запроса получения списка заказов " + GET_ORDER)
    public static Response getOrdersList(Integer courierId) {

        RequestSpecification request = baseRequest();

        if (courierId != null) {
            request.queryParam("courierId", courierId);
        }

        return request
                .when()
                .get(GET_ORDER)
                .then()
                .extract().response();
    }

    @Step("Отправка PUT запроса принятия заказа без номера заказа " + ACCEPT_ORDER)
    public static Response acceptOrderWithoutOrderId(Integer courierId) {

        RequestSpecification request = baseRequest();

        if (courierId != null) {
            request.queryParam("courierId", courierId);
        }

        return request
                .when()
                .put(ACCEPT_ORDER)
                .then()
                .extract().response();
    }

    @Step("Отправка PUT запроса принятия заказа без номера курьера " + ACCEPT_ORDER)
    public static Response acceptOrderWithoutCourierId(Integer orderId) {

        RequestSpecification request = baseRequest();

        return request
                .when()
                .put(ACCEPT_ORDER + orderId)
                .then()
                .extract().response();
    }

    @Step("Отправка GET запроса получения заказа по трек-номеру (track={trackNumber})")
    public static Response getOrderByTrack(Integer trackNumber) {

        RequestSpecification request = baseRequest();

        // Передаем параметр 't' только если он не null (для теста "без номера")
        if (trackNumber != null) {
            request.queryParam("t", trackNumber);
        }

        return request
                .when()
                .get(GET_ORDER_BY_TRACK)
                .then()
                .extract().response();
    }


    // Для получения заказа по номеру
    @Step("Проверка, что в ответе order не пустой")
    public static void checkOrderObjectPresent(Response response) {
        // Проверяем, что есть корневой объект "order"
        response
                .then()
                .body("order", notNullValue());
    }

    // Для получения списка заказов
    @Step("Проверка, что в ответе есть заказы в виде списка")
    public static void checkOrdersListPresent(Response response) {

        Object orders = response.path("orders");

        assertNotNull("Поле 'orders' отсутствует в ответе", orders);
        assertTrue("Поле 'orders' должно быть списком", orders instanceof List);
    }

    // Для создания заказа
    @Step("Проверка появления поля track в теле ответа")
    public static void checkTrackIsNotNull(Response response) {
        response
                .then()
                .body("track", notNullValue());
    }

    @Step("Получение номера трека из ответа")
    public static int getTrackId(Response response) {

        return response
                .then()
                .extract().path("track");
    }
}


