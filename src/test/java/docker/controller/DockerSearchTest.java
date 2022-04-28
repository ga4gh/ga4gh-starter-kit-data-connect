package docker.controller;

import org.testng.annotations.Test;

import testutils.HttpMethod;
import testutils.SimpleHttpRequestTester;

public class DockerSearchTest {

    @Test
    public void testSearch() throws Exception {
        SimpleHttpRequestTester.requestAndTest(
            HttpMethod.POST,
            "http://localhost:4500/search",
            200,
            true,
            "/responses/search/00.txt"
        );
    }
}
