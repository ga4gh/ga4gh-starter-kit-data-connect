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
import org.testng.annotations.Test;
import testutils.ResourceLoader;

@SpringBootTest
@ContextConfiguration(classes = {
    DataConnectServer.class,
    DataConnectServerSpringConfig.class,
    ServiceInfo.class
})
@WebAppConfiguration
public class ServiceInfoTest extends AbstractTestNGSpringContextTests {

    private static final String SERVICE_INFO_FILE = "/responses/service-info/show/00.json";

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @BeforeMethod
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void testGetServiceInfo() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/service-info"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        String expResponseBody = ResourceLoader.load(SERVICE_INFO_FILE);
        Assert.assertEquals(responseBody, expResponseBody);
    }
}
