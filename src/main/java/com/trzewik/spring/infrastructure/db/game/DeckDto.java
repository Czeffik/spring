package com.trzewik.spring.infrastructure.db.game;

import com.trzewik.spring.domain.game.Deck;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Stack;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class DeckDto {
    private Stack<CardDto> cards;

    static DeckDto from(final Deck deck) {
        return new DeckDto(
            deck.getCards().stream()
                .map(CardDto::from)
                .collect(Collectors.toCollection(Stack::new))
        );
    }

    Deck toDeck() {
        return new Deck(
            this.getCards().stream()
                .map(CardDto::toCard)
                .collect(Collectors.toCollection(Stack::new))
        );
    }
}
