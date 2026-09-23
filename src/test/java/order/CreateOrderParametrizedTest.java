package order;

import base.BaseApiTest;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;
import io.restassured.response.Response;

import model.OrderModel;
import static steps.OrderSteps.*;
import static steps.CommonSteps.*;
import static data.OrderData.*;

@Epic("Заказы")
@Feature("Создание заказа с разными вариантами цвета самоката")
@RunWith(Parameterized.class)
public class CreateOrderParametrizedTest extends BaseApiTest {

    private final OrderModel order;

    @SuppressWarnings("unused") //testName для названий тестовых прогонов
    public CreateOrderParametrizedTest(OrderModel order, String testName) {
        this.order = order;
    }

    @Parameterized.Parameters(name = "{1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                { getBaseOrder(), "Создание заказа без цвета" },
                { getOrderWithBlackColor(), "Создание заказа с цветом BLACK" },
                { getOrderWithGreyColor(), "Создание заказа с цветом GREY" },
                { getOrderWithTwoColors(), "Создание заказа с цветами BLACK и GREY" }
        });
    }

    @Test
    @Description("Покрытие требований: \n" +
            "можно указать один из цветов — BLACK или GREY, \n" +
            "можно указать оба цвета, \n" +
            "можно совсем не указывать цвет, \n" +
            "тело ответа содержит track")
    public void testCreateOrderWithDifferentColors() {

        Response response = createOrder(order);

        checkStatusCode(response, 201);
        checkTrackIsNotNull(response);
    }
}
