package com.trzewik.spring.interfaces.rest


import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.springframework.boot.web.server.LocalServerPort

trait GameRequestSender {
    @LocalServerPort
    int port

    Response getResultsRequest(String gameId) {
        return request("/games/${gameId}/results")
            .get()
            .thenReturn()
    }

    Response makeMoveRequest(String gameId, String playerId, String move) {
        return request("/games/${gameId}/move").contentType(ContentType.JSON)
            .body("""{"playerId": "${playerId}", "move": "${move}"}""").post().thenReturn()
    }

    Response startGameRequest(String gameId) {
        return request("/games/${gameId}/startGame").post().thenReturn()
    }

    Response createGameRequest() {
        return request('/games').contentType(ContentType.JSON).post().thenReturn()
    }

    Response addPlayerRequest(String name, String gameId) {
        return request("/games/${gameId}/players").contentType(ContentType.JSON)
            .body("""{"name": "${name}"}""")
            .post()
            .thenReturn()
    }

    RequestSpecification request(String basePath) {
        return RestAssured.given()
            .baseUri("http://localhost:${port}")
            .basePath(basePath)
            .log().all()
    }
}
