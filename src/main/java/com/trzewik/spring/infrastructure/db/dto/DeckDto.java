package com.trzewik.spring.infrastructure.db.dto;

import com.trzewik.spring.domain.deck.Deck;
import com.trzewik.spring.domain.deck.DeckFactory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Stack;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DeckDto {
    private final Stack<CardDto> cards;

    public static DeckDto from(Deck deck) {
        return new DeckDto(
            deck.getCards().stream()
                .map(CardDto::from)
                .collect(Collectors.toCollection(Stack::new))
        );
    }

    public static Deck to(DeckDto dto) {
        return DeckFactory.createDeck(
            dto.getCards().stream()
                .map(CardDto::to)
                .collect(Collectors.toCollection(Stack::new))
        );
    }
}
