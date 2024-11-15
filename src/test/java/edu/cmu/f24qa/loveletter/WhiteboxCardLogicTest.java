package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

public class WhiteboxCardLogicTest {

    private PriestAction priestAction;
    private GuardAction guardAction;
    private UserInput mockUserInput;
    private Player mockPlayer;
    private Player mockOpponent;
    private PlayerList mockPlayerList;
    private Card mockCard;
    private Card mockOpponentCard;

    @BeforeEach
    public void setUp() {
        priestAction = new PriestAction();
        guardAction = new GuardAction();
        mockUserInput = Mockito.mock(UserInput.class);
        mockPlayer = Mockito.mock(Player.class);
        mockOpponent = Mockito.mock(Player.class);
        mockPlayerList = Mockito.mock(PlayerList.class);
        mockCard = Mockito.mock(Card.class);
        mockOpponentCard = Mockito.mock(Card.class);
    }

    // PriestAction Tests
    @Test
    public void testExecuteWithValidOpponentAndCardInHand() {
        // Set up a valid opponent with a card
        Mockito.when(mockUserInput.getOpponent(mockPlayerList, mockPlayer)).thenReturn(mockOpponent);
        Mockito.when(mockOpponent.getName()).thenReturn("Opponent");
        Mockito.when(mockOpponent.viewHandCard(0)).thenReturn(mockCard);

        Assertions.assertDoesNotThrow(() -> {
            priestAction.execute(mockUserInput, mockPlayer, mockPlayerList);
        });
        
        // Verify that the opponent's card was viewed
        Mockito.verify(mockOpponent).viewHandCard(0);
    }

    // @Test
    // public void testExecuteWithOpponentNoCardsInHand() {
    //     // Set up a valid opponent with no cards
    //     Mockito.when(mockUserInput.getOpponent(mockPlayerList, mockPlayer)).thenReturn(mockOpponent);
    //     Mockito.when(mockOpponent.getName()).thenReturn("Opponent");
    //     Mockito.when(mockOpponent.viewHandCard(0)).thenThrow(new IndexOutOfBoundsException("No cards in hand"));

    //     // Check that an exception is thrown if there are no cards in hand
    //     Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
    //         priestAction.execute(mockUserInput, mockPlayer, mockPlayerList);
    //     });
    // }

    // @Test
    // public void testExecuteWithNullOpponent() {
    //     // Set up the user input to return a null opponent
    //     Mockito.when(mockUserInput.getOpponent(mockPlayerList, mockPlayer)).thenReturn(null);

    //     // NullPointerException
    //     Assertions.assertThrows(NullPointerException.class, () -> {
    //         priestAction.execute(mockUserInput, mockPlayer, mockPlayerList);
    //     });
    // }

    // GuardAction Tests
    @Test
    public void testGuardExecuteWithCorrectGuess() {
        // Set up a valid opponent and correct guess
        Mockito.when(mockUserInput.getOpponent(mockPlayerList, mockPlayer)).thenReturn(mockOpponent);
        Mockito.when(mockUserInput.getCardName()).thenReturn("Priest");
        Mockito.when(mockOpponent.viewHandCard(0)).thenReturn(mockOpponentCard);
        Mockito.when(mockOpponentCard.getName()).thenReturn("Priest");

        Assertions.assertDoesNotThrow(() -> {
            guardAction.execute(mockUserInput, mockPlayer, mockPlayerList);
        });

        // Verify that the opponent was eliminated
        Mockito.verify(mockOpponent).eliminate();
    }

    @Test
    public void testGuardExecuteWithIncorrectGuess() {
        // Set up a valid opponent and incorrect guess
        Mockito.when(mockUserInput.getOpponent(mockPlayerList, mockPlayer)).thenReturn(mockOpponent);
        Mockito.when(mockUserInput.getCardName()).thenReturn("Baron");
        Mockito.when(mockOpponent.viewHandCard(0)).thenReturn(mockOpponentCard);
        Mockito.when(mockOpponentCard.getName()).thenReturn("Priest");

        Assertions.assertDoesNotThrow(() -> {
            guardAction.execute(mockUserInput, mockPlayer, mockPlayerList);
        });

        // Verify that the opponent was not eliminated
        Mockito.verify(mockOpponent, Mockito.never()).eliminate();
    }

    // @Test
    // public void testGuardExecuteWithNullOpponent() {
    //     // Set up the user input to return a null opponent
    //     Mockito.when(mockUserInput.getOpponent(mockPlayerList, mockPlayer)).thenReturn(null);

    //     // NullPointerException
    //     Assertions.assertThrows(NullPointerException.class, () -> {
    //         guardAction.execute(mockUserInput, mockPlayer, mockPlayerList);
    //     });
    // }
}
