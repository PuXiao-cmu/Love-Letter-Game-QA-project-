package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

/**
 * White-box tests for the SycophantAction class
 */
public class WhiteboxSycophantCardTest {

    private UserInput userInput;
    private Player user;
    private Player selectedPlayer;
    private PlayerList players;
    private Deck deck;
    private SycophantAction sycophantAction;

    /**
     * Sets up the test environment with necessary mock objects.
     */
    @BeforeEach
    void setUp() {
        userInput = mock(UserInput.class);
        user = mock(Player.class);
        selectedPlayer = mock(Player.class);
        players = mock(PlayerList.class);
        deck = mock(Deck.class);
        sycophantAction = new SycophantAction();
    }

    /**
     * Test ID: Sycophant-W1
     * Tests the execution of the SycophantAction where a player is selected.
     */
    @Test
    void testExecuteSetsSycophantChoice() {
        // Mock behavior for selecting a player
        when(userInput.getOpponent(players, user, true)).thenReturn(selectedPlayer);

        // Execute the Sycophant action
        sycophantAction.execute(userInput, user, players, deck);

        // Verify that the correct player was selected and set as the Sycophant choice
        verify(userInput, times(1)).getOpponent(players, user, true);
        verify(userInput, times(1)).setSycophantChoice(selectedPlayer);
    }
}
