package com.trzewik.spring.domain.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Suit {
    SPADE("♠"),
    HEART("❤"),
    DIAMOND("♦"),
    CLUB("♣");

    private final String image;
}
