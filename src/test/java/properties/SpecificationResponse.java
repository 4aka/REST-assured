package properties;

import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;

/*
¬ спецификацию ответа выносим все проверки, которые дублируютс€ от запроса к запросу.

ResponseSpecification responseSpec = new ResponseSpecBuilder()
    .expectStatusCode(200)
    .expectBody(containsString("success"))
    .build();

// можно задать одну спецификацию дл€ всех ответов:
RestAssured.responseSpecification = responseSpec;

// или дл€ отдельного:
given()...when().get(someEndpoint).then().spec(responseSpec)...;
 */

public class SpecificationResponse {

    private int code;

    /**
     * @param code set Object
     */
    public SpecificationResponse(int code) {
        this.code = code;
    }

    private final ResponseSpecification responseSpec =
            expect()
                    .statusCode(code);

    /**
     * @param get set endpoint
     */
    public void response(String get) {
        given()
                .expect()
                .spec(responseSpec)
                .when()
                .get("/" + get);
    }
}
