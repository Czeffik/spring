package com.trzewik.spring.interfaces.rest

import io.restassured.response.Response
import org.springframework.http.HttpStatus

trait ErrorResponseValidator extends ResponseValidator {
    boolean validateErrorResponse(Response response, String message, HttpStatus status) {
        def parsedResponse = parseResponse(response)
        assert parsedResponse.message == message
        assert parsedResponse.code == status.value()
        assert parsedResponse.reason == status.reasonPhrase
        return true
    }
}
