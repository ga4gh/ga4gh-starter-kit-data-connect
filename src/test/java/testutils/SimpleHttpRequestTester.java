package testutils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;

import org.testng.Assert;

public class SimpleHttpRequestTester {

    public static void requestAndTest(HttpMethod method, String url, int expStatus, boolean expSuccess, String expResponseFile) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        Builder requestBuilder = HttpRequest.newBuilder();

        // set request method
        switch (method) {
            case GET:
                requestBuilder.GET();
                break;
            case POST:
                requestBuilder.POST(BodyPublishers.ofString(""));
                break;
            case PUT:
                requestBuilder.PUT(BodyPublishers.ofString(""));
                break;
            case DELETE:
                requestBuilder.DELETE();
                break;
        }

        requestBuilder.uri(URI.create(url));
        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(response.statusCode(), expStatus);
        if (expSuccess) {
            String responseBody = response.body();
            String expResponseBody = ResourceLoader.load(expResponseFile);
            Assert.assertEquals(responseBody, expResponseBody);
        }
    }
}
