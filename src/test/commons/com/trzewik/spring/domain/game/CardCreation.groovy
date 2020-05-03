package com.trzewik.spring.domain.game

trait CardCreation {
    Card createCard(CardCreator creator = new CardCreator()) {
        return new Card(
            creator.suit,
            creator.rank
        )
    }

    Stack<Card> createCards(Stack<CardCreator> creators = [new CardCreator(), new CardCreator(rank: Card.Rank.EIGHT)] as Stack) {
        return creators.collect { createCard(it) } as Stack<Card>
    }

    Stack<CardCreator> createDeckCreators() {
        Stack<CardCreator> stack = new Stack<>()
        Card.Suit.values().each { suit ->
            Card.Rank.values().each { rank ->
                stack.push(new CardCreator(suit: suit, rank: rank))
            }
        }
        return stack
    }

    static class CardCreator {
        Card.Suit suit = Card.Suit.CLUB
        Card.Rank rank = Card.Rank.ACE

        CardCreator() {}

        CardCreator(String suit, String rank) {
            this.suit = Card.Suit.valueOf(suit)
            this.rank = Card.Rank.valueOf(rank)
        }
    }
}
