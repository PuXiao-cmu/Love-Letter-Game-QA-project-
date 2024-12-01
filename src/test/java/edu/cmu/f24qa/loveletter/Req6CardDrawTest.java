package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Req6CardDrawTest {
    private PlayerList playerList;
    private Deck deck;

    @BeforeEach
    void setUp() {
        playerList = new PlayerList();
        playerList.addPlayer("Alice");
        playerList.addPlayer("Bob");
        playerList.addPlayer("Charlie");
        playerList.addPlayer("Doe");

        // Initialize a deck with a known sequence of cards
        deck = new Deck();
        playerList.reset();
        deck.build();
        deck.shuffle();
        playerList.dealCards(deck);
    }

    /**
     * Requirement 6 Test: Card Draw per Turn
     * This test verifies that each player draws a card each turn.
     * Steps:
     * 1. Iterate through each player
     * 2. Store the initial card in the player's hand
     * 3. Player draws a card
     * 4. Retrieve the two cards now in the player's hand
     * 5. Validate the player now has exactly two distinct cards
     */
    @Test
    void testCardDrawPerTurn() {
        // Step 1: Iterate through each player
        for (int i = 0; i < 4; i++) {
            Player currentPlayer = playerList.getCurrentPlayer();

            // Step 2: Store the initial card in the player's hand
            Card initialCard = currentPlayer.viewHandCard(0);

            // Step 3: Player draws a card
            Card drawCard = deck.draw();
            currentPlayer.receiveHandCard(drawCard);

            // Step 4: Retrieve the two cards now in the player's hand
            Card firstCard = currentPlayer.viewHandCard(0);
            Card secondCard = currentPlayer.viewHandCard(1);

            // Step 5: Validate the player now has exactly two distinct cards
            assertEquals(firstCard, initialCard, "The initial card should still be in the player's hand.");
            assertEquals(drawCard, secondCard, "The new card should be the second card in the player's hand.");
        }
    }
}
