package sa.com.neoleap.quarkus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class GreetingResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .header("Content-Type", "application/vnd.3f2504e04f8911d3b9f2f1f7d2a1d27f+json")
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("hello"));
    }

    // @Test
    // public void testGreetingEndpoint() {
    //     String uuid = UUID.randomUUID().toString();
    //     given()
    //             .pathParam("name", uuid)
    //             .when().get("/hello/greeting/{name}")
    //             .then()
    //             .statusCode(200)
    //             .body(is("hello " + uuid));
    // }

}
