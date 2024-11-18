package edu.cmu.f24qa.loveletter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BlackboxCardLogicTest {
    private UserInput userInput;
    private Player user;
    private Player opponent;
    private PlayerList playerList;

    @BeforeEach
    void setUp() {
        userInput = mock(UserInput.class);
        user = mock(Player.class);
        playerList = mock(PlayerList.class);
        opponent = mock(Player.class);

        // Set up generic conditions applicable to all tests
        when(userInput.getOpponent(playerList, user)).thenReturn(opponent);
    }

    /**
     * Handmaid Card Test: Rule 1: If Player plays Handmaid card, then this player is protected.
     */
    @Test
    void testHandmaidProtectPlayer() {
        HandmaidAction handmaidAction = new HandmaidAction();
        Player realUser = new Player("testPlayer");
        user = spy(realUser); 

        handmaidAction.execute(userInput, user, playerList);

        assertTrue(user.isProtected(), "The user should be protected after playing the Handmaid card.");
    }

    /**
     * Countess Card Test: Rule 1: Play Countess Card does not trigger any action.
     */
    @Test
    void testCountessAction() {
        CountessAction countessAction = new CountessAction();

        countessAction.execute(userInput, user, playerList);

        // Verify that no actions are triggered:
        verifyNoInteractions(user);
        verifyNoInteractions(userInput);
        verifyNoInteractions(playerList);
    }

    /**
     * Blackbox test for Priest.
     * R1: If a player looks at another player’s hand, then the player sees the player’s hand.
     */
    @Test
    void testPriestAllowsViewingOpponentCard() {
        PriestAction priestAction = new PriestAction();

        // Assume the opponent has a specific card (e.g., "King")
        when(opponent.viewHandCard(0)).thenReturn(Card.KING);

        // Execute Priest action
        priestAction.execute(userInput, user, playerList);

        // Verify that the user successfully views the opponent’s card
        verify(opponent).viewHandCard(0);
    }

    /**
     * Blackbox test for Guard.
     * R1: If the guess is correct, the opponent is eliminated.
     */
    @Test
    void testGuardCorrectGuessEliminatesOpponent() {
        GuardAction guardAction = new GuardAction();

        // Set up a correct guess scenario (opponent has "Prince")
        when(userInput.getCardName()).thenReturn("Prince");
        when(opponent.viewHandCard(0)).thenReturn(Card.PRINCE);

        // Execute Guard action
        guardAction.execute(userInput, user, playerList);

        // Verify that the opponent is eliminated
        verify(opponent).eliminate();
    }

    /**
     * Blackbox test for Guard.
     * R2: If the guess is correct and the guess is a guard card, do not eliminate the opponent.
     */
    @Test
    void testGuardCorrectGuessGuardDoesNotEliminateOpponent() {
        GuardAction guardAction = new GuardAction();

        // Set up a correct guess scenario with "Guard" (invalid guess)
        when(userInput.getCardName()).thenReturn("Guard");
        when(opponent.viewHandCard(0)).thenReturn(Card.GUARD);

        // Execute Guard action
        guardAction.execute(userInput, user, playerList);

        // Verify that the opponent is not eliminated
        verify(opponent, never()).eliminate();
    }

    /**
     * Blackbox test for Guard.
     * R3: If the guess is not correct and the guess is not a guard card, do not eliminate the opponent.
     */
    @Test
    void testGuardIncorrectGuessNotGuardDoesNotEliminateOpponent() {
        GuardAction guardAction = new GuardAction();

        // Set up an incorrect guess scenario (opponent has "Prince", guess is "King")
        when(userInput.getCardName()).thenReturn("King");
        when(opponent.viewHandCard(0)).thenReturn(Card.PRINCE);

        // Execute Guard action
        guardAction.execute(userInput, user, playerList);

        // Verify that the opponent is not eliminated
        verify(opponent, never()).eliminate();
    }

    /**
     * Blackbox test for Guard.
     * R4: If the guess is not correct and the guess is a guard card, do not eliminate the opponent.
     */
    @Test
    void testGuardIncorrectGuessGuardDoesNotEliminateOpponent() {
        GuardAction guardAction = new GuardAction();

        // Set up an incorrect guess scenario with "Guard" (invalid guess)
        when(userInput.getCardName()).thenReturn("Guard");
        when(opponent.viewHandCard(0)).thenReturn(Card.PRINCE);

        // Execute Guard action
        guardAction.execute(userInput, user, playerList);

        // Verify that the opponent is not eliminated
        verify(opponent, never()).eliminate();
    }
}
