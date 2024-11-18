package edu.cmu.f24qa.loveletter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WhiteboxCardLogicTest {
    private UserInput userInput;
    private Player player;
    private PlayerList playerList;

    @BeforeEach
    void setUp() {
        userInput = mock(UserInput.class);
        player = mock(Player.class);
        playerList = mock(PlayerList.class);
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
     * Tests that playing Handmaid card do noting for protected player.
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
     * Tests that playing Countess card do not trigger any action.
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
}

