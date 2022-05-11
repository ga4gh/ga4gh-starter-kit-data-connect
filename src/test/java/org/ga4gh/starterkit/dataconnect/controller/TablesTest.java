package org.ga4gh.starterkit.dataconnect.controller;

import org.ga4gh.starterkit.dataconnect.app.DataConnectServer;
import org.ga4gh.starterkit.dataconnect.app.DataConnectServerSpringConfig;
import org.json.JSONArray;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testutils.ResourceLoader;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = {
    DataConnectServer.class,
    DataConnectServerSpringConfig.class,
    Tables.class
})
@WebAppConfiguration
public class TablesTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @BeforeMethod
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @DataProvider(name = "getTableCases")
    public Object[][] getTableCases() {
        return new Object[][]{
                {
                        "one_thousand_genomes_sample",
                        status().isOk()
                },
                {
                        "phenopacket_v1",
                        status().isOk()
                }
        };
    }

    @Test
    public void testListTables() throws Exception {
        // From the list of all tables, we only validate the details of
        // phenopacket_v1 and one_thousand_genomes_sample tables

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tables"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
        String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

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
                Assert.assertEquals(tableDetails.get("description"),"Table / directory containing JSON files for 1000 genome samples from https://www.internationalgenome.org");
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
    public void testGetTableInfo(String tableName, ResultMatcher expStatus) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/table/" + tableName + "/info"))
            .andExpect(expStatus)
            .andReturn();
        String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expResponseBody = ResourceLoader.load(String.format("/responses/tables/GetTableInfo/table_info_%s.json",tableName));
        Assert.assertEquals(responseBody, expResponseBody);
    }

    // TODO: fix this!
    @Test(dataProvider = "getTableCases")
    public void testGetTableData(String tableName, ResultMatcher expStatus)
            throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/table/" + tableName + "/data"))
            .andExpect(expStatus)
            .andReturn();

        String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expResponseBody = ResourceLoader.load(String.format("/responses/tables/GetTableData/table_data_%s.json",tableName));
        Assert.assertEquals(responseBody, expResponseBody);
    }
}
