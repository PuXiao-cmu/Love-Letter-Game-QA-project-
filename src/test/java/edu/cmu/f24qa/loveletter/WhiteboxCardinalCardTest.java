package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

/**
 * White-box tests for the CardinalAction class to achieve 100% branch coverage.
 */
public class WhiteboxCardinalCardTest {

    private UserInput userInput;
    private Player user;
    private Player player1;
    private Player player2;
    private PlayerList players;
    private Deck deck;
    private CardinalAction cardinalAction;

    /**
     * Sets up the test environment with necessary mock objects.
     */
    @BeforeEach
    void setUp() {
        userInput = mock(UserInput.class);
        user = mock(Player.class);
        player1 = mock(Player.class);
        player2 = mock(Player.class);
        players = mock(PlayerList.class);
        deck = mock(Deck.class);
        cardinalAction = new CardinalAction();
    }

    /**
     * CardinalT1
     * Tests the execution of the CardinalAction where two valid players are selected and hands are swapped.
     */
    @Test
    void testExecuteWithValidPlayers() {
        // Mock user input to select two different players
        when(userInput.getOpponent(players, user, true)).thenReturn(player1, player2);

        // Mock behavior for hand cards
        Card card1 = mock(Card.class);
        Card card2 = mock(Card.class);
        when(player1.playHandCard(0)).thenReturn(card1);
        when(player2.playHandCard(0)).thenReturn(card2);

        // Execute the Cardinal action
        cardinalAction.execute(userInput, user, players, deck);

        // Verify interactions
        verify(userInput, times(2)).getOpponent(players, user, true); // Two players selected
        verify(player1, times(1)).playHandCard(0);
        verify(player2, times(1)).playHandCard(0);
        verify(player1, times(1)).receiveHandCard(card2);
        verify(player2, times(1)).receiveHandCard(card1);
        verify(player1, times(1)).viewHandCard(0);
    }

    /**
     * CardinalT2
     * Tests the behavior when the user tries to select the same player twice.
     */
    @Test
    void testExecuteWithSamePlayerSelectedTwice() {
        // Mock user input to initially select the same player
        when(userInput.getOpponent(players, user, true)).thenReturn(player1, player1, player2);

        // Mock behavior for hand cards
        Card card1 = mock(Card.class);
        Card card2 = mock(Card.class);
        when(player1.playHandCard(0)).thenReturn(card1);
        when(player2.playHandCard(0)).thenReturn(card2);

        // Execute the Cardinal action
        cardinalAction.execute(userInput, user, players, deck);

        // Verify interactions
        verify(userInput, times(3)).getOpponent(players, user, true); // Called three times due to retry
        verify(player1, times(1)).playHandCard(0);
        verify(player2, times(1)).playHandCard(0);
        verify(player1, times(1)).receiveHandCard(card2);
        verify(player2, times(1)).receiveHandCard(card1);
        verify(player1, times(1)).viewHandCard(0);
    }
}
