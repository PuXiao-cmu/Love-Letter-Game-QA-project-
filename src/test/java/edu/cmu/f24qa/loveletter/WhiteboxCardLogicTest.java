package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

public class WhiteboxCardLogicTest {

    private UserInput mockUserInput;
    private Player mockPlayer;
    private Player mockOpponent;
    private PlayerList mockPlayerList;
    private Card mockCard;
    private Card mockOpponentCard;

    @BeforeEach
    public void setUp() {
        mockUserInput = Mockito.mock(UserInput.class);
        mockPlayer = Mockito.mock(Player.class);
        mockOpponent = Mockito.mock(Player.class);
        mockPlayerList = Mockito.mock(PlayerList.class);
        mockCard = Mockito.mock(Card.class);
        mockOpponentCard = Mockito.mock(Card.class);
    }

    /**
     * Test ID: PriestT1
     * Branch ID: Priest-W1
     * Tests that playing Priest on an opponent results in viewing that opponent's handcard.
     */
    @Test
    public void testExecuteWithValidOpponentAndCardInHand() {
        PriestAction priestAction = new PriestAction();

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

    /**
     * Test ID: GuardT1
     * Branch ID: Guard-W1
     * Tests that playing Guard on an opponent and haveing a correct guess results in
     * eliminating that opponent.
     */
    // GuardAction Tests
    @Test
    public void testGuardExecuteWithCorrectGuess() {
        GuardAction guardAction = new GuardAction();

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

    /**
     * Test ID: GuardT2
     * BranchID: Guard-W2
     * Tests that playing Guard on an opponent and haveing an incorrect guess results in
     * not eliminating that opponent.
     */
    @Test
    public void testGuardExecuteWithIncorrectGuess() {
        GuardAction guardAction = new GuardAction();

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
}
