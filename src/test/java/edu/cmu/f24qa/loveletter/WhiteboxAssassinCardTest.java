package edu.cmu.f24qa.loveletter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WhiteboxAssassinCardTest {

    private UserInput userInput;
    private Player player;
    private PlayerList playerList;
    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck();
        deck.build();
        deck.shuffle();
        userInput = mock(UserInput.class);
        player = mock(Player.class);
        playerList = mock(PlayerList.class);
    }

    /**
     * Test ID: AssassinT1
     * Tests that the AssassinAction.execute method does not trigger any actions.
     * Verifies that the player, userInput, and playerList are not interacted with.
     */
    @Test
    void testAssassinAction() {
        AssassinAction assassinAction = new AssassinAction();

        assassinAction.execute(userInput, player, playerList, deck);

        // Verify that no actions are triggered:
        verifyNoInteractions(player);
        verifyNoInteractions(userInput);
        verifyNoInteractions(playerList);
    }
    
}
