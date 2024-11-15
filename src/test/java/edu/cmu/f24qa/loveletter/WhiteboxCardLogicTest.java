package edu.cmu.f24qa.loveletter;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WhiteboxCardLogicTest {
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
     * Test ID: HT1
     * Branch ID: Handmaid-W1
     * Tests that playing Handmaid card results in protecting current player.
     */
    @Test
    void testHandmaidActionNotProtected() {    // HT1
        System.out.println("Running Handmaid Action Test");

        HandmaidAction handmaidAction = new HandmaidAction();

        when(user.isProtected()).thenReturn(false);
        handmaidAction.execute(userInput, user, playerList);

        verify(user).switchProtection();
    }
}

