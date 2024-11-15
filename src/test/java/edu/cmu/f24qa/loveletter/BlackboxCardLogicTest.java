package edu.cmu.f24qa.loveletter;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BlackboxCardLogicTest {
    private UserInput userInput;
    private Player user;
    private PlayerList playerList;

    @BeforeEach
    void setUp() {
        userInput = mock(UserInput.class);
        user = mock(Player.class);
        playerList = mock(PlayerList.class);
    }

    /**
     * Handmaid Card Test: Rule 1-2
     */

    // Rule 1: Normal case - the player is initially unprotected.
    @Test
    void testHandmaidProtectionGain() {
        HandmaidAction handmaidAction = new HandmaidAction();

        when(user.isProtected()).thenReturn(false);

        handmaidAction.execute(userInput, user, playerList);

        verify(user).switchProtection();  // Verify protection is enabled
    }

    // Rule 2: Edge case - the player is already protected.
    @Test
    void testHandmaidAlreadyProtected() {
        HandmaidAction handmaidAction = new HandmaidAction();

        when(user.isProtected()).thenReturn(true);

        handmaidAction.execute(userInput, user, playerList);

        verify(user, never()).switchProtection();  // Verify no protection change
    }
}
