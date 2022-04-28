package docker.controller;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testutils.HttpMethod;
import testutils.SimpleHttpRequestTester;

public class DockerTablesTest {

    @DataProvider(name = "getTableCases")
    public Object[][] getTableInfoCases() {
        return new Object[][] {
            {
                "exampleData",
                "00.txt"
            },
            {
                "phenopacket",
                "01.txt"
            },
            {
                "variant",
                "02.txt"
            }
        };
    }

    @Test
    public void testListTables() throws Exception {
        SimpleHttpRequestTester.requestAndTest(
            HttpMethod.GET,
            "http://localhost:4500/tables",
            200,
            true,
            "/responses/tables/listTables/00.txt"
        );
    }

    @Test(dataProvider = "getTableCases")
    public void testGetTableInfo(String tableName, String fileName) throws Exception {
        SimpleHttpRequestTester.requestAndTest(
            HttpMethod.GET,
            "http://localhost:4500/tables/" + tableName + "/info",
            200,
            true,
            "/responses/tables/getTableInfo/" + fileName
        );
    }

    @Test(dataProvider = "getTableCases")
    public void testGetTableData(String tableName, String fileName) throws Exception {
        SimpleHttpRequestTester.requestAndTest(
            HttpMethod.GET,
            "http://localhost:4500/tables/" + tableName + "/data",
            200,
            true,
            "/responses/tables/getTableData/" + fileName
        );
    }
}
