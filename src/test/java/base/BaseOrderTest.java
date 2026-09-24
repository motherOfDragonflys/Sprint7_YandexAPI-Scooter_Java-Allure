package base;

import org.junit.Before;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import model.OrderModel;
import static data.OrderData.*;
import static steps.CommonSteps.*;
import static steps.OrderSteps.*;

public class BaseOrderTest extends BaseApiTest {

    protected int validTrackId;

    @Before
    @Step("Создание заказа")
    public void setUpOrder() {

        OrderModel order = getBaseOrder();
        Response response = createOrder(order);
        checkStatusCode(response, 201);

        this.validTrackId = getTrackId(response);
    }
}
