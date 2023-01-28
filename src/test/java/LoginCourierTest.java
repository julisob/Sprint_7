import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.core.IsEqual.equalTo;

public class LoginCourierTest extends Client {
    private Courier courier;
    private Response response;

    @Before
    public void setUp() {
        courier = new Courier();
    }

    @Description("Проверяет, что курьер залогинен, и в теле ответа есть поле id")
    @Test
    public void loginNewCourier() {
        courier.setLogin("fastercourier12");
        courier.setPassword("1234");
        postCreateCourier(courier);
        response = postLoginCourier(courier);
        response.then().assertThat().body(anything("id"))
                .and()
                .statusCode(200);
    }

    @Description("Проверяет, что курьер с неправильным логином не залогинен, и приходит сообщение об ошибке")
    @Test
    public void loginNewCourierFalseLogin() {
        courier.setLogin("fastercourier123");
        courier.setPassword("1234");
        response = postLoginCourier(courier);
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Description("Проверяет, что курьер без обязательного поля не залогинен, и приходит сообщение об ошибке")
    @Test
    public void loginNewCourierNoLogin() {
        courier.setPassword("1234");
        response = postLoginCourier(courier);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }
}
