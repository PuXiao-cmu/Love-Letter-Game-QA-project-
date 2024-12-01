package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

public class Req9SkipTurnsTest {

    private PlayerList playerList;
    private Deck mockDeck;
    private UserInput mockUserInput;
    private Game game;
    private Player spyAlice;
    private Player spyBob;
    private Player spyCharlie;

    @BeforeEach
    void setUp() {
        // Create a spy for PlayerList
        playerList = spy(new PlayerList());

        // Add players directly to the PlayerList
        playerList.addPlayer("Alice");
        playerList.addPlayer("Bob");
        playerList.addPlayer("Charlie");

        // Create spy players
        spyAlice = spy(playerList.getPlayer("Alice"));
        spyBob = spy(playerList.getPlayer("Bob"));
        spyCharlie = spy(playerList.getPlayer("Charlie"));

        // Assign initial cards to each player
        spyAlice.receiveHandCard(Card.COUNTESS);
        spyBob.receiveHandCard(Card.COUNTESS);
        spyCharlie.receiveHandCard(Card.COUNTESS);

        // Mock the deck
        mockDeck = mock(Deck.class);
        when(mockDeck.draw()).thenReturn(Card.COUNTESS); // Mock deck always draws COUNTESS card

        // Mock the UserInput
        mockUserInput = mock(UserInput.class);
        when(mockUserInput.getCardIndex(any(Player.class))).thenReturn("0"); // Always play the first card

        // Create a spy for the Game class
        game = spy(new Game(mockUserInput, playerList, mockDeck));

        // Stub game methods
        doReturn(false).when(playerList).checkForRoundWinner(); // Round never ends based on players
        doReturn(true, true, true, false).when(mockDeck).hasMoreCards(); // Deck has cards initially, then runs out
        List<Player> winners = new ArrayList<>();
        winners.add(spyBob);
        doReturn(winners).when(game).determineRoundWinner(); // Bob always wins the round
    }

    /**
     * Requirement 9.1: The system shall skip the turns of eliminated players in the current round.
     */
    @Test
    void testEliminatedPlayerSkippedDuringRound() throws Exception {
        // Eliminate Alice
        spyAlice.eliminate();

        // Stub resetGame to do nothing to ensure that the elimination action is happened in current round.
        doNothing().when(game).resetGame();

        // Call playRound
        game.playRound();

        // Capture arguments for executeTurn
        ArgumentCaptor<Player> playerCaptor = ArgumentCaptor.forClass(Player.class);
        verify(game, atLeastOnce()).executeTurn(playerCaptor.capture());

        // Extract captured arguments
        List<Player> capturedPlayers = playerCaptor.getAllValues(); // List of all captured arguments

        // Validate captured arguments
        for (Player player : capturedPlayers) {
            System.out.println(player.getName()); // Print the name of each player whose turn was executed
        }

        // Validate captured arguments by name
        boolean bobExecuted = playerCaptor.getAllValues().stream()
                                        .anyMatch(player -> "Bob".equals(player.getName()));
        boolean charlieExecuted = playerCaptor.getAllValues().stream()
                                            .anyMatch(player -> "Charlie".equals(player.getName()));
        boolean aliceExecuted = playerCaptor.getAllValues().stream()
                                            .anyMatch(player -> "Alice".equals(player.getName()));

        // Assertions
        assertTrue(bobExecuted, "Bob should have executed a turn.");
        assertTrue(charlieExecuted, "Charlie should have executed a turn.");
        assertFalse(aliceExecuted, "Alice should not have executed a turn.");
    }

    /**
     * Requirement 9.2 The system shall reset the status of eliminated players at the start of a new round.
     */
    @Test
    void testResetEliminatedPlayerStatusAtNewRound() throws Exception {
        // Eliminate Alice in the current round
        spyAlice.eliminate();

        // Verify that Alice has no hand cards after being eliminated
        assertFalse(spyAlice.hasHandCards(), "Alice should have no hand cards after being eliminated.");
        assertTrue(spyAlice.discardedValue() > 0, "Alice should have cards in the discard pile after elimination.");

        // Start a new round by calling resetGame
        game.resetGame();

        // Verify that Alice's status has been reset
        assertTrue(spyAlice.hasHandCards(), "Alice should have hand cards after the new round starts.");
        assertEquals(0, spyAlice.discardedValue(), "Alice's discard pile should be empty at the start of the new round.");

        // Verify that other players' statuses are also reset
        assertTrue(spyBob.hasHandCards(), "Bob should have hand cards after the new round starts.");
        assertEquals(0, spyBob.discardedValue(), "Bob's discard pile should be empty at the start of the new round.");

        assertTrue(spyCharlie.hasHandCards(), "Charlie should have hand cards after the new round starts.");
        assertEquals(0, spyCharlie.discardedValue(), "Charlie's discard pile should be empty at the start of the new round.");
    }


}
