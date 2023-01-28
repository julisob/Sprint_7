import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class Client extends Courier {
    private static final String POST_CREATE_COURIER = "/api/v1/courier";
    private static final String POST_LOGIN_COURIER = "/api/v1/courier/login";
    private static final String DELETE_COURIER = "/api/v1/courier/";
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    public Response postCreateCourier (Courier courier) {
        RestAssured.baseURI = BASE_URL;
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(POST_CREATE_COURIER);
    }

    public Response postLoginCourier(Courier courier) {
        RestAssured.baseURI = BASE_URL;
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(POST_LOGIN_COURIER);
    }

    public void deleteCourier(String id) {
        RestAssured.baseURI = BASE_URL;
        given()
                .delete(DELETE_COURIER + id);
    }
}




