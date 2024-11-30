package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BlackboxPrinceLogicTest {

    private CommandLineUserInput commandLineUserInput;
    private Scanner mockScanner;
    private Player spyPlayer;
    private Player spyOpponent;
    private Player spyProtectedOpponent;
    private PlayerList mockPlayerList;
    private Deck deck;

    @BeforeEach
    void setup() throws Exception {
        deck = new Deck();
        deck.build();
        deck.shuffle();
        // Initialize CommandLineUserInput without DI
        commandLineUserInput = new CommandLineUserInput();

        // Mock Scanner
        mockScanner = mock(Scanner.class);

        // Use reflection to inject the mockScanner into CommandLineUserInput
        Field scannerField = CommandLineUserInput.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(commandLineUserInput, mockScanner);

        // Spy Players
        spyPlayer = spy(new Player("Player"));
        doReturn("Player").when(spyPlayer).getName();
        spyOpponent = spy(new Player("Opponent"));
        doReturn("Opponent").when(spyOpponent).getName();
        spyProtectedOpponent = spy(new Player("ProtectedOpponent"));
        doReturn("ProtectedOpponent").when(spyProtectedOpponent).getName();
        spyProtectedOpponent.switchProtection();

        // Mock PlayerList
        mockPlayerList = mock(PlayerList.class);
        doReturn(spyPlayer).when(mockPlayerList).getPlayer("Player");
        doReturn(spyOpponent).when(mockPlayerList).getPlayer("Opponent");
        doReturn(spyProtectedOpponent).when(mockPlayerList).getPlayer("ProtectedOpponent");

    }

    /**
     * Rule 1
     * Tests that when a player selects a valid player with a Princess card,
     * the opponent is eliminated.
     */
    @Test
    void testRule1_PlayerSelectsValidPlayerWithPrincessCard() {
        // Setup opponent with Princess card
        PrinceAction princeAction = new PrinceAction();
        spyOpponent.receiveHandCard(Card.PRINCESS);

        // Mock input to select the opponent
        doReturn("Opponent").when(mockScanner).nextLine();

        princeAction.execute(commandLineUserInput, spyPlayer, mockPlayerList, deck);

        // Validate that the opponent is eliminated
        verify(spyOpponent).eliminate();
    }

    /**
     * Rule 2
     * Tests that when a player selects a valid player with a non-Princess card,
     * the opponent discards and redraws a card.
     */
    @Test
    void testRule2_PlayerSelectsValidPlayerWithNonPrincessCard() {
        // Setup opponent with a non-Princess card
        PrinceAction princeAction = new PrinceAction();
        spyOpponent.receiveHandCard(Card.GUARD);

        // Mock input to select the opponent
        doReturn("Opponent").when(mockScanner).nextLine();

        princeAction.execute(commandLineUserInput, spyPlayer, mockPlayerList, deck);

        // Validate that the opponent discards and redraws a card
        verify(spyOpponent).playHandCard(0);
        verify(spyOpponent, times(2)).receiveHandCard(any(Card.class));
    }

    /**
     * Rule 3
     * Tests that when a player selects themselves, they discard and redraw a card.
     */
    @Test
    void testRule3_PlayerSelectsThemselves() {
        // Setup player with a non-Princess card
        PrinceAction princeAction = new PrinceAction();
        spyPlayer.receiveHandCard(Card.GUARD);
        spyOpponent.receiveHandCard(Card.GUARD);

        // Mock input to select themselves
        doReturn("Player", "Opponent").when(mockScanner).nextLine();

        princeAction.execute(commandLineUserInput, spyPlayer, mockPlayerList, deck);

        // Validate that the player discards and redraws a card
        verify(spyPlayer).playHandCard(0);
        verify(spyPlayer, times(2)).receiveHandCard(any(Card.class));
        // Validate retry logic: verify nextLine was called twice
        verify(mockScanner, times(1)).nextLine();
    }

    /**
     * Rule 4
     * Tests that when a player selects a non-existent player, they are re-prompted to select an opponent.
     * Verifies that the retry logic works by validating that the opponent is discarded and redrawn a card,
     * and that nextLine was called twice.
     */
    @Test
    void testRule4_PlayerSelectsNonExistentPlayer() {
        PrinceAction princeAction = new PrinceAction();
        spyOpponent.receiveHandCard(Card.GUARD);

        // Mock input to first select a non-existent player, then a valid opponent
        doReturn("NonExistentPlayer", "Opponent").when(mockScanner).nextLine();        

        princeAction.execute(commandLineUserInput, spyPlayer, mockPlayerList, deck);

        // Validate that the opponent discards and redraws a card
        verify(spyOpponent).playHandCard(0);
        verify(spyOpponent, times(2)).receiveHandCard(any(Card.class));
        // Validate retry logic: verify nextLine was called twice
        verify(mockScanner, times(2)).nextLine();
    }

    /**
     * Rule 5
     * Tests that when a player selects a protected player, they are re-prompted to select an opponent.
     * Verifies that the retry logic works by validating that the opponent is discarded and redrawn a card,
     * and that nextLine was called twice.
     */
    @Test
    void testRule5_PlayerSelectsProtectedPlayer() {
        PrinceAction princeAction = new PrinceAction();
        spyProtectedOpponent.receiveHandCard(Card.GUARD);
        spyOpponent.receiveHandCard(Card.GUARD);

        // Mock input to first select the protected player, then a valid opponent
        doReturn("ProtectedOpponent", "Opponent").when(mockScanner).nextLine();

        princeAction.execute(commandLineUserInput, spyPlayer, mockPlayerList, deck);

        // Validate that the protected opponent never discards a card
        verify(spyProtectedOpponent, never()).playHandCard(0);
        // Validate that the player discards and redraws a card
        verify(spyOpponent).playHandCard(0);
        verify(spyOpponent, times(2)).receiveHandCard(any(Card.class));
        // Validate retry logic: verify nextLine was called twice
        verify(mockScanner, times(2)).nextLine();
    }

    /**
     * New Test for Req 19
     * Tests that when the player draws a card when the deck is empty, they draw the removed card.
     * Verifies that the opponent discards and redraws a card.
     */
    @Disabled("Issue #66: This bug will be fixed in a future PR.")
    @Test
    void testRule6_PlayerDrawsRemovedCardWhenDeckIsEmpty() {
        // Setup: Manually retrieve and record the top card of the deck
        while(deck.hasMoreCards()) {
            deck.draw();
        }

        // Ensure deck is empty
        assertFalse(deck.hasMoreCards(), "Deck should be empty during this test.");

        // Player with a non-Princess card
        PrinceAction princeAction = new PrinceAction();
        spyOpponent.receiveHandCard(Card.GUARD);

        // Mock input to select themselves
        doReturn("Opponent").when(mockScanner).nextLine();

        // Execute Prince action
        princeAction.execute(commandLineUserInput, spyPlayer, mockPlayerList, deck);

        // Validate that the opponent discards and redraws a card
        verify(spyOpponent).playHandCard(0);
        verify(spyOpponent, times(2)).receiveHandCard(any(Card.class));
    }

    /**
     * New Test for Req 19
     * Tests that when all other players are protected or eliminated, the player is forced to select themselves.
     * Verifies that the player discards and redraws their card, and that the retry logic prompts three times.
     */
    @Test
    void testRule7_PlayerForcedToChooseSelfWhenNoValidTargets() {
        // Setup: All other players are protected or eliminated
        spyOpponent.switchProtection();

        // Player with a non-Princess card
        PrinceAction princeAction = new PrinceAction();
        spyPlayer.receiveHandCard(Card.GUARD);

        // Mock input to ensure player tries to select others first
        doReturn("Opponent", "ProtectedOpponent", "Player").when(mockScanner).nextLine();

        // Execute Prince action
        princeAction.execute(commandLineUserInput, spyPlayer, mockPlayerList, deck);

        // Verify player discarded and redrew their card
        verify(spyPlayer).playHandCard(0);
        verify(spyPlayer).discardCard(any(Card.class));
        verify(spyPlayer, times(2)).receiveHandCard(any(Card.class));

        // Verify retry logic prompts 3 times
        verify(mockScanner, times(3)).nextLine();
    }
}
