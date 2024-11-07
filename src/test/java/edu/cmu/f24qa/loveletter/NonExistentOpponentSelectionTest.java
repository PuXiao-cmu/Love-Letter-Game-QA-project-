package edu.cmu.f24qa.loveletter;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class NonExistentOpponentSelectionTest {
   private PlayerList mockPlayerList;
   private Player mockUser;
   private ByteArrayOutputStream outputStream;

   @BeforeEach
   void setUp() {
       mockPlayerList = mock(PlayerList.class);
       mockUser = mock(Player.class);
       outputStream = new ByteArrayOutputStream();
       System.setOut(new PrintStream(outputStream));
   }

   @Test
   void testNonExistentOpponentReprompts() {
       String input = "NonExistentPlayer\nValidPlayer\n";
       System.setIn(new ByteArrayInputStream(input.getBytes()));
       
       Player mockValidPlayer = mock(Player.class);
       when(mockPlayerList.getPlayer("NonExistentPlayer")).thenReturn(null);
       when(mockPlayerList.getPlayer("ValidPlayer")).thenReturn(mockValidPlayer);
       
       CommandLineUserInput userInput = new CommandLineUserInput();
       userInput.getOpponent(mockPlayerList, mockUser);
       
       verify(mockPlayerList, times(2)).getPlayer(anyString());
       String output = outputStream.toString();
       assert(output.contains("Invalid player name. Please try again."));
   }
}
