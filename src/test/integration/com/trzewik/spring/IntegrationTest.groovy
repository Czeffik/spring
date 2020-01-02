package com.trzewik.spring


import com.trzewik.spring.interfaces.rest.RestConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.AnnotationConfigWebContextLoader
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import spock.lang.Specification

@ContextConfiguration(
    classes = [
        RestConfiguration.class,
//        AnnotationConfigWebApplicationContext.class
    ]
//    , loader = AnnotationConfigWebContextLoader
)
@WebAppConfiguration
class IntegrationTest extends Specification {
    @Autowired
    WebApplicationContext context

    MockMvc mockMvc

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    def 'adasdas'() {
        expect:
        mockMvc.perform(MockMvcRequestBuilders.get('/'))
            .andExpect(MockMvcResultMatchers.content()
                .string('{\"dupa\": \"greeting\"}'))
    }

}
