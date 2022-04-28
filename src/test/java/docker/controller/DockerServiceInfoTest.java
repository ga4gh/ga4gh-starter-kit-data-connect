package docker.controller;

import org.testng.annotations.Test;
import testutils.HttpMethod;
import testutils.SimpleHttpRequestTester;

public class DockerServiceInfoTest {

    @Test
    public void testGetServiceInfo() throws Exception {
        SimpleHttpRequestTester.requestAndTest(
            HttpMethod.GET,
            "http://localhost:4500/service-info",
            200,
            true,
            "/responses/service-info/show/00.json"
        );
    }
}
