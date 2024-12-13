package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BlackboxCountCardTest {

    private Player player;

    @BeforeEach
    void setup() {
        // Initialize the player
        player = new Player("TestPlayer");
    }

    /**
     * Rule 1: If a player has a "Count" card in hand, increase hand card value by 1.
     */
    @Test
    void testFinalHandValueWithCountInHand() {
        // Add a "Count" card to the player's hand
        player.receiveHandCard(Card.COUNT);

        // Verify final hand value
        assertEquals(6, player.finalHandValue());
    }

    /**
     * Rule 2: If a player has one "Count" card in hand and one "Count" card in the discard pile, increase hand card value by 2.
     */
    @Test
    void testFinalHandValueWithCountInHandAndDiscardPile() {
        // Add a "Count" card to the player's hand
        player.receiveHandCard(Card.COUNT);

        // Add a "Count" card to the player's discard pile
        player.discardCard(Card.COUNT);

        // Verify final hand value
        assertEquals(7, player.finalHandValue());
    }

    /**
     * Rule 3: If a player has two "Count" cards in the discard pile, increase hand card value by 2.
     */
    @Test
    void testFinalHandValueWithTwoCountsInDiscardPile() {
        // Add a non-"Count" card to the player's hand
        player.receiveHandCard(Card.GUARD);

        // Add two "Count" cards to the player's discard pile
        player.discardCard(Card.COUNT);
        player.discardCard(Card.COUNT);

        // Verify final hand value
        assertEquals(3, player.finalHandValue());
    }
}
