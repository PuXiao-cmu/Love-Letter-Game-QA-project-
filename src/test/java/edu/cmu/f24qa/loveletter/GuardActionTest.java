package edu.cmu.f24qa.loveletter;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the GuardAction class that focus on verifying that
 * playing a Guard card prompts the selection of an opponent.
 */
class GuardActionTest {

    private UserInput mockUserInput;
    private Player mockUser;
    private GuardAction guardAction;
    private PlayerList mockPlayerList;
    private Player mockOpponent;
    private Deck deck;

    /**
     * Sets up the mock objects and the GuardAction instance before each test.
     * Ensures that viewHandCard(0) on mockOpponent returns a valid card to avoid NullPointerExceptions.
     */
    @BeforeEach
    void setUp() {
        deck = new Deck();
        deck.build();
        deck.shuffle();
        mockUserInput = mock(UserInput.class);
        mockUser = mock(Player.class);
        mockPlayerList = mock(PlayerList.class);
        mockOpponent = mock(Player.class);

        guardAction = new GuardAction();
        
        // when(mockUserInput.getCardName()).thenReturn("Guard");
        when(mockUserInput.getCardNumber()).thenReturn(1);
        when(mockOpponent.viewHandCard(0)).thenReturn(Card.PRIEST);
    }

    /**
     * Tests that playing a Guard card allows an opponent to be selected.
     * Verifies that the getOpponent method is called on UserInput when GuardAction is executed.
     */
    @Test
    void testPlayingGuardCardAllowsOpponentSelection() {
        when(mockUserInput.getOpponent(mockPlayerList, mockUser)).thenReturn(mockOpponent);

        guardAction.execute(mockUserInput, mockUser, mockPlayerList, deck);

        verify(mockUserInput, times(1)).getOpponent(mockPlayerList, mockUser);
    }
}
