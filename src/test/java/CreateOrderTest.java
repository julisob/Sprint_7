import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final int metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;
    private Order order;
    private Response response;

    public CreateOrderTest(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] fillOrder() {
        return new Object[][]{
                {"Иван", "Петров", "Москва, пр. Автомобильный дом 74 квартира 12", 5, "89001234567", 1, "2023-03-03", "комментарий", new String[]{""}},
                {"Иван", "Петров", "Москва, пр. Автомобильный дом 74 квартира 12", 5, "89001234567", 1, "2023-03-03", "комментарий", new String[]{"BLACK"}},
                {"Иван", "Петров", "Москва, пр. Автомобильный дом 74 квартира 12", 5, "89001234567", 1, "2023-03-03", "комментарий", new String[]{"BLACK", "GREY"}},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
    }

    @Step("Создать заказ")
    public void postCreateOrder(Order order) {
        response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("/api/v1/orders");
    }

    @Test
    @Description("Проверяет, что заказ создан, и в теле ответа есть поле track")
    public void checkOrderStatusIsCreated() {
        postCreateOrder(order);
        response.then().assertThat().body(anything("track"))
                .and()
                .statusCode(201);
    }

    @After
    @Description("Удаляет заказ")
    public void cleanUp() {
        int track = order.getTrack();
        given()
                .delete("/api/v1/orders/cancel?track=" + track);
    }
}
