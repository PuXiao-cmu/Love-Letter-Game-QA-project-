package edu.cmu.f24qa.loveletter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class WhiteboxCardLogicTest {
    private UserInput userInput;
    private Player player;
    private Player opponent;
    private PlayerList playerList;
    private Card card;
    private Card opponentCard;

    @BeforeEach
    void setUp() {
        userInput = mock(UserInput.class);
        player = mock(Player.class);
        opponent = mock(Player.class);
        playerList = mock(PlayerList.class);
        card = mock(Card.class);
        opponentCard = mock(Card.class);
    }

    /**
     * Test ID: HT1
     * Branch ID: Handmaid-W1
     * Tests that playing Handmaid card results in protecting unprotected player.
     */
    @Test
    void testHandmaidActionNotProtected() {    // HT1
        HandmaidAction handmaidAction = new HandmaidAction();

        when(player.isProtected()).thenReturn(false);

        // Redirect System.out for capturing output
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        PrintStream originalOut = System.out; // Save original System.out
        System.setOut(new PrintStream(outputStreamCaptor));

        try {
            handmaidAction.execute(userInput, player, playerList);

            verify(player).switchProtection();

            String output = outputStreamCaptor.toString().trim();
            assertTrue(output.contains("You are now protected until your next turn"),
                    "Expected output message was not printed.");
        } finally {
            // Restore original System.out
            System.setOut(originalOut);
        }
    }

    /**
     * Test ID: HT2
     * Branch ID: Handmaid-W2
     * Tests that playing Handmaid card does nothing for protected player.
     */
    @Test
    void testHandmaidActionProtected() {    // HT2
        HandmaidAction handmaidAction = new HandmaidAction();

        when(player.isProtected()).thenReturn(true);

        // Redirect System.out for capturing output
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        PrintStream originalOut = System.out; // Save original System.out
        System.setOut(new PrintStream(outputStreamCaptor));

        try {
            handmaidAction.execute(userInput, player, playerList);

            verify(player, never()).switchProtection();

            String output = outputStreamCaptor.toString().trim();
            assertTrue(output.contains("You are now protected until your next turn"),
                    "Expected output message was not printed.");
        } finally {
            // Restore original System.out
            System.setOut(originalOut);
        }
    }

    /**
     * Test ID: CT1
     * Branch ID: Countess-W1
     * Tests that playing Countess card does not trigger any action.
     */
    @Test
    void testCountessAction() {
        CountessAction countessAction = new CountessAction();

        countessAction.execute(userInput, player, playerList);

        // Verify that no actions are triggered:
        verifyNoInteractions(player);
        verifyNoInteractions(userInput);
        verifyNoInteractions(playerList);
    }

    /**
     * Test ID: PriestT1
     * Branch ID: Priest-W1
     * Tests that playing Priest on an opponent results in viewing that opponent's hand card.
     */
    @Test
    public void testExecuteWithValidOpponentAndCardInHand() {
        PriestAction priestAction = new PriestAction();

        // Set up a valid opponent with a card
        when(userInput.getOpponent(playerList, player)).thenReturn(opponent);
        when(opponent.getName()).thenReturn("Opponent");
        when(opponent.viewHandCard(0)).thenReturn(card);

        Assertions.assertDoesNotThrow(() -> {
            priestAction.execute(userInput, player, playerList);
        });
        
        // Verify that the opponent's card was viewed
        verify(opponent).viewHandCard(0);
    }

    /**
     * Test ID: GuardT1
     * Branch ID: Guard-W1
     * Tests that playing Guard on an opponent and having a correct guess results in
     * eliminating that opponent.
     */
    @Test
    public void testGuardExecuteWithCorrectGuess() {
        GuardAction guardAction = new GuardAction();

        // Set up a valid opponent and correct guess
        when(userInput.getOpponent(playerList, player)).thenReturn(opponent);
        when(userInput.getCardName()).thenReturn("Priest");
        when(opponent.viewHandCard(0)).thenReturn(opponentCard);
        when(opponentCard.getName()).thenReturn("Priest");

        Assertions.assertDoesNotThrow(() -> {
            guardAction.execute(userInput, player, playerList);
        });

        // Verify that the opponent was eliminated
        verify(opponent).eliminate();
    }

    /**
     * Test ID: GuardT2
     * Branch ID: Guard-W2
     * Tests that playing Guard on an opponent and having an incorrect guess results in
     * not eliminating that opponent.
     */
    @Test
    public void testGuardExecuteWithIncorrectGuess() {
        GuardAction guardAction = new GuardAction();

        // Set up a valid opponent and incorrect guess
        when(userInput.getOpponent(playerList, player)).thenReturn(opponent);
        when(userInput.getCardName()).thenReturn("Baron");
        when(opponent.viewHandCard(0)).thenReturn(opponentCard);
        when(opponentCard.getName()).thenReturn("Priest");

        Assertions.assertDoesNotThrow(() -> {
            guardAction.execute(userInput, player, playerList);
        });

        // Verify that the opponent was not eliminated
        verify(opponent, never()).eliminate();
    }

    /**
     * Test ID: PrinceT1
     * Branch ID: Prince-W1
     * Tests that playing Prince card forces the opponent to be eliminated.
     * Verifies that getOpponent is called and opponent is eliminated.
     */
    @Test
    void testPrinceAction() {
        PrinceAction princeAction = new PrinceAction();
        // Setup opponent
        when(userInput.getOpponent(playerList, player)).thenReturn(opponent);

        // Execute
        princeAction.execute(userInput, player, playerList);

        // Verify the opponent was selected and eliminated
        verify(userInput).getOpponent(playerList, player);
        verify(opponent).eliminate();
    }
}
