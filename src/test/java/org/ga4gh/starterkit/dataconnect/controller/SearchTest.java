package org.ga4gh.starterkit.dataconnect.controller;

import org.ga4gh.starterkit.dataconnect.app.DataConnectServer;
import org.ga4gh.starterkit.dataconnect.app.DataConnectServerSpringConfig;
import org.ga4gh.starterkit.dataconnect.model.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testutils.ResourceLoader;

@SpringBootTest
@ContextConfiguration(classes = {
    DataConnectServer.class,
    DataConnectServerSpringConfig.class,
    Search.class
})
@WebAppConfiguration
public class SearchTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @BeforeMethod
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @DataProvider(name = "searchCases")
    public Object[][] getSearchCases() {
        return new Object[][] {
            {
                "select * from one_thousand_genomes_sample;",
                new ArrayList<Object>() {{}},
                status().isOk(),
                true,
                "/responses/search/1kgenomes_all.json"
            },
            {
                "select sample_name from one_thousand_genomes_sample;",
                new ArrayList<Object>() {{}},
                status().isOk(),
                true,
                "/responses/search/1kgenomes_field_samplename.json"
            },
            {
                "select sample_name , sex from one_thousand_genomes_sample;",
                new ArrayList<Object>() {{}},
                status().isOk(),
                true,
                "/responses/search/1kgenomes_field_samplename_sex.json"
            },
            {
                "select * from one_thousand_genomes_sample where population_code='PUR';",
                new ArrayList<Object>() {{}},
                status().isOk(),
                true,
                "/responses/search/1kgenomes_filter_populationcode.json"
            },
            {
                "select * from one_thousand_genomes_sample where population_code='PUR' and sex='female';",
                new ArrayList<Object>() {{}},
                status().isOk(),
                true,
                "/responses/search/1kgenomes_filter_populationcode_sex.json"
            },
            {
                "select sample_name , sex , population_code , population_name from one_thousand_genomes_sample where population_code='PUR' and sex='female';",
                new ArrayList<Object>() {{}},
                status().isOk(),
                true,
                "/responses/search/1kgenomes_field_samplename_sex_populationcode_populationname_filter_populationcode_sex.json"
            }
        };
    }

    @Test(dataProvider = "searchCases")
    public void testSearch(String query, ArrayList<Object> parameters, ResultMatcher expStatus, boolean expSuccess, String expResponseFile) throws Exception {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQuery(query);
        searchRequest.setParameters(parameters);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(searchRequest);

        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders
                .post("/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(expStatus)
            .andReturn();

        if (expSuccess) {
            String responseBody = result.getResponse().getContentAsString();
            String expResponseBody = ResourceLoader.load(expResponseFile);
            Assert.assertEquals(responseBody, expResponseBody);
        }

        
        
    }
}
