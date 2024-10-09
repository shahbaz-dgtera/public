package sa.com.neoleap.quarkus;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/hello")
public class Greeting {

    @GET
    public String hello() {
        return "hello";
    }
}