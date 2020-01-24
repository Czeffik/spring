package com.trzewik.spring.interfaces.rest.player

import com.trzewik.spring.interfaces.rest.RequestSender
import io.restassured.http.ContentType
import io.restassured.response.Response
import org.springframework.boot.web.server.LocalServerPort

trait PlayerRequestSender extends RequestSender{
    @LocalServerPort
    int port

    Response createPlayerRequest(String playerName) {
        return request('/players').contentType(ContentType.JSON)
            .body("""{"name": "${playerName}"}""").post().thenReturn()
    }

    Response getPlayerRequest(String playerId){
        return request("/players/${playerId}").get().thenReturn()
    }

}
