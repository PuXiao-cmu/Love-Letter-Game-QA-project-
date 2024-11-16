package edu.cmu.f24qa.loveletter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Whitebox tests for card actions in Love Letter game.
 */
public class WhiteboxCardLogicTest {
    private UserInput userInput;
    private Player user;
    private PlayerList playerList;
    private Player opponent;  // Add opponent as a member variable

    @BeforeEach
    void setUp() {
        // Initialize mock objects needed for all test cases
        userInput = mock(UserInput.class);
        user = mock(Player.class);
        playerList = mock(PlayerList.class);
        opponent = mock(Player.class);  // Initialize opponent
    }

    /**
     * Test ID: PT1
     * Branch ID: Princess-W1
     * Tests that playing Princess card results in immediate elimination.
     */
    @Test
    void testPrincessActionElimination() {  // PT1
        System.out.println("Running Princess Action Test");

        PrincessAction princessAction = new PrincessAction();
        princessAction.execute(userInput, user, playerList);

        verify(user).eliminate();
        verify(userInput, never()).getOpponent(any(), any());
        verifyNoMoreInteractions(playerList);
    }

    /**
     * Test ID: KT1
     * Branch ID: King-W1
     * Tests that King card successfully swaps cards between players.
     * Steps verified:
     * 1. Select opponent
     * 2. Exchange cards between players
     */
    @Test
    void testKingActionCardSwap() {  // KT1
        System.out.println("Running King Action Test");

        // Setup
        KingAction kingAction = new KingAction();
        when(userInput.getOpponent(playerList, user)).thenReturn(opponent);

        // Set user and opponent cards
        Card userCard = Card.KING;
        Card opponentCard = Card.GUARD;
        when(user.playHandCard(0)).thenReturn(userCard);
        when(opponent.playHandCard(0)).thenReturn(opponentCard);

        // Execute
        kingAction.execute(userInput, user, playerList);

        // Verify
        verify(userInput).getOpponent(playerList, user);  // Verify opponent selection
        verify(user).playHandCard(0);  // Verify user plays card
        verify(opponent).playHandCard(0);  // Verify opponent plays card
        verify(user).receiveHandCard(opponentCard);  // Verify user receives opponent's card
        verify(opponent).receiveHandCard(userCard);  // Verify opponent receives user's card
    }

    /**
     * Test ID: BT1
     * Branch ID: Baron-W1
     * Tests when user's card value is higher
     */
    @Test
    void testBaronActionUserWinsComparison() {
        // Setup
        BaronAction baronAction = new BaronAction();
        when(userInput.getOpponent(playerList, user)).thenReturn(opponent);
        
        // Simulate user's card value higher than opponent's
        when(user.viewHandCard(0)).thenReturn(Card.KING);  // Value 6
        when(opponent.viewHandCard(0)).thenReturn(Card.GUARD);  // Value 1
        
        // Execute
        baronAction.execute(userInput, user, playerList);
        
        // Verify
        verify(userInput).getOpponent(playerList, user);
        verify(opponent).eliminate();  // Opponent is eliminated
        verify(user, never()).eliminate();  // User is not eliminated
    }

    /**
     * Test ID: BT2
     * Branch ID: Baron-W2
     * Tests when user's card value is lower
     */
    @Test
    void testBaronActionUserLosesComparison() {
        // Setup
        BaronAction baronAction = new BaronAction();
        when(userInput.getOpponent(playerList, user)).thenReturn(opponent);
        
        // Simulate user's card value lower than opponent's
        when(user.viewHandCard(0)).thenReturn(Card.GUARD);  // Value 1
        when(opponent.viewHandCard(0)).thenReturn(Card.KING);  // Value 6
        
        // Execute
        baronAction.execute(userInput, user, playerList);
        
        // Verify
        verify(userInput).getOpponent(playerList, user);
        verify(user).eliminate();  // User is eliminated
        verify(opponent, never()).eliminate();  // Opponent is not eliminated
    }

    /**
     * Test ID: BT3
     * Branch ID: Baron-W3
     * Tests when cards are equal and opponent's discard value is higher
     */
    @Test
    void testBaronActionEqualCardsOpponentWins() {
        // Setup
        BaronAction baronAction = new BaronAction();
        when(userInput.getOpponent(playerList, user)).thenReturn(opponent);
        
        // Simulate equal card values
        when(user.viewHandCard(0)).thenReturn(Card.GUARD);
        when(opponent.viewHandCard(0)).thenReturn(Card.GUARD);
        
        // Simulate discard pile value comparison
        when(opponent.discardedValue()).thenReturn(5);
        when(user.discardedValue()).thenReturn(3);
        
        // Execute
        baronAction.execute(userInput, user, playerList);
        
        // Verify
        verify(userInput).getOpponent(playerList, user);
        verify(opponent).eliminate();
        verify(user, never()).eliminate();
    }

    /**
     * Test ID: BT4
     * Branch ID: Baron-W4
     * Tests when cards are equal and user's discard value is higher or equal
     */
    @Test
    void testBaronActionEqualCardsUserWins() {
        // Setup
        BaronAction baronAction = new BaronAction();
        when(userInput.getOpponent(playerList, user)).thenReturn(opponent);
        
        // Simulate equal card values
        when(user.viewHandCard(0)).thenReturn(Card.GUARD);
        when(opponent.viewHandCard(0)).thenReturn(Card.GUARD);
        
        // Simulate discard pile value comparison (user higher)
        when(opponent.discardedValue()).thenReturn(3);
        when(user.discardedValue()).thenReturn(5);
        
        // Execute
        baronAction.execute(userInput, user, playerList);
        
        verify(userInput).getOpponent(playerList, user);
        verify(user).eliminate();
        verify(opponent, never()).eliminate();
    }

    /**
     * Test ID: PrinceT1
     * Branch ID: Prince-W1
     * Tests that playing Prince card forces the opponent to be eliminated.
     * Verifies that getOpponent is called and opponent is eliminated.
     */
    @Test
    void testPrinceAction() {
        // Setup
        PrinceAction princeAction = new PrinceAction();
        when(userInput.getOpponent(playerList, user)).thenReturn(opponent);

        // Execute
        princeAction.execute(userInput, user, playerList);

        // Verify
        verify(userInput).getOpponent(playerList, user);
        verify(opponent).eliminate();
    }
}
