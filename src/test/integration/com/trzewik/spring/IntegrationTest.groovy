package com.trzewik.spring

import com.trzewik.spring.interfaces.rest.EmbeddedJetty
import io.restassured.response.Response
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification

import static io.restassured.RestAssured.when

@Ignore
class IntegrationTest extends Specification {
    @Shared
    EmbeddedJetty server
    @Shared
    Thread thread

    def setupSpec() {
//        server = new EmbeddedJetty(9090)
    }

    def 'adasdas'() {
        expect:
        Response response = when()
            .get(getUrl())
            .thenReturn()
        response.statusCode() == 200
        response.body().asString() == ''
    }

    String getUrl() {
        return "http://localhost:9090"
    }

}
