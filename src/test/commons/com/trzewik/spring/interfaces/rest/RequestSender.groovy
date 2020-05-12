package com.trzewik.spring.interfaces.rest

import io.restassured.RestAssured
import io.restassured.specification.RequestSpecification

trait RequestSender {
    abstract int getPort()

    RequestSpecification request(String basePath) {
        return RestAssured.given()
            .baseUri("http://localhost:${port}")
            .basePath(basePath)
            .log().all()
    }
}
