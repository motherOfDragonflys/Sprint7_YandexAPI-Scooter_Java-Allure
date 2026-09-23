package data;

import model.OrderModel;
import java.util.List;

public class OrderData {

//__РУЧКИ_______________________________________________________________________________________________________________

    public static final String CREATE_ORDER = "/api/v1/orders";
    public static final String GET_ORDER = "/api/v1/orders";
    public static final String GET_ORDER_BY_TRACK = "/api/v1/orders/track";
    public static final String ACCEPT_ORDER = "/api/v1/orders/accept/";

//__МЕТОДЫ______________________________________________________________________________________________________________

    // Без цвета
    public static OrderModel getBaseOrder() {
        return new OrderModel(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                "4",
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                null // Цвет не указан
        );
    }

    // Заказ с одним цветом (BLACK)
    public static OrderModel getOrderWithBlackColor() {
        return new OrderModel(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                "4",
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                List.of("BLACK")
        );
    }

    // Заказ с одним цветом (GREY)
    public static OrderModel getOrderWithGreyColor() {
        return new OrderModel(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                "4",
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                List.of("GREY")
        );
    }

    // Заказ с двумя цветами (BLACK, GREY)
    public static OrderModel getOrderWithTwoColors() {
        return new OrderModel(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                "4",
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                List.of("BLACK", "GREY")
        );
    }
}
