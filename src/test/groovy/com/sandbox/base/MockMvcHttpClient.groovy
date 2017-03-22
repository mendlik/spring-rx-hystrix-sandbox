package com.sandbox.base

import groovy.transform.PackageScope
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultActions

import static java.util.Objects.requireNonNull
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

class MockMvcHttpClient {
    private final MockMvc mockMvc

    @PackageScope
    MockMvcHttpClient(MockMvc mockMvc) {
        this.mockMvc = requireNonNull(mockMvc)
    }

    ResultActions sendAsyncGet(String path) {
        MvcResult mvcResult = sendGet(path).andReturn()
        return mockMvc.perform(asyncDispatch(mvcResult))
    }

    ResultActions sendGet(String path) {
        return mockMvc.perform(
            get(path)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
    }
}
