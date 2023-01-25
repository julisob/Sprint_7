import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.core.IsEqual.equalTo;

public class CreateCourierTest {
    private Courier courier;
    private Response response;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        courier = new Courier("roadtosuccess5", "1234");
    }

    @Step("Создать курьера")
    public Response postCreateCourier(Courier courier) {
        response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        return response;
    }

    @Description("Создание курьера с обязательными полями")
    @Test
    public void createNewCourier() {
        postCreateCourier(courier);
        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
    }

    @Description("Создание курьера с логином, который уже есть")
    @Test
    public void createDoubleCourier() {
        postCreateCourier(courier);
        postCreateCourier(courier);
        response.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }

    @Test
    @Description("Проверяет, что нельзя создать курьера, если одно из обязательных полей пустое")
    public void checkCourierCanNotBeCreatedWEmptyField() {
        courier.setPassword("");
        response = postCreateCourier(courier);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Test
    @Description("Проверяет, что нельзя создать курьера, если одно из обязательных полей отсутсвует")
    public void checkCourierCanNotBeCreatedWNoField() {
        Courier wrongCourier = new Courier();
        wrongCourier.setLogin("w10n6c0ur1er");
        response = postCreateCourier(wrongCourier);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @After
    public void cleanUp() {
        deleteCourier(courier);
    }

    @Step("Удалить курьера")
    public void deleteCourier(Courier courier) {
        response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
        String id = response.jsonPath().getString("id");

        given()
                .delete("/api/v1/courier/" + id);
    }
}
