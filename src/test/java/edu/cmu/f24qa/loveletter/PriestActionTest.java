package edu.cmu.f24qa.loveletter;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the PriestAction class to verify that playing a Priest card
 * reveals a card from the selected opponent's hand.
 */
class PriestActionTest {

    private UserInput mockUserInput;
    private Player mockUser;
    private PriestAction priestAction;
    private PlayerList mockPlayerList;
    private Player mockOpponent;

    /**
     * Sets up the mock objects and the PriestAction instance before each test.
     * Ensures that viewHandCard(0) on mockOpponent returns a valid card to avoid NullPointerExceptions.
     * Ensures that getName() on mockOpponent returns a valid name to avoid NullPointerExceptions.
     */
    @BeforeEach
    void setUp() {
        mockUserInput = mock(UserInput.class);
        mockUser = mock(Player.class);
        mockPlayerList = mock(PlayerList.class);
        mockOpponent = mock(Player.class);

        priestAction = new PriestAction();
        
        // Set up an opponent and the card in the opponent's hand
        when(mockOpponent.viewHandCard(0)).thenReturn(Card.GUARD);
        when(mockOpponent.getName()).thenReturn("Opponent");
        when(mockUserInput.getOpponent(mockPlayerList, mockUser)).thenReturn(mockOpponent);        
    }

    /**
     * Tests that playing a Priest card reveals a card from the opponent's hand.
     */
    @Test
    void testPlayingPriestCardRevealsOpponentCard() {
        // Capture output from System.out
        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outputStream));

        // Execute the Priest action
        priestAction.execute(mockUserInput, mockUser, mockPlayerList);

        // Verify that the output matches the expected reveal message
        String expectedOutput = "Opponent shows you a Guard (1)";
        String actualOutput = outputStream.toString().trim();
        assertEquals(expectedOutput, actualOutput, "Priest card should reveal the opponent's card.");
    }
}

