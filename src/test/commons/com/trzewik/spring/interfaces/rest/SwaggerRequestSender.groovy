package com.trzewik.spring.interfaces.rest

import io.restassured.response.Response

trait SwaggerRequestSender extends RequestSender{
    Response getApiDocsRequest() {
        return request("/v2/api-docs")
            .get()
            .thenReturn()
    }

    Response getSwaggerUIRequest() {
        return request("/swagger-ui.html")
            .get()
            .thenReturn()
    }
}
