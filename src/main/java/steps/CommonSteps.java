package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.equalTo;

public class CommonSteps {

    @Step("Проверка статуса ответа")
    public static void checkStatusCode(Response response, int expectedStatusCode) {
        response
                .then()
                .statusCode(expectedStatusCode);
    }

    @Step("Проверка сообщения об ошибке")
    public static void checkErrorMessage(Response response, String expectedMessage) {
        response
                .then()
                .body("message", equalTo(expectedMessage));
    }

    @Step("Проверка в теле ответа 'ok': {expectedValue}")
    public static void checkOkField(Response response, boolean expectedValue) {
        response
                .then()
                .body("ok", equalTo(expectedValue));
    }

    protected static RequestSpecification baseRequest() {
        return given().contentType(JSON);
    }
}
