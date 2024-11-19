package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BlackboxGetOpponentTest {

    private CommandLineUserInput commandLineUserInput;
    private Scanner mockScanner;
    private PlayerList players;
    private Player player;
    private Player opponent;
    private Player protectedOpponent;

    @BeforeEach
    void setup() throws Exception {
        // Create a real instance of CommandLineUserInput
        commandLineUserInput = new CommandLineUserInput();
        
        // Mock Scanner
        mockScanner = mock(Scanner.class);

        // Use reflection to inject the mockScanner into CommandLineUserInput
        Field scannerField = CommandLineUserInput.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(commandLineUserInput, mockScanner);

        // Setup game players
        players = new PlayerList();
        player = new Player("Player");
        opponent = new Player("Opponent");
        protectedOpponent = new Player("ProtectedOpponent");

        players.addPlayer(player.getName());
        players.addPlayer(opponent.getName());
        players.addPlayer(protectedOpponent.getName());
        players.getPlayer("ProtectedOpponent").switchProtection();
    }

    @Test
    void testGetOpponentWithNonExistentPlayer() {
        // Mock inputs to first select a non-existent player, then the valid opponent
        doReturn("NonExistentPlayer", "Opponent").when(mockScanner).nextLine();

        Player selectedOpponent = commandLineUserInput.getOpponent(players, player);

        // Verify that the correct opponent was finally selected

        assertEquals(opponent.getName(), selectedOpponent.getName());
        // Verify that nextLine was called twice, indicating a retry
        verify(mockScanner, times(2)).nextLine();
    }

    //@Disabled("This test is currently failing and will be ignored")
    @Test
    void testGetOpponentWithProtectedPlayer() {
        // Mock inputs to first select the protected player, then the valid opponent
        doReturn("ProtectedOpponent", "Opponent").when(mockScanner).nextLine();

        Player selectedOpponent = commandLineUserInput.getOpponent(players, player);

        // Verify that the correct opponent was finally selected
        assertEquals(opponent.getName(), selectedOpponent.getName());
        // Verify that nextLine was called twice, indicating a retry
        verify(mockScanner, times(2)).nextLine();
    }

    @Test
    void testGetOpponentWithValidPlayerFirstTry() {
        // Mock input to select the valid opponent on the first try
        doReturn("Opponent").when(mockScanner).nextLine();

        Player selectedOpponent = commandLineUserInput.getOpponent(players, player);

        // Verify that the correct opponent was selected on the first attempt
        assertEquals(opponent.getName(), selectedOpponent.getName());
        // Verify that nextLine was only called once
        verify(mockScanner, times(1)).nextLine();
    }
}
