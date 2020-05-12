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

    Response makeMoveRequest(String gameId, GameService.MoveForm form) {
        return request("/games/${gameId}/move")
            .contentType(ContentType.JSON)
            .body("""{"playerId": "${form.playerId}", "move": "${form.move.name()}"}""")
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
        return request("/games/${gameId}/players")
            .contentType(ContentType.JSON)
            .body("""{"playerId": "${playerId}"}""")
            .post()
            .thenReturn()
    }
}
