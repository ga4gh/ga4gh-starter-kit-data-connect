package testutils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import org.testng.Assert;

public class SimpleHttpRequestTester {

    public static HttpResponse<String> makeHttpRequest(HttpMethod method, String url, String payloadFile) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        Builder requestBuilder = HttpRequest.newBuilder();
        requestBuilder.uri(URI.create(url));

        // set request method
        String requestBody = null;
        switch (method) {
            case GET:
                requestBuilder.GET();
                break;
            case POST:
                requestBody = ResourceLoader.load(payloadFile);
                requestBuilder.POST(BodyPublishers.ofString(requestBody));
                requestBuilder.header("Content-Type", "application/json");
                break;
            case PUT:
                requestBody = ResourceLoader.load(payloadFile);
                requestBuilder.PUT(BodyPublishers.ofString(requestBody));
                requestBuilder.header("Content-Type", "application/json");
                break;
            case DELETE:
                requestBuilder.DELETE();
                break;
        }

        HttpRequest request = requestBuilder.build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static void requestAndTest(HttpMethod method, String url, String payloadFile, int expStatus, boolean expSuccess, String expResponseFile) throws Exception {
        HttpResponse<String> response = makeHttpRequest(method, url, payloadFile);
        Assert.assertEquals(response.statusCode(), expStatus);
        if (expSuccess) {
            String responseBody = response.body();
            String expResponseBody = ResourceLoader.load(expResponseFile);
            Assert.assertEquals(responseBody, expResponseBody);
        }
    }

    public static void getRequestAndTest(String url, int expStatus, boolean expSuccess, String expResponseFile) throws Exception {
        requestAndTest(HttpMethod.GET, url, null, expStatus, expSuccess, expResponseFile);
    }

    public static void postRequestAndTest(String url, String payloadFile, int expStatus, boolean expSuccess, String expResponseFile) throws Exception {
        requestAndTest(HttpMethod.POST, url, payloadFile, expStatus, expSuccess, expResponseFile);
    }

    public static void putRequestAndTest(String url, String payloadFile, int expStatus, boolean expSuccess, String expResponseFile) throws Exception {
        requestAndTest(HttpMethod.PUT, url, payloadFile, expStatus, expSuccess, expResponseFile);
    }

    public static void deleteRequestAndTest(String url, int expStatus, boolean expSuccess, String expResponseFile) throws Exception {
        requestAndTest(HttpMethod.DELETE, url, null, expStatus, expSuccess, expResponseFile);
    }
}
