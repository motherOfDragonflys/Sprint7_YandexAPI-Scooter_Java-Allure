package data;

import com.github.javafaker.Faker;
import model.CourierModel;

public class CourierData {

    static Faker user = new Faker();

//__РУЧКИ_______________________________________________________________________________________________________________

    // Создание курьера
    public static final String CREATE_COURIER = "/api/v1/courier";
    // Логин курьера в системе
    public static final String LOGIN_COURIER = "/api/v1/courier/login";
    // Удаление курьера /api/v1/courier/:id
    public static final String DELETE_COURIER = "/api/v1/courier/";

//__МЕТОДЫ______________________________________________________________________________________________________________

    // Для создания целого курьера
    public static CourierModel randomCourier() {
        // Генерим уникальные данные для логина
        String login = user.name().lastName() + System.currentTimeMillis();
        // Поле пароль - генерим из 4х цифр диапазаона 0-9
        String password = user.regexify("[0-9]{4}");
        // Имя (пример реулярных выражений с кириллицей)
        String firstName = user.regexify("[а-я]{5}");

        return new CourierModel(login, password, firstName);
    }

    // Генерит новый пароль для негативных тестов
    public static String getDifferentPassword(String currentPassword) {
        String newPassword = "4444";

        // Защита от редкого совпадения (паранойя, всего 10к комбинаций же...)
        while (newPassword.equals(currentPassword)) {
            newPassword = user.regexify("[0-9]{4}");
        }
        return newPassword;
    }

    // Генерит новый логин для негативных тестов
    public static String getDifferentLogin(String currentLogin) {
        return currentLogin + System.currentTimeMillis();
    }

}
