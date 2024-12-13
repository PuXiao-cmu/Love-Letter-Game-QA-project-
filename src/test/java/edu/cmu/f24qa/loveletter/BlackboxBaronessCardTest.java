package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

/**
 * Black-box tests for the BaronessAction class based on defined rules.
 */
public class BlackboxBaronessCardTest {

    private UserInput userInput;
    private Player user;
    private Player opponent1;
    private Player opponent2;
    private PlayerList players;
    private Deck deck;
    private BaronessAction baronessAction;

    /**
     * Sets up the test environment with necessary mock objects.
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
     * Rule 1: If the player chooses one valid opponent, then the player sees the one opponent’s hand card.
     */
    @Test
    void testPlayerSeesSingleOpponentHandCard() {
        // Set up the input and opponent behavior
        when(userInput.getNumOpponent()).thenReturn(1);
        when(userInput.getOpponent(players, user)).thenReturn(opponent1);
        when(opponent1.viewHandCard(0)).thenReturn(Card.GUARD);

        // Execute the action
        baronessAction.execute(userInput, user, players, deck);

        // Verify that the hand card is revealed
        verify(opponent1).viewHandCard(0);
    }

    /**
     * Rule 2: If the player chooses two valid opponents, then the player sees the two opponents’ hand cards.
     */
    @Test
    void testPlayerSeesTwoOpponentsHandCards() {
        // Set up the input and opponents' behavior
        when(userInput.getNumOpponent()).thenReturn(2);
        when(userInput.getOpponent(players, user)).thenReturn(opponent1).thenReturn(opponent2);
        when(opponent1.viewHandCard(0)).thenReturn(Card.GUARD);
        when(opponent2.viewHandCard(0)).thenReturn(Card.PRIEST);

        // Execute the action
        baronessAction.execute(userInput, user, players, deck);

        // Verify that the hand cards of both opponents have been viewed
        verify(opponent1).viewHandCard(0);
        verify(opponent2).viewHandCard(0);
    }
}
