package edu.cmu.f24qa.loveletter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BlackboxCardLogicTest {
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
     * Handmaid Card Test: Rule 1: If Player plays Handmaid card, then this player is protected.
     */
    @Test
    void testHandmaidProtectPlayer() {
        HandmaidAction handmaidAction = new HandmaidAction();
        Player realUser = new Player("testPlayer");
        player = spy(realUser); 

        handmaidAction.execute(userInput, player, playerList);

        assertTrue(player.isProtected(), "The user should be protected after playing the Handmaid card."); // Verify protection is enabled
    }

    /**
     * Countess Card Test: Rule 1: Play Countess Card do not trigger any action.
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
