import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.core.IsEqual.equalTo;

public class CreateCourierTest extends Client{
    private Courier courier;
    private Response response;

    @Before
    public void setUp() {
        courier = new Courier("roadtosuccess5", "1234");
    }

    @Description("Создание курьера с обязательными полями")
    @Test
    public void createNewCourier() {
        response = postCreateCourier(courier);
        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
    }

    @Description("Создание курьера с логином, который уже есть")
    @Test
    public void createDoubleCourier() {
        postCreateCourier(courier);
        response = postCreateCourier(courier);
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
        deleteThisCourier(courier);
    }

    @Step("Удалить курьера")
    public void deleteThisCourier(Courier courier) {
        response = postLoginCourier(courier);
        String id = response.jsonPath().getString("id");
        deleteCourier(id);
    }
}
