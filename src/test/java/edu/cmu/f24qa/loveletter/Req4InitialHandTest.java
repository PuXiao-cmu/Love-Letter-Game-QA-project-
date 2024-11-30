package edu.cmu.f24qa.loveletter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Req4InitialHandTest {

    private Deck deck;
    private PlayerList players;
    private Game game;

    @BeforeEach
    public void setUp() {
        // Initialize deck and players
        deck = new Deck();
        players = new PlayerList();

        // Add players dynamically for testing
        players.addPlayer("Player 1");
        players.addPlayer("Player 2");
        players.addPlayer("Player 3");
        players.addPlayer("Player 4");

        // Create the game instance
        game = new Game(new CommandLineUserInput(), players, deck);
    }

    /**
     * Test to ensure that after calling resetGame(), each player's hand contains exactly one card.
     */
    @Test
    public void testEachPlayerHasOnlyOneCardInHand() {
        // Call resetGame to simulate game setup
        game.resetGame();

        // Verify that each player's hand contains exactly one card
        for (int i = 0; i < countPlayers(); i++) {
            Player currentPlayer = players.getCurrentPlayer();

            int handSize = getHandSize(currentPlayer);
            assertEquals(1, handSize, 
                currentPlayer.getName() + "'s hand should contain exactly one card after resetGame.");
        }
    }

    /**
     * Helper method to count the number of players in the PlayerList.
     *
     * @return the number of players in the game
     */
    private int countPlayers() {
        int playerCount = 0;

        Player initialPlayer = players.getCurrentPlayer();
        do {
            playerCount++;
        } while (!players.getCurrentPlayer().equals(initialPlayer));

        return playerCount;
    }

    /**
     * Helper method to determine the number of cards in a player's hand using public methods.
     *
     * @param player the Player object to check
     * @return the number of cards in the player's hand
     */
    private int getHandSize(Player player) {
        int handSize = 0;

        while (true) {
            try {
                player.viewHandCard(handSize);
                handSize++;
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
        return handSize;
    }
}
