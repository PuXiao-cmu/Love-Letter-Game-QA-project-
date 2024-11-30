package edu.cmu.f24qa.loveletter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Requirement2Test {

    private Deck deck;
    private PlayerList players;
    private Game game;

    @BeforeEach
    public void setUp() {
        // Initialize deck and players
        deck = new Deck();
        players = new PlayerList();
        players.addPlayer("Player 1");
        players.addPlayer("Player 2");

        // Create the game instance
        game = new Game(new CommandLineUserInput(), players, deck);
    }

    /**
     * The function of resetGame() should remove the top card from the deck and save it in the deck
     * without revealing it to the players. 
     */
    @Disabled("Pending implementation: resetGame() must remove the top card after shuffling and store it.")
    @Test
    public void testTopCardRemovedAfterShuffle() {
        // Call resetGame to simulate game setup
        game.resetGame();

        // Verify that the deck contains 15 cards after shuffle and removing the top card
        int expectedDeckSize = 15;
        int actualDeckSize = countRemainingCards(deck);
        assertEquals(expectedDeckSize, actualDeckSize, 
            "After resetGame, the deck should contain 15 cards with 1 card set aside.");
    }

    /**
     * Helper method to count remaining cards in the deck using black-box principles.
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
