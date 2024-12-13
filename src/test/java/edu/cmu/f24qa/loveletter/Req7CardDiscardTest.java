package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class Req7CardDiscardTest {

    private Game game;
    private Player spyPlayer;
    private Deck mockDeck;
    private Card mockCardInHand;
    private Card mockCardInDeck;
    private UserInput mockUserInput;

    @BeforeEach
    void setUp() {
        // Mock deck and cards
        mockDeck = mock(Deck.class);
        mockCardInHand = mock(Card.class);
        mockCardInDeck = mock(Card.class);

        // Set up mock behavior for deck.draw()
        when(mockDeck.draw()).thenReturn(mockCardInDeck);

        // Spy on player behavior
        spyPlayer = spy(new Player("TestPlayer"));

        // Add mockCardInHand to player's hand
        spyPlayer.receiveHandCard(mockCardInHand);

        // Set up mock behaviour for mockUserInput.getCardIndex()
        mockUserInput = mock(UserInput.class);
        when(mockUserInput.getCardIndex(spyPlayer)).thenReturn("0");

        // Initialize the game with the spy player and mock deck
        PlayerList playerList = new PlayerList();
        playerList.addPlayer("TestPlayer");
        game = new Game(mockUserInput, playerList, mockDeck);
    }

    /**
     * Requirement 7 Test: Card Discard and Effect Application
     * Tests the execution of a player's turn in the Game class.
     * Utilizes reflection to access and invoke the private method `executeTurn`.
     * Verifies that:
     * - The player draws a card from the deck.
     * - The player receives the drawn card.
     * - The player's hand card is played.
     * - The player's hand card is discarded.
     * - The card's execute method is called with the correct parameters.
     * Fails the test if any exceptions occur during the process.
     */
    @Disabled
    void testExecuteTurn() {
        // Use reflection to access private executeTurn method
        try {
            var executeTurnMethod = Game.class.getDeclaredMethod("executeTurn", Player.class);
            executeTurnMethod.setAccessible(true);

            // Invoke executeTurn
            executeTurnMethod.invoke(game, spyPlayer);

            // Verify player draw a card from deck and receive a card
            verify(mockDeck, times(1)).draw();
            verify(spyPlayer, times(1)).receiveHandCard(mockCardInDeck);

            // Verify that playHandCard is called
            verify(spyPlayer, times(1)).playHandCard(0);

            // Verify that discardCard is called
            verify(spyPlayer, times(1)).discardCard(mockCardInHand);

            // Verify that execute method is called on the card
            verify(mockCardInHand, times(1)).execute(any(), eq(spyPlayer), any(), eq(mockDeck));

        } catch (Exception e) {
            fail("Failed to test executeTurn: " + e.getMessage());
        }
    }
    
}
