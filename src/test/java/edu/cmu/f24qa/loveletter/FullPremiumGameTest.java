package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;


public class FullPremiumGameTest {

    private PlayerList playerList;
    private Deck mockDeck;
    private UserInput mockUserInput;
    private Game game;
    private Player alice;
    private Player bob;
    private Player charlie;
    private Player diana;
    private Player eve;
    private Player finalWinner;

    @BeforeEach
    void setUp() {
        // Create PlayerList and add players
        playerList = spy(new PlayerList());
        playerList.addPlayer("Alice");
        playerList.addPlayer("Bob");
        playerList.addPlayer("Charlie");
        playerList.addPlayer("Diana");
        playerList.addPlayer("Eve");

        alice = playerList.getPlayer("Alice");
        bob = playerList.getPlayer("Bob");
        charlie = playerList.getPlayer("Charlie");
        diana = playerList.getPlayer("Diana");
        eve = playerList.getPlayer("Eve");

        // Use spy for Deck
        mockDeck = spy(new Deck());
        doNothing().when(mockDeck).hideTopCard();
        doNothing().when(mockDeck).removeAnotherThreeCards();

        // Mock User Input
        mockUserInput = mock(UserInput.class);

        // Create the Game instance
        game = spy(new Game(mockUserInput, playerList, mockDeck));
    }


    @Test
    void testFullPremiumGame() {
        doReturn(
            // Initial hands for round 1
            Card.BISHOP, Card.GUARD, Card.DOWAGERQUEEN, Card.PRINCESS, Card.KING,
            // Round 1
            Card.ASSASSIN, Card.PRINCE, Card.BARONESS, Card.JESTER, Card.CARDINAL,
            Card.SYCOPHANT, Card.CONSTABLE, Card.COUNT, Card.GUARD, Card.GUARD, Card.PRIEST,
            // Initial hands for round 2
            Card.GUARD, Card.DOWAGERQUEEN, Card.PRINCESS, Card.KING, Card.BISHOP,
            // Round 2
            Card.ASSASSIN, Card.PRINCE, Card.BARONESS, Card.JESTER, Card.CARDINAL,
            Card.SYCOPHANT, Card.CONSTABLE, Card.COUNT, Card.GUARD, Card.GUARD, Card.PRIEST,
            // Initial hands for tie-breaking round
            Card.PRINCE, Card.HANDMAIDEN,
            // Tie-breaking round
            Card.GUARD
        ).when(mockDeck).draw();

        // Simulate rounds
        simulateRound1();
        simulateRound2();
        simulateTieBreakingRound();

        // Final assertions
        assertSame(alice, finalWinner, "Alice should be the winner.");
    }


    // Simulate round 1
    private void simulateRound1() {
        doReturn("0").when(mockUserInput).getCardIndex(alice);
        doReturn("0").when(mockUserInput).getCardIndex(bob);
        doReturn("0").doReturn("0")
            .doReturn("0").doReturn("1")
            .when(mockUserInput).getCardIndex(charlie);
    
        when(mockUserInput.getCardNumber())
            .thenReturn(8) // Alice guesses Diana's card
            .thenReturn(6) // Bob guesses Alice's card
            .thenReturn(3) // Charlie guesses Alice's card
            .thenReturn(5); // Alice guesses Charlie's card

        when(mockUserInput.getOpponent(playerList, alice)).thenReturn(diana).thenReturn(charlie);
        when(mockUserInput.getOpponent(playerList, bob)).thenReturn(alice);
        when(mockUserInput.getOpponent(playerList, charlie)).thenReturn(eve).thenReturn(alice);
        when(mockUserInput.getOpponent(playerList, alice, true)).thenReturn(charlie).thenReturn(alice);
        when(mockUserInput.getOpponent(playerList, charlie, false, true)).thenReturn(alice);

        game.playRound();

        // Assertions for Round 1
        assertEquals(1, game.determineRoundWinner().size(), "There is just 1 winner for this round.");
        assertSame(alice, game.determineRoundWinner().get(0), "Alice should be the round winner.");
        assertEquals(2, alice.getTokens(), "Alice should have 2 tokens after Round 1.");
        assertEquals(2, charlie.getTokens(), "Charlie should have 2 tokens after Round 1.");
    }


    // Simulate round 2
    private void simulateRound2() {
        doReturn("0").when(mockUserInput).getCardIndex(alice);
        doReturn("0").when(mockUserInput).getCardIndex(bob);
        doReturn("0").doReturn("0")
            .doReturn("0").doReturn("1")
            .when(mockUserInput).getCardIndex(charlie);
    
        when(mockUserInput.getCardNumber())
            .thenReturn(8) // Alice guesses Diana's card
            .thenReturn(6) // Bob guesses Alice's card
            .thenReturn(3) // Charlie guesses Alice's card
            .thenReturn(5); // Alice guesses Charlie's card

        when(mockUserInput.getOpponent(playerList, alice)).thenReturn(diana).thenReturn(charlie);
        when(mockUserInput.getOpponent(playerList, bob)).thenReturn(alice);
        when(mockUserInput.getOpponent(playerList, charlie)).thenReturn(eve).thenReturn(alice);
        when(mockUserInput.getOpponent(playerList, alice, true)).thenReturn(charlie).thenReturn(alice);
        when(mockUserInput.getOpponent(playerList, charlie, false, true)).thenReturn(alice);

        game.playRound();

        // Assertions for Round 2
        assertEquals(1, game.determineRoundWinner().size(), "There is just 1 winner for this round.");
        assertSame(alice, game.determineRoundWinner().get(0), "Alice should be the round winner.");
        assertEquals(4, alice.getTokens(), "Alice should have 4 tokens after Round 2.");
        assertEquals(4, charlie.getTokens(), "Charlie should have 4 tokens after Round 2.");
    }


    // Simulate tie-breaking round
    private void simulateTieBreakingRound() {
        when(mockUserInput.getCardIndex(alice)).thenReturn("1");
        doReturn(5).when(mockUserInput).getCardNumber(); // Alice guesses Charlie's card
        when(mockUserInput.getOpponent(playerList, alice)).thenReturn(charlie);

        finalWinner = game.getFinalGameWinner();

        // Assertions for Tie-breaking Round
        assertEquals(1, game.determineRoundWinner().size(), "There is just 1 winner for this round.");
        assertSame(alice, game.determineRoundWinner().get(0), "Alice should win the tie-breaking round.");
        assertEquals(5, alice.getTokens(), "Alice should have 5 tokens after the tie-breaking round.");
        assertEquals(4, charlie.getTokens(), "Charlie should have 4 tokens after the tie-breaking round.");
    }
}

