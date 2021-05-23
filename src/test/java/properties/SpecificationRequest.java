package properties;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/*
RequestSpecification requestSpec = new RequestSpecBuilder()
    .setBaseUri("http://localhost")
    .setPort(8080)
    .setAccept(ContentType.JSON)
    .setContentType(ContentType.ANY)
    ...
    .log(LogDetail.ALL)
    .build();

// можно задать одну спецификацию для всех запросов:
RestAssured.requestSpecification = requestSpec;

// или для отдельного:
given().spec(requestSpec)...when().get(someEndpoint);
 */

public class SpecificationRequest {

    private String baseUri;
    private String header;
    private Object o;

    /**
     * @param baseUri set base Uri
     * @param header  set header
     * @param o       set Object
     */
    public SpecificationRequest(String baseUri, String header, Object o) {
        this.baseUri = baseUri;
        this.header = header;
        this.o = o;
    }

    public SpecificationRequest() {
    }

    private final RequestSpecification requestSpec =
            given()
                    .baseUri(baseUri)
                    .header(header, o);

    /**
     * @param get  set endpoint
     * @param code set status code
     */
    public void checkStatusCode(String get, int code) {
        requestSpec.when()
                .get("/" + get)
                .then()
                .statusCode(code);
    }
}
