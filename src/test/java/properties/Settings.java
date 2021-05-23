package properties;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public class Settings {

    public static String port = System.getProperty("server.port");
    public static String baseUri = System.getProperty("server.base");

    @BeforeClass
    public static void beforeClass() {

        if (port == null) {
            RestAssured.port = 8080;
        } else {
            RestAssured.port = Integer.parseInt(port);
        }

        if (baseUri == null) {
            baseUri = "/rest-garage-sample/";
        }
        RestAssured.basePath = baseUri;

        String baseHost = System.getProperty("server.host");
        if (baseHost == null) {
            baseHost = "http://localhost";
        }
        RestAssured.baseURI = baseHost;
    }
}
