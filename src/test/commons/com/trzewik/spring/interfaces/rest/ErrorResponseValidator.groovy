package com.trzewik.spring.interfaces.rest

import io.restassured.response.Response
import org.springframework.http.HttpStatus

trait ErrorResponseValidator extends ResponseValidator {
    boolean validateErrorResponse(Response response, String message, HttpStatus status) {
        def parsedResponse = parseResponse(response)
        assert parsedResponse.message == message
        assert validateCodeAndReason(parsedResponse, status)
        return true
    }

    boolean validateErrorResponseMatch(Response response, String message, HttpStatus status) {
        def parsedResponse = parseResponse(response)
        assert parsedResponse.message.matches(message)
        assert validateCodeAndReason(parsedResponse, status)
        return true
    }

    private boolean validateCodeAndReason(parsedResponse, HttpStatus status) {
        assert parsedResponse.code == status.value()
        assert parsedResponse.reason == status.reasonPhrase
        return true
    }
}
