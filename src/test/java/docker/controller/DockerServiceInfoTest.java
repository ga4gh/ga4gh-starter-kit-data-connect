package docker.controller;

import org.testng.annotations.Test;
import testutils.SimpleHttpRequestTester;

public class DockerServiceInfoTest {

    @Test
    public void testGetServiceInfo() throws Exception {
        SimpleHttpRequestTester.getRequestAndTest(
            "http://localhost:4500/service-info",
            200,
            true,
            "/responses/service-info/show/00.json"
        );
    }
}
