package properties;

import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;

/*
� ������������ ������ ������� ��� ��������, ������� ����������� �� ������� � �������.

ResponseSpecification responseSpec = new ResponseSpecBuilder()
    .expectStatusCode(200)
    .expectBody(containsString("success"))
    .build();

// ����� ������ ���� ������������ ��� ���� �������:
RestAssured.responseSpecification = responseSpec;

// ��� ��� ����������:
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
