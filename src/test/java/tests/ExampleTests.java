package tests;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.path.json.JsonPath;
import objects.Car;
import objects.Slot;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import properties.OAuth2Filter;
import properties.Settings;
import properties.SpecificationRequest;
import properties.SpecificationResponse;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

/*
 * Garage has only 150 parking spaces
 */

public class ExampleTests extends Settings {

    // way to configure RestAssured
    @BeforeMethod
    public void configureRestAssured() {
        RestAssured.baseURI = "http://cookiemonster.com";
        RestAssured.requestSpecification = given().header("Language", "en");
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    SpecificationRequest req;
    SpecificationResponse res;

    @Test
    public void example() {
        req = new SpecificationRequest(baseUri, "header", "test");
        req.checkStatusCode("test", 200);
    }

    @Test
    public void example1() {
        res = new SpecificationResponse(200);
        res.response("test");
    }

//----------------------------------- Filter --------------------------------------------------
/*
Порядок фильтров имеет значение.
Эти два запроса приведут к разным логам: в первом будет указан авторизационный заголовок, во втором — нет.
При этом заголовок будет добавлен в оба запроса — просто в первом случае REST Assured сначала добавит авторизацию до того, как залогировать,
а во втором — наоборот.
*/

    @Test
    public void filterTest() {
        OAuth2Filter auth = new OAuth2Filter("accessToken");

        given()
                .filter(auth)
                .filter(new RequestLoggingFilter());
                // TODO add her steps

        given()
                .filter(new RequestLoggingFilter())
                .filter(auth);
                // TODO add her steps
    }

//----------------------------------- Test GET and responses -----------------------------------

    @Test
    public void basicPingTest() {
        given().when().get("/garage")
                .then().statusCode(200); // Check that path is available
    }

    @Test
    public void basicCheck() { // full test looks like this
        given()
                .baseUri("http://cookiemonster.com")
                .when()
                .get("/cookies")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void invalidParkingSpace() {
        given()
                .when()
                .get("/garage/slots/999") // Check that service return 404 code because of 999 slot doesn't exists
                .then()
                .statusCode(404);
    }

    @Test
    public void verifyNameOfGarage() {
        given().when().get("/garage")
                .then()
                .body(containsString("Acme garage")); // check that body contains string
        // Here is uses Hamcrest matchers
    }

    @Test
    public void verifyNameStructured() {
        given().when().get("/garage")
                .then()
                .body("name", equalTo("Acme garage")); // check that name inside JSON has a specific name
    }

    @Test
    public void verifySlotsOfGarage() {
        given().when().get("/garage")
                .then()
                .body("info.slots", equalTo(150))        // check that JSON info.slots = 150
                .body("info.status", equalTo("open"));   // check that JSON info.status = open
    }

    @Test
    public void verifyTopLevelURL() {
        given().when().get("/garage")
                .then()
                .body("name", equalTo("Acme garage"))    // check that
                .body("info.slots", equalTo(150))        // check that
                .body("info.status", equalTo("open"))    // check that
                .statusCode(200);           // if the previous tests is OK - this test will be have done
    }

/*
------------------------------------------------- Test POST ----------------------------------
*/

    @Test(description = "POST - add new object to the list")
    public void aCarGoesIntoTheGarage() {
        Map<String, String> car = new HashMap<>(); // use Map for build a body
        car.put("plateNumber", "xyx1111");
        car.put("brand", "audi");
        car.put("colour", "red");

        given()
                .contentType("application/json")        // JSON or XML
                .body(car)                              // create body from map
                .when().post("/garage/slots").then() // Send POST to ...
                .statusCode(200);                       // check response
    }

    @Test(description = "here is used Map")
    public void aCarGoesIntoTheGarageStructured() {
        Map<String, String> car = new HashMap<>();
        car.put("plateNumber", "xyx1111");
        car.put("brand", "audi");
        car.put("colour", "red");

        given()
                .contentType("application/json")
                .body(car)
                .when().post("/garage/slots").then()
                .body("empty", equalTo(false)) // check that garage is not empty
                .body("position", lessThan(150)); // check that position less than 150
    }

    @Test(description = "Here is used Class Car as object to add into body")
    public void aCarObjectGoesIntoTheGarage() {
        Car car = new Car();
        car.setPlateNumber("xyx1111");
        car.setBrand("audi");
        car.setColour("red");

        given()
                .contentType("application/json")
                .body(car)
                .when().post("/garage/slots").then()
                .body("empty", equalTo(false))
                .body("position", lessThan(150));
    }

    @Test(description = "Here are used Junit assertions like as in the previous test")
    public void aCarIsRegisteredInTheGarage() {
        Car car = new Car();
        car.setPlateNumber("xyx1111");
        car.setBrand("audi");
        car.setColour("red");

        Slot slot = given()
                .contentType("application/json")
                .body(car)
                .when().post("/garage/slots")
                .as(Slot.class);

        assertFalse(slot.isEmpty());
        assertTrue(slot.getPosition() < 150);
    }

//---------------------------------------- DELETE -----------------------------------------------------

    @Test
    public void aCarLeaves() {
        given().pathParam("slotID", 27)
                .when().delete("/garage/slots/{slotID}") // Delete slot with number
                .then().statusCode(200);
    }

//-----------------------------------------------------------------------------------------------------

    @Test
    public void aCarEntersAndThenLeaves() {
        Car car = new Car();
        car.setPlateNumber("xyx1111");
        car.setBrand("audi");
        car.setColour("red");

        int positionTakenInGarage = given()
                .contentType("application/json")
                .body(car)
                .when().post("/garage/slots").then()    // add new slot
                .body("empty", equalTo(false))  // check that garage is not empty
                .extract().path("position");            // extract position

        given().pathParam("slotID", positionTakenInGarage) // set parameters
                .when().delete("/garage/slots/{slotID}").then() // delete the car
                .statusCode(200);                               // check status
    }

//-------------------------------------- TEST------------------------------------

    /*
    Этот тест проверяет, что, когда мы кормим Куки-монстра, мы правильно подсчитываем, сколько печенек ему дали,
    и указываем это в истории. Но с первого взгляда это нельзя понять — все запросы выглядят одинаково, и неясно,
    где заканчивается подготовка данных через API, а где посылается тестируемый запрос.
     */

    @Test
    public void shouldCorrectlyCountAddedCookies() {
        Integer addNumber = 10;

        JsonPath beforeCookies = given()
                .when()
                .get("/latestcookies")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath();

        String beforeId = beforeCookies.getString("id");

        JsonPath afterCookies = given()
                .body(String.format("{number: %s}", addNumber))
                .when()
                .put("/cookies")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath();

        Integer afterNumber = afterCookies.getInt("number");
        String afterId = afterCookies.getString("id");
        JsonPath history = given()
                .when()
                .get("/history")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath();

//        assertThat(history.getInt(String.format("records.find{r -> r.id == %s}.number", beforeId)))
//                .isEqualTo(afterNumber - addNumber);
//        assertThat(history.getInt(String.format("records.find{r -> r.id == %s}.number", afterId)))
//                .isEqualTo(afterNumber);
    }
}
