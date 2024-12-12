package edu.cmu.f24qa.loveletter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class WhiteboxCardLogicTest {
    private UserInput userInput;
    private Player player;
    private PlayerList playerList;
    private Player opponent;
    private Card card;
    private Card opponentCard;
    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck();
        deck.build();
        deck.shuffle();
        userInput = mock(UserInput.class);
        player = mock(Player.class);
        playerList = mock(PlayerList.class);
        opponent = mock(Player.class);
        card = mock(Card.class);
        opponentCard = mock(Card.class);
    }

    /**
     * Test ID: PT1
     * Branch ID: Princess-W1
     * Tests that playing Princess card results in immediate elimination.
     */
    @Test
    void testPrincessActionElimination() {
        PrincessAction princessAction = new PrincessAction();
        princessAction.execute(userInput, player, playerList, deck);

        verify(player).eliminate();
        verify(userInput, never()).getOpponent(any(), any());
        verifyNoMoreInteractions(playerList);
    }

    /**
     * Test ID: KT1
     * Branch ID: King-W1
     * Tests that King card successfully swaps cards between players.
     * Steps verified:
     * 1. Select opponent
     * 2. Exchange cards between players
     */
    @Test
    void testKingActionCardSwap() {
        // Setup
        KingAction kingAction = new KingAction();
        when(userInput.getOpponent(playerList, player)).thenReturn(opponent);

        // Set user and opponent cards
        Card userCard = Card.KING;
        Card opponentCard = Card.GUARD;
        when(player.playHandCard(0)).thenReturn(userCard);
        when(opponent.playHandCard(0)).thenReturn(opponentCard);

        // Execute
        kingAction.execute(userInput, player, playerList, deck);

        // Verify
        verify(userInput).getOpponent(playerList, player);
        verify(player).playHandCard(0);
        verify(opponent).playHandCard(0);
        verify(player).receiveHandCard(opponentCard);
        verify(opponent).receiveHandCard(userCard);
    }

    /**
     * Test ID: BT1
     * Branch ID: Baron-W1
     * Tests when player's card value is higher
     */
    @Test
    void testBaronActionUserWinsComparison() {
        // Setup
        BaronAction baronAction = new BaronAction();
        when(userInput.getOpponent(playerList, player)).thenReturn(opponent);
        
        // Simulate user's card value higher than opponent's
        when(player.viewHandCard(0)).thenReturn(Card.KING);  // Value 6
        when(opponent.viewHandCard(0)).thenReturn(Card.GUARD);  // Value 1
        
        // Execute
        baronAction.execute(userInput, player, playerList, deck);
        
        // Verify
        verify(userInput).getOpponent(playerList, player);
        verify(opponent).eliminate();
        verify(player, never()).eliminate();
    }

    /**
     * Test ID: BT2
     * Branch ID: Baron-W2
     * Tests when player's card value is lower
     */
    @Test
    void testBaronActionUserLosesComparison() {
        // Setup
        BaronAction baronAction = new BaronAction();
        when(userInput.getOpponent(playerList, player)).thenReturn(opponent);
        
        // Simulate user's card value lower than opponent's
        when(player.viewHandCard(0)).thenReturn(Card.GUARD);  // Value 1
        when(opponent.viewHandCard(0)).thenReturn(Card.KING);  // Value 6
        
        // Execute
        baronAction.execute(userInput, player, playerList, deck);
        
        // Verify
        verify(userInput).getOpponent(playerList, player);
        verify(player).eliminate();
        verify(opponent, never()).eliminate();
    }

    /**
     * Test ID: BT3
     * Branch ID: Baron-W3
     * Tests when cards are equal and opponent's discard value is higher
     */
    @Disabled("Test needs to be updated due to rule changes - equal cards no longer check discard values")
    @Test
    void testBaronActionEqualCardsOpponentWins() {
        // Setup
        BaronAction baronAction = new BaronAction();
        when(userInput.getOpponent(playerList, player)).thenReturn(opponent);
        
        // Simulate equal card values
        when(player.viewHandCard(0)).thenReturn(Card.GUARD);
        when(opponent.viewHandCard(0)).thenReturn(Card.GUARD);
        
        // Simulate discard pile value comparison
        when(opponent.discardedValue()).thenReturn(5);
        when(player.discardedValue()).thenReturn(3);
        
        // Execute
        baronAction.execute(userInput, player, playerList, deck);
        
        // Verify
        verify(userInput).getOpponent(playerList, player);
        verify(opponent).eliminate();
        verify(player, never()).eliminate();
    }

    /**
     * Test ID: BT4
     * Branch ID: Baron-W4
     * Tests when cards are equal and user's discard value is higher or equal
     */
    @Disabled("Test needs to be updated due to rule changes - equal cards no longer check discard values")
    @Test
    void testBaronActionEqualCardsUserWins() {
        // Setup
        BaronAction baronAction = new BaronAction();
        when(userInput.getOpponent(playerList, player)).thenReturn(opponent);
        
        // Simulate equal card values
        when(player.viewHandCard(0)).thenReturn(Card.GUARD);
        when(opponent.viewHandCard(0)).thenReturn(Card.GUARD);
        
        // Simulate discard pile value comparison (player higher)
        when(opponent.discardedValue()).thenReturn(3);
        when(player.discardedValue()).thenReturn(5);
        
        // Execute
        baronAction.execute(userInput, player, playerList, deck);
        
        verify(userInput).getOpponent(playerList, player);
        verify(player).eliminate();
        verify(opponent, never()).eliminate();
    }

    /**
     * Test ID: BT5
     * Branch ID: Baron-W5
     * Tests when cards are equal with no elimination check
     */
    @Test
    void testBaronActionEqualCardsNoElimination() {
        // Setup
        BaronAction baronAction = new BaronAction();
        when(userInput.getOpponent(playerList, player)).thenReturn(opponent);
        
        // Simulate equal card values
        when(player.viewHandCard(0)).thenReturn(Card.GUARD); 
        when(opponent.viewHandCard(0)).thenReturn(Card.GUARD);
        
        // Execute
        baronAction.execute(userInput, player, playerList, deck);
        
        // Verify
        verify(userInput).getOpponent(playerList, player);
        verify(player, never()).eliminate();
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
            handmaidAction.execute(userInput, player, playerList, deck);

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
            handmaidAction.execute(userInput, player, playerList, deck);

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

        countessAction.execute(userInput, player, playerList, deck);

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
            priestAction.execute(userInput, player, playerList, deck);
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
    @Disabled("Test needs to be updated due to rule changes")
    @Test
    public void testGuardExecuteWithCorrectGuess() {
        GuardAction guardAction = new GuardAction();

        // Set up a valid opponent and correct guess
        when(userInput.getOpponent(playerList, player)).thenReturn(opponent);
        when(userInput.getCardName()).thenReturn("Priest");
        when(opponent.viewHandCard(0)).thenReturn(opponentCard);
        when(opponentCard.getName()).thenReturn("Priest");

        Assertions.assertDoesNotThrow(() -> {
            guardAction.execute(userInput, player, playerList, deck);
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
    @Disabled("Test needs to be updated due to rule changes")
    @Test
    public void testGuardExecuteWithIncorrectGuess() {
        GuardAction guardAction = new GuardAction();

        // Set up a valid opponent and incorrect guess
        when(userInput.getOpponent(playerList, player)).thenReturn(opponent);
        when(userInput.getCardName()).thenReturn("Baron");
        when(opponent.viewHandCard(0)).thenReturn(opponentCard);
        when(opponentCard.getName()).thenReturn("Priest");

        Assertions.assertDoesNotThrow(() -> {
            guardAction.execute(userInput, player, playerList, deck);
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
    @Disabled("Test needs to be updated due to rule changes")
    @Test
    void testPrinceAction() {
        PrinceAction princeAction = new PrinceAction();
        // Setup opponent
        when(userInput.getOpponent(playerList, player)).thenReturn(opponent);

        // Execute
        princeAction.execute(userInput, player, playerList, deck);

        // Verify the opponent was selected and eliminated
        verify(userInput).getOpponent(playerList, player);
        verify(opponent).eliminate();
    }
}
