package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

/**
 * Test class for the BaronessAction class, providing whitebox tests
 * to verify the behavior of the execute method under various conditions.
 */
public class WhiteboxBaronessCardTest {

    private UserInput userInput;
    private Player user;
    private Player opponent1;
    private Player opponent2;
    private PlayerList players;
    private Deck deck;
    private BaronessAction baronessAction;

    /**
     * Sets up the necessary mock objects and initializes the BaronessAction
     * instance before each test.
     */
    @BeforeEach
    void setUp() {
        userInput = mock(UserInput.class);
        user = mock(Player.class);
        opponent1 = mock(Player.class);
        opponent2 = mock(Player.class);
        players = mock(PlayerList.class);
        deck = mock(Deck.class);
        baronessAction = new BaronessAction();
    }

    /**
     * Test ID: BaronessT1
     * Tests the execute method when the user selects only one opponent.
     * Verifies that the correct interactions occur and the opponent's
     * hand card is revealed as expected.
     */
    @Test
    void testExecuteWithOneOpponent() {
        // Set up the mocks
        when(userInput.getNumOpponent()).thenReturn(1);
        when(userInput.getOpponent(players, user)).thenReturn(opponent1);
        when(opponent1.getName()).thenReturn("Opponent1");
        when(opponent1.viewHandCard(0)).thenReturn(Card.GUARD);

        // Capture the console output
        baronessAction.execute(userInput, user, players, deck);

        // Verify interactions and outputs
        verify(userInput, times(1)).getNumOpponent();
        verify(userInput, times(1)).getOpponent(players, user);
        verify(opponent1, times(1)).getName();
        verify(opponent1, times(1)).viewHandCard(0);
        verifyNoMoreInteractions(userInput, user, players, deck, opponent1);
    }

    /**
     * Test ID: BaronessT2
     * Tests the execute method when the user selects two opponents.
     * Verifies that the correct interactions occur and both opponents'
     * hand cards are revealed as expected.
     */
    @Test
    void testExecuteWithTwoOpponents() {
        // Set up the mocks
        when(userInput.getNumOpponent()).thenReturn(2);
        when(userInput.getOpponent(players, user)).thenReturn(opponent1).thenReturn(opponent2);
        when(opponent1.getName()).thenReturn("Opponent1");
        when(opponent1.viewHandCard(0)).thenReturn(Card.GUARD);
        when(opponent2.getName()).thenReturn("Opponent2");
        when(opponent2.viewHandCard(0)).thenReturn(Card.PRIEST);

        // Execute the method
        baronessAction.execute(userInput, user, players, deck);

        // Verify interactions and outputs
        verify(userInput, times(1)).getNumOpponent();
        verify(userInput, times(2)).getOpponent(players, user);
        verify(opponent1, times(1)).getName();
        verify(opponent1, times(1)).viewHandCard(0);
        verify(opponent2, times(1)).getName();
        verify(opponent2, times(1)).viewHandCard(0);
        verifyNoMoreInteractions(userInput, user, players, deck, opponent1, opponent2);
    }
}
