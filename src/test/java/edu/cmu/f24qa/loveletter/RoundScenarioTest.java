package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoundScenarioTest {

    private PlayerList playerList;
    private Deck mockDeck;
    private UserInput mockUserInput;
    private Game game;
    private Player player1;
    private Player player2;
    private Player player3;

    @BeforeEach
    void setUp() {
        // Initialize PlayerList and add players
        playerList = new PlayerList();
        playerList.addPlayer("player1");
        playerList.addPlayer("player2");
        playerList.addPlayer("player3");

        // Retrieve real Player objects
        player1 = playerList.getPlayer("player1");
        player2 = playerList.getPlayer("player2");
        player3 = playerList.getPlayer("player3");

        // Create a mocked Deck
        mockDeck = mock(Deck.class);

        // Mock the card draw sequence
        when(mockDeck.draw())
            .thenReturn(
                Card.BARON, Card.PRINCE, Card.GUARD, // Initial hands for player1, player2, player3
                Card.PRIEST, // Player1's draw (Turn 1)
                Card.GUARD,  // Player2's draw (Turn 2)
                Card.COUNTESS, // Player3's draw (Turn 3)
                Card.HANDMAIDEN, // Player1's draw (Turn 4)
                Card.PRINCESS, // Player3's draw (Turn 6)
                Card.KING // Player1's draw (Turn 7)
            );

        // Mock UserInput
        mockUserInput = mock(UserInput.class);

        // Create the Game instance
        game = new Game(mockUserInput, playerList, mockDeck);
    }

    /**
     * This is a scenario test for one round in a 3-players game.
     */
    @Test
    void testGameRoundForThreePlayers() {
        // Step 1: Setup - Deal one card to each player
        playerList.dealCards(mockDeck); // Simulate dealing one card to each player

        // Verify initial hand setup
        verify(mockDeck, times(3)).draw(); // One card drawn for each of the three players
        assertEquals(Card.BARON, player1.viewHandCard(0), "Player1 should have Baron as initial hand.");
        assertEquals(Card.PRINCE, player2.viewHandCard(0), "Player2 should have Prince as initial hand.");
        assertEquals(Card.GUARD, player3.viewHandCard(0), "Player3 should have Guard as initial hand.");

        // Step 2: Verify the result of each turn.
        // Simulate Player1's Turn (Turn 1)
        when(mockUserInput.getOpponent(playerList, player1)).thenReturn(player2); // Player1 targets Player2
        when(mockUserInput.getCardIndex(player1)).thenReturn("1"); // Select Priest
        game.executeTurn(player1);

        // After Turn 1
        assertEquals(Card.BARON, player1.viewHandCard(0), "Player1 should have Baron left in hand.");
        assertEquals(Card.PRINCE, player2.viewHandCard(0), "Player2's hand should remain unchanged.");

        // Simulate Player2's Turn (Turn 2)
        when(mockUserInput.getOpponent(playerList, player2)).thenReturn(player3); // Player2 targets Player3
        when(mockUserInput.getCardIndex(player2)).thenReturn("1"); // Select Guard
        // when(mockUserInput.getCardName()).thenReturn("Guard"); // Guess Guard
        when(mockUserInput.getCardNumber()).thenReturn(1); // Guess Guard
        game.executeTurn(player2);

        // After Turn 2
        assertEquals(Card.GUARD, player3.viewHandCard(0), "Player3 should still have Guard as guessing Guard is consider as incorrect guess.");
        assertEquals(Card.PRINCE, player2.viewHandCard(0), "Player2 should have Prince left in hand.");

        // Simulate Player3's Turn (Turn 3)
        when(mockUserInput.getOpponent(playerList, player3)).thenReturn(player2); // Player3 targets Player2
        when(mockUserInput.getCardIndex(player3)).thenReturn("0"); // Select Guard
        // when(mockUserInput.getCardName()).thenReturn("Prince"); // Guess Prince
        when(mockUserInput.getCardNumber()).thenReturn(5); // Guess Prince
        game.executeTurn(player3);

        // After Turn 3
        assertFalse(player2.hasHandCards(), "Player2 should be eliminated after Player3's correct guess.");
        assertEquals(Card.COUNTESS, player3.viewHandCard(0), "Player3 should have Countess left in hand.");

        // Simulate Player1's Turn (Turn 4)
        when(mockUserInput.getCardIndex(player1)).thenReturn("1"); // Select Handmaiden
        game.executeTurn(player1);

        // After Turn 4
        assertTrue(player1.isProtected(), "Player1 should be protected after playing Handmaiden.");
        assertEquals(Card.BARON, player1.viewHandCard(0), "Player1 should have Baron left in hand.");

        // Verify Skipped Turn for Player2 (Turn 5)
        assertFalse(player2.hasHandCards(), "Player2 should have no hand cards as they are eliminated.");

        // Simulate Player3's Turn (Turn 6)
        when(mockUserInput.getCardIndex(player3)).thenReturn("0"); // Select Countess
        game.executeTurn(player3);

        // After Turn 6
        assertEquals(Card.PRINCESS, player3.viewHandCard(0), "Player3 should have Princess left in hand.");
        assertFalse(player2.hasHandCards(), "Player2 should have no hand cards as they are eliminated.");

        // Simulate Player1's Turn (Turn 7)
        when(mockUserInput.getOpponent(playerList, player1)).thenReturn(player3); // Player1 targets Player3
        when(mockUserInput.getCardIndex(player1)).thenReturn("0"); // Select Baron
        game.executeTurn(player1);

        // After Turn 7
        assertFalse(player1.hasHandCards(), "Player1 should be eliminated after losing Baron comparison.");
        assertEquals(Card.PRINCESS, player3.viewHandCard(0), "Player3 should still have Princess in hand.");
        assertFalse(player2.hasHandCards(), "Player2 should have no hand cards as they are eliminated.");

        // Step 3: Final assertions
        assertTrue(player3.hasHandCards(), "Player3 should be the last active player.");
        assertFalse(player1.hasHandCards(), "Player1 should be eliminated.");
        assertFalse(player2.hasHandCards(), "Player2 should be eliminated.");
        assertTrue(playerList.checkForRoundWinner(), "There should be a round winner for this round.");
        List<Player> winners = game.determineRoundWinner();
        assertEquals(1, winners.size(), "There should be exactly one winner.");
        assertTrue(winners.contains(player3), "The winners list should only contain Player3.");
        game.handleRoundWinner(winners);
        assertEquals(1, player3.getTokens(), "Player3 should now have 1 token.");

    }
}
