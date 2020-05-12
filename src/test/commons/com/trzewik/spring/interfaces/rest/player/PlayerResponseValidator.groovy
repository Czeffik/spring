package com.trzewik.spring.interfaces.rest.player

import com.trzewik.spring.domain.player.Player
import com.trzewik.spring.interfaces.rest.ResponseValidator
import io.restassured.response.Response

trait PlayerResponseValidator extends ResponseValidator {

    boolean validatePlayerResponse(Response response, Player player) {
        def parsedResponse = parseResponse(response)
        assert parsedResponse.id == player.id
        assert parsedResponse.name == player.name
        return true
    }
}
