package com.trzewik.spring.interfaces.rest.game

import com.trzewik.spring.domain.game.GameService
import com.trzewik.spring.interfaces.rest.RequestSender
import io.restassured.http.ContentType
import io.restassured.response.Response

trait GameRequestSender extends RequestSender {

    Response getResultsRequest(String gameId) {
        return request("/games/${gameId}/results")
            .get()
            .thenReturn()
    }

    Response makeMoveRequest(String gameId, String playerId, String move) {
        return request("/games/${gameId}/players/${playerId}/${move}")
            .contentType(ContentType.JSON)
            .post()
            .thenReturn()
    }

    Response startGameRequest(String gameId) {
        return request("/games/${gameId}/startGame")
            .post()
            .thenReturn()
    }

    Response createGameRequest() {
        return request('/games')
            .contentType(ContentType.JSON)
            .post()
            .thenReturn()
    }

    Response addPlayerRequest(String gameId, String playerId) {
        return request("/games/${gameId}/players/${playerId}")
            .contentType(ContentType.JSON)
            .post()
            .thenReturn()
    }
}
