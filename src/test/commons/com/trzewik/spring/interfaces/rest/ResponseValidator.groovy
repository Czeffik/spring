package com.trzewik.spring.interfaces.rest

import groovy.json.JsonSlurper
import io.restassured.response.Response

trait ResponseValidator {
    abstract JsonSlurper getSlurper()

    def parseResponse(Response response) {
        return slurper.parseText(response.body().asString())
    }
}
