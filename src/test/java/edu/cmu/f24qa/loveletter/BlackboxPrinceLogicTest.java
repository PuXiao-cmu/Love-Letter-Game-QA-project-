package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Scanner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BlackboxPrinceLogicTest {

    private CommandLineUserInput commandLineUserInput;
    private Scanner mockScanner;
    private Player spyPlayer;
    private Player spyOpponent;
    private Player spyProtectedOpponent;
    private PlayerList mockPlayerList;

    @BeforeEach
    void setup() throws Exception {
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

    @Test
    void testRule1_PlayerSelectsValidPlayerWithPrincessCard() {
        // Setup opponent with Princess card
        PrinceAction princeAction = new PrinceAction();
        spyOpponent.receiveHandCard(Card.PRINCESS);

        // Mock input to select the opponent
        doReturn("Opponent").when(mockScanner).nextLine();

        princeAction.execute(commandLineUserInput, spyPlayer, mockPlayerList);

        // Validate that the opponent is eliminated
        verify(spyOpponent).eliminate();
    }

    @Test
    void testRule2_PlayerSelectsValidPlayerWithNonPrincessCard() {
        // Setup opponent with a non-Princess card
        PrinceAction princeAction = new PrinceAction();
        spyOpponent.receiveHandCard(Card.GUARD);

        // Mock input to select the opponent
        doReturn("Opponent").when(mockScanner).nextLine();

        princeAction.execute(commandLineUserInput, spyPlayer, mockPlayerList);

        // Validate that the opponent discards and redraws a card
        verify(spyOpponent).playHandCard(0);
        verify(spyOpponent).receiveHandCard(any(Card.class));
    }

    @Test
    void testRule3_PlayerSelectsThemselves() {
        // Setup player with a non-Princess card
        PrinceAction princeAction = new PrinceAction();
        spyPlayer.receiveHandCard(Card.GUARD);
        spyOpponent.receiveHandCard(Card.GUARD);

        // Mock input to select themselves
        doReturn("Player", "Opponent").when(mockScanner).nextLine();

        princeAction.execute(commandLineUserInput, spyPlayer, mockPlayerList);

        // Validate that the player discards and redraws a card
        verify(spyPlayer).playHandCard(0);
        verify(spyPlayer).receiveHandCard(any(Card.class));
    }

    @Test
    void testRule4_PlayerSelectsNonExistentPlayer() {
        PrinceAction princeAction = new PrinceAction();
        // Mock input to first select a non-existent player, then a valid opponent
        doReturn("NonExistentPlayer", "Opponent").when(mockScanner).nextLine();

        princeAction.execute(commandLineUserInput, spyPlayer, mockPlayerList);

        // Validate that the player discards and redraws a card
        verify(spyOpponent).playHandCard(0);
        verify(spyOpponent).receiveHandCard(any(Card.class));
        // Validate retry logic: verify nextLine was called twice
        verify(mockScanner, times(2)).nextLine();
    }

    @Test
    void testRule5_PlayerSelectsProtectedPlayer() {
        PrinceAction princeAction = new PrinceAction();
        // Mock input to first select the protected player, then a valid opponent
        doReturn("ProtectedOpponent", "Opponent").when(mockScanner).nextLine();

        princeAction.execute(commandLineUserInput, spyPlayer, mockPlayerList);

        // Validate that the player discards and redraws a card
        verify(spyOpponent).playHandCard(0);
        verify(spyOpponent).receiveHandCard(any(Card.class));
        // Validate retry logic: verify nextLine was called twice
        verify(mockScanner, times(2)).nextLine();
    }
}
