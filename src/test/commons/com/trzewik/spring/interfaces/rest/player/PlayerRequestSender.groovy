package com.trzewik.spring.interfaces.rest.player

import com.trzewik.spring.domain.player.PlayerService.CreateForm
import com.trzewik.spring.interfaces.rest.RequestSender
import io.restassured.http.ContentType
import io.restassured.response.Response

trait PlayerRequestSender extends RequestSender {
    Response createPlayerRequest(CreateForm form) {
        return request('/players')
            .contentType(ContentType.JSON)
            .body("""{"name": "${form.name}"}""")
            .post()
            .thenReturn()
    }

    Response getPlayerRequest(String playerId) {
        return request("/players/${playerId}")
            .get()
            .thenReturn()
    }

}
