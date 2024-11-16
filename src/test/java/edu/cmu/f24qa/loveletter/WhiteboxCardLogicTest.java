package edu.cmu.f24qa.loveletter;

import static org.mockito.Mockito.*;
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
     * Tests that playing Handmaid card results in protecting current player.
     */
    @Test
    void testHandmaidActionNotProtected() {    // HT1
        HandmaidAction handmaidAction = new HandmaidAction();

        when(player.isProtected()).thenReturn(false);
        handmaidAction.execute(userInput, player, playerList);

        verify(player).switchProtection();
    }

    /**
     * Test ID: HT2
     * Branch ID: Handmaid-W2
     * Tests that playing Handmaid card results in protecting current player.
     */
    @Test
    void testHandmaidActionProtected() {    // HT2
        HandmaidAction handmaidAction = new HandmaidAction();

        when(player.isProtected()).thenReturn(true);
        handmaidAction.execute(userInput, player, playerList);

        verify(player, never()).switchProtection();
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

