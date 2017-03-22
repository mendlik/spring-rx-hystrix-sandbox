package com.sandbox.base

import groovy.transform.TypeChecked
import org.junit.Before
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup

@TypeChecked
abstract class MvcIntegrationSpec extends IntegrationSpec {
    @Autowired
    WebApplicationContext webApplicationContext

    MockMvc mockMvc

    MockMvcHttpClient mockMvcHttpClient

    @Before
    void setupWebApplicationContext() {
        mockMvc = webAppContextSetup(webApplicationContext)
            .build()
        mockMvcHttpClient = new MockMvcHttpClient(mockMvc)
    }
}
