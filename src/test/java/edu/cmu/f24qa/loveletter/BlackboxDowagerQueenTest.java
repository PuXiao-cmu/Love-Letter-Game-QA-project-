package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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
        
        user = playerList.getPlayer("User");
        opponent = playerList.getPlayer("Opponent");
        dowagerQueenAction = new DowagerQueenAction();
        deck = new Deck();
        deck.build();
        
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

    /**
     * Rule 4: When selecting non-existent opponent
     */
    @Test
    void testNonExistentOpponentSelection() {
        Player validOpponent = new Player("ValidOpponent");
        playerList.addPlayer("ValidOpponent");
        
        when(mockUserInput.getOpponent(any(PlayerList.class), any(Player.class)))
            .thenReturn(validOpponent);
        
        user.receiveHandCard(Card.GUARD);
        validOpponent.receiveHandCard(Card.PRIEST);
        
        int initialUserCards = user.hasHandCards() ? 1 : 0;
        
        dowagerQueenAction.execute(mockUserInput, user, playerList, deck);
    
        assertTrue(user.hasHandCards() || validOpponent.hasHandCards(), 
            "Game should proceed with valid opponent");
    }

    /**
     * Rule 5: When selecting protected opponent
     */
    @Test
    void testProtectedOpponentSelection() {
        Player protectedOpponent = playerList.getPlayer("Opponent");
        Player validOpponent = new Player("ValidOpponent");
        playerList.addPlayer("ValidOpponent");
        
        when(mockUserInput.getOpponent(any(PlayerList.class), any(Player.class)))
            .thenReturn(validOpponent);
        
        user.receiveHandCard(Card.GUARD);
        protectedOpponent.receiveHandCard(Card.KING);
        validOpponent.receiveHandCard(Card.PRIEST);
        protectedOpponent.switchProtection();
        
        assertTrue(user.hasHandCards(), "User should have cards initially");
        assertTrue(protectedOpponent.hasHandCards(), "Protected opponent should have cards initially");
        assertTrue(protectedOpponent.isProtected(), "Opponent should be protected");
    
        dowagerQueenAction.execute(mockUserInput, user, playerList, deck);
    
        // Verify protected opponent was not affected
        assertTrue(protectedOpponent.hasHandCards(), "Protected opponent should still have cards");
        assertTrue(protectedOpponent.isProtected(), "Protected opponent should still be protected");
    }
}