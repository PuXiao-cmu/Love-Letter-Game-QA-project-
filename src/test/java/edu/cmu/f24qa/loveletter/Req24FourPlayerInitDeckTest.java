package edu.cmu.f24qa.loveletter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Req24FourPlayerInitDeckTest {

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
     * For a 4-player game, resetGame() should:
     * 1. Remove 1 card without revealing it.
     * 2. Deal 1 card to each of the 4 players.
     * Thus, it should have 16 - 1 - 4 = 11 cards in the deck after resetGame().
     */
    @Disabled("Pending implementation: resetGame() must account for hidden and dealt cards.")
    @Test
    public void testDeckSizeAfterSetupForFourPlayers() {
        // Add 4 players
        players.addPlayer("Player 1");
        players.addPlayer("Player 2");
        players.addPlayer("Player 3");
        players.addPlayer("Player 4");

        // Call resetGame to simulate game setup
        game.resetGame();

        // Verify that the deck contains 11 cards
        int expectedDeckSize = 11;
        int actualDeckSize = countRemainingCards(deck);
        assertEquals(expectedDeckSize, actualDeckSize,
            "For 4 players, the deck should contain 11 cards after setup.");
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
