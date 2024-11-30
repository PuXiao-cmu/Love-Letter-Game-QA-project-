package edu.cmu.f24qa.loveletter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Req2ThreePlayerInitDeckTest {

    private Deck deck;
    private PlayerList players;
    private Game game;

    @BeforeEach
    public void setUp() {
        // Initialize deck
        deck = new Deck();
        players = new PlayerList();

        // Create the game instance
        game = new Game(new CommandLineUserInput(), players, deck);
    }

    /**
     * For a 3-player game, resetGame() should:
     * 1. Remove 1 card without revealing it.
     * 2. Deal 1 card to each of the 3 players.
     * Thus, it should have 16 - 1 - 3 = 12 cards in the deck after resetGame().
     */
    @Disabled("Pending implementation: resetGame() must account for the hidden card.")
    @Test
    public void testDeckSizeAfterSetupForThreePlayers() {
        // Add 3 players
        players.addPlayer("Player 1");
        players.addPlayer("Player 2");
        players.addPlayer("Player 3");

        // Call resetGame to simulate game setup
        game.resetGame();

        // Verify that the deck contains 12 cards
        int expectedDeckSize = 12;
        int actualDeckSize = countRemainingCards(deck);
        assertEquals(expectedDeckSize, actualDeckSize,
            "For 3 players, the deck should contain 12 cards after setup.");
    }

    /**
     * Helper method to count remaining cards in the deck.
     *
     * @param deck the Deck object
     * @return the number of cards remaining in the deck
     */
    private int countRemainingCards(Deck deck) {
        int count = 0;
        while (deck.hasMoreCards()) {
            deck.draw();
            count++;
        }
        return count;
    }
}
