package edu.cmu.f24qa.loveletter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Whitebox tests for card actions in Love Letter game.
 */
public class WhiteboxCardLogicTest {
    private UserInput userInput;
    private Player player;
    private PlayerList playerList;
    private Player opponent;

    @BeforeEach
    void setUp() {
        // Initialize mock objects needed for all test cases
        userInput = mock(UserInput.class);
        player = mock(Player.class);
        playerList = mock(PlayerList.class);
        opponent = mock(Player.class);
    }

    /**
     * Test ID: PT1
     * Branch ID: Princess-W1
     * Tests that playing Princess card results in immediate elimination.
     */
    @Test
    void testPrincessActionElimination() {
        PrincessAction princessAction = new PrincessAction();
        princessAction.execute(userInput, player, playerList);

        verify(player).eliminate();
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
    void testKingActionCardSwap() {
        // Setup
        KingAction kingAction = new KingAction();
        when(userInput.getOpponent(playerList, player)).thenReturn(opponent);

        // Set user and opponent cards
        Card userCard = Card.KING;
        Card opponentCard = Card.GUARD;
        when(player.playHandCard(0)).thenReturn(userCard);
        when(opponent.playHandCard(0)).thenReturn(opponentCard);

        // Execute
        kingAction.execute(userInput, player, playerList);

        // Verify
        verify(userInput).getOpponent(playerList, player);
        verify(player).playHandCard(0);
        verify(opponent).playHandCard(0);
        verify(player).receiveHandCard(opponentCard);
        verify(opponent).receiveHandCard(userCard);
    }

    /**
     * Test ID: BT1
     * Branch ID: Baron-W1
     * Tests when player's card value is higher
     */
    @Test
    void testBaronActionUserWinsComparison() {
        // Setup
        BaronAction baronAction = new BaronAction();
        when(userInput.getOpponent(playerList, player)).thenReturn(opponent);
        
        // Simulate user's card value higher than opponent's
        when(player.viewHandCard(0)).thenReturn(Card.KING);  // Value 6
        when(opponent.viewHandCard(0)).thenReturn(Card.GUARD);  // Value 1
        
        // Execute
        baronAction.execute(userInput, player, playerList);
        
        // Verify
        verify(userInput).getOpponent(playerList, player);
        verify(opponent).eliminate();
        verify(player, never()).eliminate();
    }

    /**
     * Test ID: BT2
     * Branch ID: Baron-W2
     * Tests when player's card value is lower
     */
    @Test
    void testBaronActionUserLosesComparison() {
        // Setup
        BaronAction baronAction = new BaronAction();
        when(userInput.getOpponent(playerList, player)).thenReturn(opponent);
        
        // Simulate user's card value lower than opponent's
        when(player.viewHandCard(0)).thenReturn(Card.GUARD);  // Value 1
        when(opponent.viewHandCard(0)).thenReturn(Card.KING);  // Value 6
        
        // Execute
        baronAction.execute(userInput, player, playerList);
        
        // Verify
        verify(userInput).getOpponent(playerList, player);
        verify(player).eliminate();
        verify(opponent, never()).eliminate();
    }

    /**
     * Test ID: BT3
     * Branch ID: Baron-W3
     * Tests when cards are equal and opponent's discard value is higher
     */
    @Disabled("Test needs to be updated due to rule changes - equal cards no longer check discard values")
    @Test
    void testBaronActionEqualCardsOpponentWins() {
        // Setup
        BaronAction baronAction = new BaronAction();
        when(userInput.getOpponent(playerList, player)).thenReturn(opponent);
        
        // Simulate equal card values
        when(player.viewHandCard(0)).thenReturn(Card.GUARD);
        when(opponent.viewHandCard(0)).thenReturn(Card.GUARD);
        
        // Simulate discard pile value comparison
        when(opponent.discardedValue()).thenReturn(5);
        when(player.discardedValue()).thenReturn(3);
        
        // Execute
        baronAction.execute(userInput, player, playerList);
        
        // Verify
        verify(userInput).getOpponent(playerList, player);
        verify(opponent).eliminate();
        verify(player, never()).eliminate();
    }

    /**
     * Test ID: BT4
     * Branch ID: Baron-W4
     * Tests when cards are equal and user's discard value is higher or equal
     */
    @Disabled("Test needs to be updated due to rule changes - equal cards no longer check discard values")
    @Test
    void testBaronActionEqualCardsUserWins() {
        // Setup
        BaronAction baronAction = new BaronAction();
        when(userInput.getOpponent(playerList, player)).thenReturn(opponent);
        
        // Simulate equal card values
        when(player.viewHandCard(0)).thenReturn(Card.GUARD);
        when(opponent.viewHandCard(0)).thenReturn(Card.GUARD);
        
        // Simulate discard pile value comparison (player higher)
        when(opponent.discardedValue()).thenReturn(3);
        when(player.discardedValue()).thenReturn(5);
        
        // Execute
        baronAction.execute(userInput, player, playerList);
        
        verify(userInput).getOpponent(playerList, player);
        verify(player).eliminate();
        verify(opponent, never()).eliminate();
    }

    /**
     * Test ID: BT5
     * Branch ID: Baron-W5
     * Tests when cards are equal with no elimination check
     */
    @Test
    void testBaronActionEqualCardsNoElimination() {
        // Setup
        BaronAction baronAction = new BaronAction();
        when(userInput.getOpponent(playerList, player)).thenReturn(opponent);
        
        // Simulate equal card values
        when(player.viewHandCard(0)).thenReturn(Card.GUARD); 
        when(opponent.viewHandCard(0)).thenReturn(Card.GUARD);
        
        // Execute
        baronAction.execute(userInput, player, playerList);
        
        // Verify
        verify(userInput).getOpponent(playerList, player);
        verify(player, never()).eliminate();
        verify(opponent, never()).eliminate();
    }
}
