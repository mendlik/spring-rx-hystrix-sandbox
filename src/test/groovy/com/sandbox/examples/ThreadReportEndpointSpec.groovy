package com.sandbox.examples

import com.sandbox.base.MvcIntegrationSpec
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Unroll

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ThreadReportEndpointSpec extends MvcIntegrationSpec {
    @Unroll
    void "should return thread report from #uri"() {
        when:
            ResultActions result = mockMvcHttpClient.sendGet(uri)
        then:
            expectNonEmptyThreadReport(result)
        where:
            uri << [
                '/threads/undertow',
                '/threads/hystrix',
            ]
    }

    @Unroll
    void "should return thread report asynchronously from #uri"() {
        when:
            ResultActions result = mockMvcHttpClient.sendAsyncGet(uri)
        then:
            expectNonEmptyThreadReport(result)
        where:
            uri << [
                '/threads/rx',
                '/threads/rxhystrix',
                '/threads/hystrixrx'
            ]
    }

    private void expectNonEmptyThreadReport(ResultActions resultActions) {
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath('$.threadTrace').isNotEmpty())
            .andExpect(jsonPath('$.threadDump').isNotEmpty())
    }
}
