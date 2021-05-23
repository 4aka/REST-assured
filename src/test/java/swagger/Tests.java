package swagger;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.AssertJUnit.assertEquals;

public class Tests extends Properties {

    @BeforeClass
    public void login() {
        baseURI = "https://petstore.swagger.io/v2/user";

        given().auth().basic("test", "abc123")
                .when().get("https://petstore.swagger.io/oauth/authorize")
                .then().statusCode(200);
    }

    @Test(priority = 496)
    public void createUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("Di");
        user.setFirstName("Di");
        user.setLastName("Arch");
        user.setEmail("123@mail.com");
        user.setPassword("123456");
        user.setPhone("0991234567");
        user.setUserStatus(1);

        given()
                .accept("application/json")
                .contentType("application/json")
                .body(user)
                .when().post(baseURI)
                .then().statusCode(200);

        assertEquals(1, user.getId());
        assertEquals("Di", user.getUsername());
    }

    @Test(priority = 497)
    public void userLogin() {
        given()
                .accept("application/json")
                .when().get(baseURI + "/login?username=Di&password=123456")
                .then().statusCode(200);
    }

    @Test(priority = 498)
    public void userLogout() {
        given()
                .accept("application/json")
                .when().get(baseURI + "/logout")
                .then().statusCode(200);
    }

    @Test(priority = 499)
    public void getUser() {
        given()
                .accept("application/json")
                .when().get(baseURI + "/Di")
                .then()
                .body("username", equalTo("Di"))
                .body("password", equalTo("123456"));
    }

    @Test(priority = 500)
    public void updateUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("Di");
        user.setFirstName("Di");
        user.setLastName("Arch");
        user.setEmail("123@mail.com");
        user.setPassword("123456");
        user.setPhone("0991234567");
        user.setUserStatus(2);

        given()
                .accept("application/json")
                .contentType("application/json")
                .body(user)
                .when().put(baseURI + "/Di")
                .then().statusCode(200);

        assertEquals(1, user.getId());
        assertEquals(2, user.getUserStatus());
    }

    @AfterClass
    public void deleteUser() {
        given()
                .accept("application/json")
                .when().delete(baseURI + "/Di")
                .then()
                .statusCode(200);
    }
}
