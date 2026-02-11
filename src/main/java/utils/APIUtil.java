package utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Map;

/**
 * REST API Utility for making HTTP requests and validating responses.
 * Built on top of REST Assured library for API testing.
 */
public class APIUtil {
    private static final Logger logger = LogManager.getLogger(APIUtil.class);
    private static ConfigReader config = new ConfigReader();
    private static final String BASE_URL = config.getApiBaseUrl();

    /**
     * Initialize RestAssured with base URL
     */
    static {
        if (BASE_URL != null && !BASE_URL.isEmpty()) {
            RestAssured.baseURI = BASE_URL;
            logger.info("RestAssured base URL set to: {}", BASE_URL);
        }
    }

    /**
     * Make GET request
     */
    public static Response get(String endpoint) {
        logger.info("GET request to: {}", endpoint);
        return RestAssured.get(endpoint);
    }

    /**
     * Make GET request with headers
     */
    public static Response get(String endpoint, Map<String, String> headers) {
        logger.info("GET request to: {} with headers", endpoint);
        RequestSpecification request = RestAssured.given().headers(headers);
        return request.get(endpoint);
    }

    /**
     * Make POST request with JSON body
     */
    public static Response post(String endpoint, Object body) {
        logger.info("POST request to: {} with body: {}", endpoint, body);
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .post(endpoint);
    }

    /**
     * Make POST request with JSON body and headers
     */
    public static Response post(String endpoint, Object body, Map<String, String> headers) {
        logger.info("POST request to: {} with headers and body", endpoint);
        return RestAssured.given()
                .headers(headers)
                .contentType(ContentType.JSON)
                .body(body)
                .post(endpoint);
    }

    /**
     * Make PUT request with JSON body
     */
    public static Response put(String endpoint, Object body) {
        logger.info("PUT request to: {} with body: {}", endpoint, body);
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .put(endpoint);
    }

    /**
     * Make PUT request with JSON body and headers
     */
    public static Response put(String endpoint, Object body, Map<String, String> headers) {
        logger.info("PUT request to: {} with headers and body", endpoint);
        return RestAssured.given()
                .headers(headers)
                .contentType(ContentType.JSON)
                .body(body)
                .put(endpoint);
    }

    /**
     * Make PATCH request with JSON body
     */
    public static Response patch(String endpoint, Object body) {
        logger.info("PATCH request to: {} with body: {}", endpoint, body);
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .patch(endpoint);
    }

    /**
     * Make PATCH request with JSON body and headers
     */
    public static Response patch(String endpoint, Object body, Map<String, String> headers) {
        logger.info("PATCH request to: {} with headers and body", endpoint);
        return RestAssured.given()
                .headers(headers)
                .contentType(ContentType.JSON)
                .body(body)
                .patch(endpoint);
    }

    /**
     * Make DELETE request
     */
    public static Response delete(String endpoint) {
        logger.info("DELETE request to: {}", endpoint);
        return RestAssured.delete(endpoint);
    }

    /**
     * Make DELETE request with headers
     */
    public static Response delete(String endpoint, Map<String, String> headers) {
        logger.info("DELETE request to: {} with headers", endpoint);
        RequestSpecification request = RestAssured.given().headers(headers);
        return request.delete(endpoint);
    }

    /**
     * Validate HTTP status code
     */
    public static boolean validateStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        logger.info("Expected status code: {}, Actual: {}", expectedStatusCode, actualStatusCode);
        return actualStatusCode == expectedStatusCode;
    }

    /**
     * Get response status code
     */
    public static int getStatusCode(Response response) {
        return response.getStatusCode();
    }

    /**
     * Get response body as string
     */
    public static String getResponseBody(Response response) {
        return response.getBody().asString();
    }

    /**
     * Get response header value
     */
    public static String getResponseHeader(Response response, String headerName) {
        return response.getHeader(headerName);
    }

    /**
     * Get response as JSON object
     */
    public static Object getResponseAsJSON(Response response, String jsonPath) {
        return response.jsonPath().get(jsonPath);
    }

    /**
     * Extract value from JSON response using JSONPath
     */
    public static String extractFromJSON(Response response, String jsonPath) {
        logger.info("Extracting value from path: {}", jsonPath);
        return response.jsonPath().getString(jsonPath);
    }

    /**
     * Validate response contains expected text
     */
    public static boolean validateResponseContains(Response response, String expectedText) {
        String responseBody = response.getBody().asString();
        logger.info("Checking if response contains: {}", expectedText);
        return responseBody.contains(expectedText);
    }

    /**
     * Validate response header exists
     */
    public static boolean validateHeaderExists(Response response, String headerName) {
        boolean headerExists = response.getHeaders().hasHeaderWithName(headerName);
        logger.info("Header {} exists: {}", headerName, headerExists);
        return headerExists;
    }

    /**
     * Get response time in milliseconds
     */
    public static long getResponseTime(Response response) {
        return response.getTime();
    }

    /**
     * Validate response time is less than specified milliseconds
     */
    public static boolean validateResponseTime(Response response, long maxTimeMs) {
        long responseTime = getResponseTime(response);
        logger.info("Response time: {}ms, Max allowed: {}ms", responseTime, maxTimeMs);
        return responseTime < maxTimeMs;
    }

    /**
     * Extract all JSON paths matching a pattern
     */
    public static Object extractAllFromJSON(Response response, String jsonPath) {
        logger.info("Extracting all values from path: {}", jsonPath);
        return response.jsonPath().get(jsonPath);
    }
}
