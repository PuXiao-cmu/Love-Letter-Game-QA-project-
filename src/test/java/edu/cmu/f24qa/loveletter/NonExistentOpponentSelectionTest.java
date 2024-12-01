package edu.cmu.f24qa.loveletter;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
* Tests for verifying the re-prompt behavior when selecting a non-existent opponent.
*/
public class NonExistentOpponentSelectionTest {
   private PlayerList mockPlayerList;
   private Player mockUser;
   private ByteArrayOutputStream outputStream;

   /**
    * Sets up the test environment before each test.
    */
   @BeforeEach
   void setUp() {
       mockPlayerList = mock(PlayerList.class);
       mockUser = mock(Player.class);
       outputStream = new ByteArrayOutputStream();
       System.setOut(new PrintStream(outputStream));
   }

    @AfterEach
    void tearDown() {
        System.setOut(System.out);
    }

   /**
    * Tests that the system re-prompts when a non-existent opponent is selected.
    * Verifies both the error message and the number of attempts to get player.
    */
    @Test
    void testNonExistentOpponentReprompts() {
        String input = "NonExistentPlayer\nValidPlayer\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Player mockValidPlayer = mock(Player.class);
        when(mockValidPlayer.hasHandCards()).thenReturn(true);
        
        when(mockPlayerList.getPlayer("NonExistentPlayer")).thenReturn(null);
        when(mockPlayerList.getPlayer("ValidPlayer")).thenReturn(mockValidPlayer);
        
        CommandLineUserInput userInput = new CommandLineUserInput();
        userInput.getOpponent(mockPlayerList, mockUser);
        
        verify(mockPlayerList, times(2)).getPlayer(anyString());
        String output = outputStream.toString();
        assert(output.contains("Invalid player name. Please try again."));
    }
}