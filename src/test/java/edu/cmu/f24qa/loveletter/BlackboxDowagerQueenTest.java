package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Blackbox tests for Dowager Queen card functionality.
 */
class BlackboxDowagerQueenTest {
    private PlayerList playerList;
    private Player user;
    private Player opponent;
    private DowagerQueenAction dowagerQueenAction;
    private Deck deck;
    private UserInput mockUserInput;

    @BeforeEach
    void setup() {
        playerList = new PlayerList();
        
        playerList.addPlayer("User");
        playerList.addPlayer("Opponent");
        playerList.addPlayer("Player3");
        playerList.addPlayer("Player4");
        playerList.addPlayer("Player5");
        
        assertEquals(5, playerList.numPlayer(), "Should have 5 players for Dowager Queen card");
        
        user = playerList.getPlayer("User");
        opponent = playerList.getPlayer("Opponent");
        dowagerQueenAction = new DowagerQueenAction();
        deck = new Deck();
        deck.buildPremium();
        
        mockUserInput = mock(UserInput.class);
        when(mockUserInput.getOpponent(any(PlayerList.class), any(Player.class)))
            .thenReturn(opponent);
    }

    /**
     * Rule 1: When player's card value is higher than opponent's, they are eliminated.
     */
    @Test
    void testHigherValuePlayerIsEliminated() {
        // Setup
        user.receiveHandCard(Card.KING);      // value 6
        opponent.receiveHandCard(Card.GUARD);  // value 1
        assertTrue(user.hasHandCards(), "User should have cards initially");
        assertTrue(opponent.hasHandCards(), "Opponent should have cards initially");

        // Execute
        dowagerQueenAction.execute(mockUserInput, user, playerList, deck);

        // Verify results
        assertFalse(user.hasHandCards(), "User should be eliminated");
        assertTrue(opponent.hasHandCards(), "Opponent should still be in game");
    }

    /**
     * Rule 2: When opponent's card value is higher, they are eliminated.
     */
    @Test
    void testHigherValueOpponentIsEliminated() {
        // Setup
        user.receiveHandCard(Card.GUARD);     // value 1
        opponent.receiveHandCard(Card.KING);  // value 6
        assertTrue(user.hasHandCards(), "User should have cards initially");
        assertTrue(opponent.hasHandCards(), "Opponent should have cards initially");

        // Execute
        dowagerQueenAction.execute(mockUserInput, user, playerList, deck);

        // Verify results
        assertTrue(user.hasHandCards(), "User should still be in game");
        assertFalse(opponent.hasHandCards(), "Opponent should be eliminated");
    }

    /**
     * Rule 3: When card values are equal, no one is eliminated.
     */
    @Test
    void testEqualValuesNoElimination() {
        // Setup
        user.receiveHandCard(Card.PRIEST);     // value 2
        opponent.receiveHandCard(Card.PRIEST); // value 2
        assertTrue(user.hasHandCards(), "User should have cards initially");
        assertTrue(opponent.hasHandCards(), "Opponent should have cards initially");

        // Execute
        dowagerQueenAction.execute(mockUserInput, user, playerList, deck);

        // Verify results
        assertTrue(user.hasHandCards(), "User should still be in game");
        assertTrue(opponent.hasHandCards(), "Opponent should still be in game");
    }
}
