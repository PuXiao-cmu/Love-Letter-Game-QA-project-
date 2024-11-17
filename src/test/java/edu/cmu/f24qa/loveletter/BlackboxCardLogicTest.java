package edu.cmu.f24qa.loveletter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Blackbox tests for card actions in Love Letter game.
 */
public class BlackboxCardLogicTest {
    private UserInput userInput;
    private Player player;
    private PlayerList playerList;
    private Player opponent;

    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        userInput = mock(UserInput.class);
        player = mock(Player.class);
        playerList = mock(PlayerList.class);
        opponent = mock(Player.class);
    }

    @AfterEach
    void restoreSystemOut() {
        System.setOut(originalOut); 
    }

    /**
     * Princess card tests - Rule 1: The basic scenario where the player is immediately eliminated
     */
    @Test
    void testPrincessEliminatesPlayer() {
        PrincessAction princessAction = new PrincessAction();
        
        // Execute
        princessAction.execute(userInput, player, playerList);
        
        // Verify
        verify(player).eliminate();
        verify(userInput, never()).getOpponent(any(), any());
        verifyNoMoreInteractions(playerList);
    }

    /**
     * King card tests - Rules 1-4
     */
    // Rule 1: Normal case - successful card swap
    @Test
    void testKingSuccessfulCardSwap() {
        KingAction kingAction = new KingAction();
        when(userInput.getOpponent(playerList, player)).thenReturn(opponent);
        when(opponent.isProtected()).thenReturn(false);
        
        Card userCard = Card.KING;
        Card opponentCard = Card.GUARD;
        when(player.playHandCard(0)).thenReturn(userCard);
        when(opponent.playHandCard(0)).thenReturn(opponentCard);

        // Execute
        kingAction.execute(userInput, player, playerList);

        // Verify cards are swapped
        verify(player).receiveHandCard(opponentCard);
        verify(opponent).receiveHandCard(userCard);
    }

    // Rule 2: Protected opponent - no card swap
    @Test
    void testKingProtectedOpponent() {
        KingAction kingAction = new KingAction();
        when(userInput.getOpponent(playerList, player)).thenReturn(opponent);
        when(opponent.isProtected()).thenReturn(true);

        // Execute
        kingAction.execute(userInput, player, playerList);

        // Verify no card exchange happened
        verify(player, never()).receiveHandCard(any(Card.class));
        verify(opponent, never()).receiveHandCard(any(Card.class));
    }

    // Rule 3: No-existed opponent selected
    @Test
    void testKingNoOpponentSelected() {
        KingAction kingAction = new KingAction();
        when(userInput.getOpponent(playerList, player)).thenReturn(null);

        // Execute
        kingAction.execute(userInput, player, playerList);

        // Verify no card exchange happened
        verify(player, never()).receiveHandCard(any(Card.class));
        verify(opponent, never()).receiveHandCard(any(Card.class));
    }

    /**
     * Rule 1: Player's card has higher value than opponent's
     * Conditions:
     * - Valid opponent selected
     * - Player card > Opponent card
     * Result:
     * - Opponent is eliminated
     * - Show comparison result
     */
    @Test
    void testRule1_PlayerCardHigherValue() throws IOException {
        BaronAction baronAction = new BaronAction();
        when(userInput.getOpponent(playerList, player)).thenReturn(opponent);
        when(player.viewHandCard(0)).thenReturn(Card.KING);    // value 6
        when(opponent.viewHandCard(0)).thenReturn(Card.GUARD); // value 1
        
        // Set up System.out capture
        try (ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
            PrintStream temporaryOut = new PrintStream(outputStreamCaptor)) {
            System.setOut(temporaryOut);
        
            // Execute
            baronAction.execute(userInput, player, playerList);
            
            // Verify
            verify(userInput).getOpponent(playerList, player);
            verify(player).viewHandCard(0);
            verify(opponent).viewHandCard(0);
            verify(opponent).eliminate();
            verify(player, never()).eliminate();
            
            String output = outputStreamCaptor.toString().trim();
            assertTrue(output.contains("You have won the comparison!"));
        }
    }

    /**
     * Rule 2: Player's card has lower value than opponent's
     * Conditions:
     * - Valid opponent selected
     * - Player card < Opponent card
     * Result:
     * - Player is eliminated
     * - Show comparison result
     */
    @Test
    void testRule2_PlayerCardLowerValue() throws IOException {
        BaronAction baronAction = new BaronAction();
        when(userInput.getOpponent(playerList, player)).thenReturn(opponent);
        when(player.viewHandCard(0)).thenReturn(Card.GUARD);   // value 1
        when(opponent.viewHandCard(0)).thenReturn(Card.KING);  // value 6
        
        // Set up System.out capture
        try (ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
            PrintStream temporaryOut = new PrintStream(outputStreamCaptor)) {
            System.setOut(temporaryOut);
        
            // Execute
            baronAction.execute(userInput, player, playerList);
            
            // Verify
            verify(userInput).getOpponent(playerList, player);
            verify(player).viewHandCard(0);
            verify(opponent).viewHandCard(0);
            verify(player).eliminate();
            verify(opponent, never()).eliminate();
            
            String output = outputStreamCaptor.toString().trim();
            assertTrue(output.contains("You have lost the comparison"));
        }
    }

    /**
     * Rule 3: Cards have equal value
     * Conditions:
     * - Valid opponent selected
     * - Cards have equal value
     * Result:
     * - No one is eliminated
     * - Show comparison result
     */
    @Test
    void testRule3_EqualCardValues() throws IOException {
        BaronAction baronAction = new BaronAction();
        when(userInput.getOpponent(playerList, player)).thenReturn(opponent);
        when(player.viewHandCard(0)).thenReturn(Card.PRIEST);  // value 2
        when(opponent.viewHandCard(0)).thenReturn(Card.PRIEST); // value 2
        
        // Set up System.out capture
        try (ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
            PrintStream temporaryOut = new PrintStream(outputStreamCaptor)) {
            System.setOut(temporaryOut);
        
            // Execute
            baronAction.execute(userInput, player, playerList);
            
            // Verify
            verify(userInput).getOpponent(playerList, player);
            verify(player).viewHandCard(0);
            verify(opponent).viewHandCard(0);
            verify(player, never()).eliminate();
            verify(opponent, never()).eliminate();
            
            String output = outputStreamCaptor.toString().trim();
            assertTrue(output.contains("You have the same card - no one is eliminated!"));
        }
    }

    /**
     * Rule 4: Invalid opponent selection
     * Conditions:
     * - Player selects non-existent opponent
     * Result:
     * - Action fails
     */
    @Test
    void testRule4_InvalidOpponentSelection() throws IOException {
        BaronAction baronAction = new BaronAction();
        when(userInput.getOpponent(playerList, player)).thenReturn(null);
        
        // Set up System.out capture
        try (ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
            PrintStream temporaryOut = new PrintStream(outputStreamCaptor)) {
            System.setOut(temporaryOut);
        
            // Execute
            baronAction.execute(userInput, player, playerList);
            
            // Verify
            verify(userInput).getOpponent(playerList, player);
            verify(player, never()).viewHandCard(anyInt());
            verify(opponent, never()).viewHandCard(anyInt());
            verify(player, never()).eliminate();
            verify(opponent, never()).eliminate();
            
            String output = outputStreamCaptor.toString().trim();
            assertTrue(output.contains("No valid opponent selected."));
        }
    }
}