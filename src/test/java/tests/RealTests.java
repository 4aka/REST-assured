package tests;

import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;
import properties.BlogDTO;
import properties.SpecificationRequest;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class RealTests {
    SpecificationRequest spec = new SpecificationRequest();

    @Test
    public void createBlogAndCheckExistence() {
        BlogDTO newBlog = createDummyBlog();
        String blogResourceLocation = createResource("blogs", newBlog);
        BlogDTO retrievedBlog = getResource(blogResourceLocation, BlogDTO.class);
        assertEqualBlog(newBlog, retrievedBlog);
    }

    private BlogDTO createDummyBlog() {
        return new BlogDTO()
                .setName("Example Name")
                .setDescription("Example Description")
                .setUrl("www.blogdomain.de");
    }

    //nice reusable method
    private String createResource(String path, Object bodyPayload) {
        return given()
                .spec((RequestSpecification) spec)
                .body(bodyPayload)
                .when()
                .post(path)
                .then()
                .statusCode(201)
                .extract().header("location");
    }

    //nice reusable method
    private <T> T getResource(String locationHeader, Class<T> responseClass) {
        return given()
                .spec((RequestSpecification) spec)
                .when()
                .get(locationHeader)
                .then()
                .statusCode(200)
                .extract().as(responseClass);
    }

    private void assertEqualBlog(BlogDTO newBlog, BlogDTO retrievedBlog) {
        assertThat(retrievedBlog.getName()).isEqualTo(newBlog.getName());
        assertThat(retrievedBlog.getDescription()).isEqualTo(newBlog.getDescription());
        assertThat(retrievedBlog.getUrl()).isEqualTo(newBlog.getUrl());
    }
}