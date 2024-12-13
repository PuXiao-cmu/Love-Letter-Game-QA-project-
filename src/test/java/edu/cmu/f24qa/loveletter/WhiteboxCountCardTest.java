package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class WhiteboxCountCardTest {

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
     * Test ID: CountT1
     * Tests that the CountAction.execute method does not trigger any actions.
     * Verifies that the player, userInput, and playerList are not interacted with.
     */
    @Test
    void testCountAction() {
        CountAction countAction = new CountAction();

        countAction.execute(userInput, player, playerList, deck);

        // Verify no interactions with mocks
        verifyNoInteractions(userInput);
        verifyNoInteractions(player);
        verifyNoInteractions(playerList);
    }
}
