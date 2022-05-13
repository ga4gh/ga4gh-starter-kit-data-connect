package docker.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONParser;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testutils.HttpMethod;
import testutils.SimpleHttpRequestTester;
import java.net.http.HttpResponse;
import static testutils.SimpleHttpRequestTester.makeHttpRequest;

public class DockerTablesTest {

    @DataProvider(name = "getTableCases")
    public Object[][] getTableCases() {
        return new Object[][]{
                {
                        "one_thousand_genomes_sample"
                },
                {
                        "phenopacket_v1"
                }
        };
    }

    @Test
    public void testListTables() throws Exception {
        // From the list of all tables, we only validate the details of
        // phenopacket_v1 and one_thousand_genomes_sample tables

        HttpResponse<String> response = makeHttpRequest(HttpMethod.GET, "http://localhost:4500/tables", null);
        Assert.assertEquals(response.statusCode(), 200);

        String responseBody = response.body();
        JSONObject dataObj = (JSONObject) JSONParser.parseJSON(responseBody);

        //Get the required object from the above created object
        JSONArray tablesList = (JSONArray) dataObj.get("tables");

        boolean phenopacketV1Present = false;
        boolean oneThousandGenomesSamplePresent = false;

        for (int i = 0; i < tablesList.length(); i++) {

            JSONObject tableDetails = (JSONObject) tablesList.get(i);

            // Assert the table details for phenopacket_v1 and one_thousand_genomes_sample tables
            if (tableDetails.get("name").equals("phenopacket_v1")) {
                phenopacketV1Present = true;
                Assert.assertEquals(tableDetails.get("description"),"Table / directory containing JSON files for phenopackets");
                JSONObject dataModel = (JSONObject) tableDetails.get("data_model");
                Assert.assertEquals(dataModel.get("$ref"),"table/phenopacket_v1/info");
            } else if(tableDetails.get("name").equals("one_thousand_genomes_sample")){
                oneThousandGenomesSamplePresent = true;
                Assert.assertEquals(tableDetails.get("description"),"Table / directory containing JSON files for one thousand genomes sample from https://www.internationalgenome.org");
                JSONObject dataModel = (JSONObject) tableDetails.get("data_model");
                Assert.assertEquals(dataModel.get("$ref"),"table/one_thousand_genomes_sample/info");
            }
            if (phenopacketV1Present && oneThousandGenomesSamplePresent){
                break;
            }
        }

        // Assert that the default tables, "phenopacket_v1" and "one_thousand_genomes_sample" are present in the list of tables
        Assert.assertEquals(phenopacketV1Present, true);
        Assert.assertEquals(oneThousandGenomesSamplePresent, true);

    }

    @Test(dataProvider = "getTableCases")
    public void testGetTableInfo(String tableName) throws Exception {
        SimpleHttpRequestTester.getRequestAndTest(
            "http://localhost:4500/table/" + tableName + "/info",
            200,
            true,
            String.format("/responses/tables/getTableInfo/table_info_%s.json",tableName)
        );
    }

    @Test(dataProvider = "getTableCases")
    public void testGetTableData(String tableName) throws Exception {
        SimpleHttpRequestTester.getRequestAndTest(
            "http://localhost:4500/table/" + tableName + "/data",
            200,
            true,
            String.format("/responses/tables/getTableData/table_data_%s.json",tableName)
        );
    }
}
