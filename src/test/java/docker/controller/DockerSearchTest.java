package docker.controller;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testutils.SimpleHttpRequestTester;

public class DockerSearchTest {

    @DataProvider(name = "searchCases")
    public Object[][] showUserCases() {
        return new Object[][] {
            {
                "/payloads/search/00.json",
                200,
                true,
                "/responses/search/1kgenomes_all.json"
            },
            {
                "/payloads/search/01.json",
                200,
                true,
                "/responses/search/1kgenomes_field_samplename.json"
            },
            {
                "/payloads/search/02.json",
                200,
                true,
                "/responses/search/1kgenomes_field_samplename_sex.json"
            },
            {
                "/payloads/search/03.json",
                200,
                true,
                "/responses/search/1kgenomes_filter_populationcode.json"
            },
            {
                "/payloads/search/04.json",
                200,
                true,
                "/responses/search/1kgenomes_filter_populationcode_sex.json"
            },
            {
                "/payloads/search/05.json",
                200,
                true,
                "/responses/search/1kgenomes_field_samplename_sex_populationcode_populationname_filter_populationcode_sex.json"
            },
        };
    }

    @Test(dataProvider = "searchCases")
    public void testSearch(String payloadFile, int expStatus, boolean expSuccess, String expResponseFile) throws Exception {
        SimpleHttpRequestTester.postRequestAndTest(
            "http://localhost:4500/search",
            payloadFile,
            expStatus,
            expSuccess,
            expResponseFile
        );
    }
}
