package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BlackboxCountCardTest {

    private Player player;

    @BeforeEach
    void setup() {
        player = new Player("TestPlayer");
    }

    /**
     * Rule 1: If a player has a "Count" card in hand, increase hand card value by 1.
     */
    @Test
    void testFinalHandValueWithCountInHand() {
        player.receiveHandCard(Card.COUNT);

        assertEquals(6, player.finalHandValue());
    }

    /**
     * Rule 2: If a player has one "Count" card in hand and one "Count" card in the discard pile, increase hand card value by 2.
     */
    @Test
    void testFinalHandValueWithCountInHandAndDiscardPile() {
        player.receiveHandCard(Card.COUNT);

        player.discardCard(Card.COUNT);

        assertEquals(7, player.finalHandValue());
    }

    /**
     * Rule 3: If a player has two "Count" cards in the discard pile, increase hand card value by 2.
     */
    @Test
    void testFinalHandValueWithTwoCountsInDiscardPile() {
        player.receiveHandCard(Card.GUARD);

        player.discardCard(Card.COUNT);
        player.discardCard(Card.COUNT);

        assertEquals(3, player.finalHandValue());
    }

    /**
     * Rule 4: If the player does not have any "Count" card in hand or discard pile,
     * the final hand value is the base value of the hand card.
     */
    @Test
    void testFinalHandValueWithoutAnyCountCard() {
        player.receiveHandCard(Card.GUARD);

        assertEquals(1, player.finalHandValue());
    }
}
