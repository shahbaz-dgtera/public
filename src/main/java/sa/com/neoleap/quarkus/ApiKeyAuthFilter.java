package sa.com.neoleap.quarkus;

import jakarta.ws.rs.container.*;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Provider
public class ApiKeyAuthFilter implements ContainerRequestFilter {

    private static final String[] API_KEYS = {"3f2504e04f8911d3b9f2f1f7d2a1d27f", "5e64a5e8ec0e1a2a4e39c83fd9b486e1"}; // Add your API keys
    private static final List<String> keysList = Arrays.asList(API_KEYS);
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String contentType = requestContext.getHeaderString(HttpHeaders.CONTENT_TYPE);
        String apiKey = extractIdFromContentType(contentType);

        if (apiKey == null || !keysList.contains(apiKey)) {
            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                    .entity("API key is missing or invalid")
                    .build());
        }
    }

    public static String extractIdFromContentType(String contentType) {
        // Define a regex pattern to extract the ID
        String regex = "vnd\\.([0-9a-fA-F]+)\\+json";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(contentType);

        // Check if the pattern matches
        if (matcher.find()) {
            return matcher.group(1); // Return the captured group
        }
        return null; // or throw an exception, depending on your needs
    }
}