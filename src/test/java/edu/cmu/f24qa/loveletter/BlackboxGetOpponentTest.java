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

    /**
     * Sets up the test environment before each test.
     */
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
        players.addPlayer("Player");
        players.addPlayer("Opponent");
        players.addPlayer("ProtectedOpponent");

        player = players.getPlayer("Player");
        opponent = players.getPlayer("Opponent");
        protectedOpponent = players.getPlayer("ProtectedOpponent");

        player.receiveHandCard(Card.GUARD);
        opponent.receiveHandCard(Card.GUARD);
        protectedOpponent.receiveHandCard(Card.GUARD);
        protectedOpponent.switchProtection();
    }

    /**
     * Tests the behavior when selecting a non-existent opponent.
     * Mocks user inputs to first choose a non-existent player, 
     * followed by a valid opponent. Verifies that the correct 
     * opponent is selected after a retry and that the input 
     * prompt is called twice.
     */
    @Test
    void testGetOpponentWithNonExistentPlayer() {
        // Set opponent to have cards
        // when(opponent.hasHandCards()).thenReturn(true);

        // Mock inputs to first select a non-existent player, then the valid opponent
        doReturn("NonExistentPlayer", "Opponent").when(mockScanner).nextLine();

        Player selectedOpponent = commandLineUserInput.getOpponent(players, player);

        // Verify that the correct opponent was finally selected

        assertEquals(opponent.getName(), selectedOpponent.getName());
        // Verify that nextLine was called twice, indicating a retry
        verify(mockScanner, times(2)).nextLine();
    }

    
    /**
     * Tests the behavior when selecting a protected opponent.
     * Mocks user inputs to first choose a protected player, 
     * followed by a valid opponent. Verifies that the correct 
     * opponent is selected after a retry and that the input 
     * prompt is called twice.
     */
    @Test
    void testGetOpponentWithProtectedPlayer() {
        // Set opponent to have cards
        // when(opponent.hasHandCards()).thenReturn(true);
        // Mock inputs to first select the protected player, then the valid opponent
        doReturn("ProtectedOpponent", "Opponent").when(mockScanner).nextLine();

        Player selectedOpponent = commandLineUserInput.getOpponent(players, player);

        // Verify that the correct opponent was finally selected
        assertEquals(opponent.getName(), selectedOpponent.getName());
        // Verify that nextLine was called twice, indicating a retry
        verify(mockScanner, times(2)).nextLine();
    }

    /**
     * Tests the behavior when selecting a valid opponent on the first try.
     * Mocks user inputs to select the valid opponent. Verifies that the correct
     * opponent is selected and that the input prompt is only called once.
     */
    @Test
    void testGetOpponentWithValidPlayerFirstTry() {
        // Set opponent to have cards
        // when(opponent.hasHandCards()).thenReturn(true);
        // Mock input to select the valid opponent on the first try
        doReturn("Opponent").when(mockScanner).nextLine();

        Player selectedOpponent = commandLineUserInput.getOpponent(players, player);

        // Verify that the correct opponent was selected on the first attempt
        assertEquals(opponent.getName(), selectedOpponent.getName());
        // Verify that nextLine was only called once
        verify(mockScanner, times(1)).nextLine();
    }
}
