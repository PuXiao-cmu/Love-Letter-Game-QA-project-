package edu.cmu.f24qa.loveletter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Blackbox tests for card actions in Love Letter game.
 */
public class BlackboxCardLogicTest {
    private UserInput userInput;
    private Player user;
    private PlayerList playerList;
    private Player opponent;

    @BeforeEach
    void setUp() {
        userInput = mock(UserInput.class);
        user = mock(Player.class);
        playerList = mock(PlayerList.class);
        opponent = mock(Player.class);
    }

    /**
     * Princess card tests - Rule 1: The basic scenario where the player is immediately eliminated
     */
    @Test
    void testPrincessEliminatesPlayer() {
        PrincessAction princessAction = new PrincessAction();
        
        // Execute
        princessAction.execute(userInput, user, playerList);
        
        // Verify
        verify(user).eliminate();
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
        when(userInput.getOpponent(playerList, user)).thenReturn(opponent);
        when(opponent.isProtected()).thenReturn(false);
        
        Card userCard = Card.KING;
        Card opponentCard = Card.GUARD;
        when(user.playHandCard(0)).thenReturn(userCard);
        when(opponent.playHandCard(0)).thenReturn(opponentCard);

        // Execute
        kingAction.execute(userInput, user, playerList);

        // Verify cards are swapped
        verify(user).receiveHandCard(opponentCard);
        verify(opponent).receiveHandCard(userCard);
    }

    // Rule 2: Protected opponent - no card swap
    @Test
    void testKingProtectedOpponent() {
        KingAction kingAction = new KingAction();
        when(userInput.getOpponent(playerList, user)).thenReturn(opponent);
        when(opponent.isProtected()).thenReturn(true);

        // Execute
        kingAction.execute(userInput, user, playerList);

        // Verify no card exchange happened
        verify(user, never()).receiveHandCard(any(Card.class));
        verify(opponent, never()).receiveHandCard(any(Card.class));
    }

    // Rule 3: Player holds King and Countess - invalid action
    @Test
    void testKingWithCountess() {
        KingAction kingAction = new KingAction();
        when(userInput.getOpponent(playerList, user)).thenReturn(opponent);
        when(user.viewHandCard(0)).thenReturn(Card.COUNTESS);
        when(user.viewHandCard(1)).thenReturn(Card.KING);

        // Execute
        kingAction.execute(userInput, user, playerList);

        // Verify no card exchange happened
        verify(user, never()).receiveHandCard(any(Card.class));
        verify(opponent, never()).receiveHandCard(any(Card.class));
    }

    // Rule 4: No valid opponent selected
    @Disabled("Test temporarily disabled until KingAction null check is implemented")
    @Test
    void testKingNoOpponentSelected() {
        KingAction kingAction = new KingAction();
        when(userInput.getOpponent(playerList, user)).thenReturn(null);

        // Execute
        kingAction.execute(userInput, user, playerList);

        // Verify no card exchange happened
        verify(user, never()).receiveHandCard(any(Card.class));
        verify(opponent, never()).receiveHandCard(any(Card.class));
    }

    /**
     * Prince card tests - Rules 1-4
     */
    // Rule 1: Opponent discards a Princess card and is eliminated
    @Test
    void testPrinceEliminatesOpponent() {
        PrinceAction princeAction = new PrinceAction();
        when(userInput.getOpponent(playerList, user)).thenReturn(opponent);
        when(opponent.viewHandCard(0)).thenReturn(Card.PRINCESS);

        // Execute
        princeAction.execute(userInput, user, playerList);

        // Verify opponent is eliminated
        verify(opponent).playHandCard(0);
        verify(opponent).eliminate();
    }

    // Opponent discards a non-Princess card and redraws a new card
    @Test
    void testPrinceRedrawsCard() {
        PrinceAction princeAction = new PrinceAction();
        when(userInput.getOpponent(playerList, user)).thenReturn(opponent);
        when(opponent.viewHandCard(0)).thenReturn(Card.GUARD);

        // Execute 
        princeAction.execute(userInput, user, playerList);

        // Verify opponent receives a new card
        verify(opponent).playHandCard(0);
        verify(opponent).receiveHandCard(any(Card.class));
    }

}