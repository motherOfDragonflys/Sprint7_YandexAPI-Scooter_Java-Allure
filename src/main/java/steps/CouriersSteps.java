package steps;

import io.qameta.allure.Step;

import io.restassured.response.Response;
import static org.hamcrest.CoreMatchers.notNullValue;

import model.CourierModel;
import static data.CourierData.*;
import static steps.CommonSteps.baseRequest;

public class CouriersSteps {

    @Step("Отправка POST запроса создания курьера на ручку " + CREATE_COURIER )
    public static Response createCourier (CourierModel courier) {
        return baseRequest()
                .body(courier)
                .when()
                .post(CREATE_COURIER)
                .then()
                .extract().response();
    }

    @Step("Отправка POST запроса логина курьера на ручку " + LOGIN_COURIER)
    public static Response loginCourier (CourierModel courier) {
        return baseRequest()
                .body(courier)
                .when()
                .post(LOGIN_COURIER)
                .then()
                .extract().response();
    }

    @Step("Отправка DELETE запроса удаления курьера по id на ручку "+ DELETE_COURIER + "{courierId}")
    public static Response deleteCourier(int courierId) {
        return baseRequest()
                .when()
                .delete(DELETE_COURIER + courierId)
                .then()
                .extract().response();
    }

    @Step("Отправка DELETE запроса удаления курьера без id на ручку " + DELETE_COURIER)
    public static Response deleteCourierWithoutId() {
        return baseRequest()
                .when()
                .delete(DELETE_COURIER)
                .then()
                .extract().response();
    }

    // Для респонза логина курьера
    @Step("Проверка появления поля 'id' в теле ответа")
    public static void checkIdIsNotNull(Response response) {
        response
                .then()
                .body("id", notNullValue());
    }
    @Step("Получение 'id' курьера")
    public static int getIdCourier (Response response) {
        return response
                .then()
                .extract()
                .path("id");
    }
}
