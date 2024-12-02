package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class FullGameTest {

    private PlayerList playerList;
    private Deck mockDeck;
    private UserInput mockUserInput;
    private Game game;
    private Player spyAlice;
    private Player spyBob;
    private Player alice;
    private Player bob;

    @BeforeEach
    void setUp() {
        // Create PlayerList and add players
        playerList = spy(new PlayerList());
        playerList.addPlayer("Alice");
        playerList.addPlayer("Bob");

        alice = playerList.getPlayer("Alice");
        bob = playerList.getPlayer("Bob");

        // Create spies for players
        spyAlice = spy(playerList.getPlayer("Alice"));
        spyBob = spy(playerList.getPlayer("Bob"));

        // Use spy for Deck
        mockDeck = spy(new Deck());
        mockDeck.build(); // Ensure the deck is built
        mockDeck.shuffle(); // Shuffle the deck
        doNothing().when(mockDeck).hideTopCard();
        doNothing().when(mockDeck).removeAnotherThreeCards();
        mockUserInput = mock(UserInput.class);
        

        // Create the Game instance
        game = spy(new Game(mockUserInput, playerList, mockDeck));
    }

    @Test
    void testCompleteGame() {
        // Step 1: Mock the deck to return predefined card order for all rounds
        when(mockDeck.draw())
            .thenReturn(
                // Round 1
                Card.HANDMAIDEN, Card.PRINCE, Card.GUARD, 
                // Round 2
                Card.GUARD, Card.PRINCESS, Card.HANDMAIDEN, Card.PRIEST, Card.COUNTESS, Card.PRINCE,
                // Round 3
                Card.COUNTESS, Card.BARON, Card.HANDMAIDEN,
                // Round 4
                Card.GUARD, Card.KING, Card.PRINCESS, Card.PRIEST, Card.PRINCE,
                // Round 5
                Card.PRIEST, Card.COUNTESS, Card.BARON, Card.HANDMAIDEN, Card.KING,
                // Round 6
                Card.GUARD, Card.BARON, Card.HANDMAIDEN, Card.GUARD, Card.KING,
                // Round 7
                Card.COUNTESS, Card.GUARD, Card.KING, Card.BARON, Card.PRIEST, Card.PRINCE, Card.HANDMAIDEN,
                // Round 8
                Card.PRINCE, Card.HANDMAIDEN, Card.COUNTESS, Card.BARON, Card.PRIEST, Card.GUARD
            );

        // Simulate 8 rounds
        for (int round = 1; round <= 8; round++) {
            switch (round) {
                case 1 -> simulateRound1();
                case 2 -> simulateRound2();
                case 3 -> simulateRound3();
                case 4 -> simulateRound4();
                case 5 -> simulateRound5();
                case 6 -> simulateRound6();
                case 7 -> simulateRound7();
                case 8 -> simulateRound8();
            }
        }

        // Final assertions
        assertEquals(1, spyAlice.getTokens(), "Alice should have 1 token at the end of the game.");
        assertEquals(7, spyBob.getTokens(), "Bob should have 7 tokens and win the game.");
        assertSame(spyBob, playerList.getGameWinner(), "The winner should be Bob.");
    }

    // Individual round simulations
    private void simulateRound1() {
        // Setup
        doReturn("Prince").when(mockUserInput).getCardName(); // Alice guesses Bob's card
        when(mockUserInput.getCardIndex(any(Player.class))).thenReturn("1");
        when(mockUserInput.getOpponent(any(PlayerList.class), any(Player.class))).thenReturn(bob);

        // Gameplay
        game.playRound();

        // Assertions
        assertEquals(1, alice.getTokens(), "Alice should win Round 1.");
        assertEquals(0, bob.getTokens(), "Bob should have 0 tokens after Round 1.");
        //verify(spyBob, times(1)).eliminate();
    }

    private void simulateRound2() {
        // Setup
        doReturn("1").when(mockUserInput).getCardIndex(alice);
        when(mockUserInput.getCardIndex(bob))
            .thenReturn("0")
            .thenReturn("1"); 
        when(mockUserInput.getOpponent(any(PlayerList.class), any(Player.class), anyBoolean())).thenReturn(alice);

        // Gameplay
        game.playRound();

        // Assertions
        assertEquals(1, playerList.getPlayer("Alice").getTokens(), "Alice should still have 1 token after Round 2.");
        assertEquals(1, playerList.getPlayer("Bob").getTokens(), "Bob should win Round 2.");
    }

    private void simulateRound3() {
        game.playRound();

        assertEquals(1, spyAlice.getTokens(), "Alice should still have 1 token after Round 3.");
        assertEquals(2, spyBob.getTokens(), "Bob should win Round 3.");
    }

    private void simulateRound4() {
        game.playRound();

        assertEquals(1, spyAlice.getTokens(), "Alice should still have 1 token after Round 4.");
        assertEquals(3, spyBob.getTokens(), "Bob should win Round 4.");
    }

    private void simulateRound5() {
        game.playRound();

        assertEquals(1, spyAlice.getTokens(), "Alice should still have 1 token after Round 5.");
        assertEquals(4, spyBob.getTokens(), "Bob should win Round 5.");
    }

    private void simulateRound6() {
        game.playRound();

        assertEquals(1, spyAlice.getTokens(), "Alice should still have 1 token after Round 6.");
        assertEquals(5, spyBob.getTokens(), "Bob should win Round 6.");
    }

    private void simulateRound7() {
        game.playRound();

        assertEquals(1, spyAlice.getTokens(), "Alice should still have 1 token after Round 7.");
        assertEquals(6, spyBob.getTokens(), "Bob should win Round 7.");
    }

    private void simulateRound8() {
        game.playRound();

        assertEquals(1, spyAlice.getTokens(), "Alice should still have 1 token after Round 8.");
        assertEquals(7, spyBob.getTokens(), "Bob should win Round 8.");
    }
}

