package edu.cmu.f24qa.loveletter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PrincessActionTest {
    private UserInput mockUserInput;
    private Player mockPlayer;
    private PrincessAction princessAction;
    private PlayerList mockPlayerList;

    /**
     * Sets up the mock objects for the tests, including a UserInput for
     * retrieving user input, a Player for the player playing the card, and
     * a PlayerList for the list of players in the game. A PrincessAction
     * object is also created for testing.
     */
    @BeforeEach
    public void setup() {
        mockUserInput = mock(UserInput.class);
        mockPlayer = mock(Player.class);
        mockPlayerList = mock(PlayerList.class);
        princessAction = new PrincessAction();
    }

    /**
     * Tests that the Princess action eliminates the player holding the card
     * without asking for an opponent selection.
     */
    @Test
    public void testPrincessAction_EliminatePlayerWithoutSelectingOpponent() {
        // Execute the Princess action
        princessAction.execute(mockUserInput, mockPlayer, mockPlayerList);

        // Verify that eliminate is called on the player holding the card
        verify(mockPlayer, times(1)).eliminate();

        // Verify that no opponent selection takes place
        verify(mockUserInput, never()).getOpponent(any(), any());;

    }
}
