package org.ga4gh.starterkit.dataconnect.controller;

import org.ga4gh.starterkit.dataconnect.app.DataConnectServer;
import org.ga4gh.starterkit.dataconnect.app.DataConnectServerSpringConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
        return new Object[][] {
            { "exampleData" },
            { "variant" },
            { "phenopacket" }
        };
    }

    // TODO Create proper tests when the list tables endpoint is implemented.
    @Test
    public void testListTables() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tables"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        String expResponseBody = "You hit the /tables endpoint. This method is a stub.";
        Assert.assertEquals(responseBody, expResponseBody);
    }

    // TODO Create proper tests when the get table info endpoint is implemented.
    @Test(dataProvider = "getTableCases")
    public void testGetTableInfo(String tableName) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tables/" + tableName + "/info"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        String expResponseBody =
            "You hit the /tables/{table_name}/info endpoint."
            + " This method is a stub."
            + " You requested the table: '" + tableName + "'";
        Assert.assertEquals(responseBody, expResponseBody);
    }

    // TODO Create proper tests when the get table endpoint is implemented.
    @Test(dataProvider = "getTableCases")
    public void testGetTableData(String tableName) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tables/" + tableName + "/data"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        String expResponseBody =
            "You hit the /tables/{table_name}/data endpoint."
            + " This method is a stub."
            + " You requested the table: '" + tableName + "'";
        Assert.assertEquals(responseBody, expResponseBody);
    }
}
