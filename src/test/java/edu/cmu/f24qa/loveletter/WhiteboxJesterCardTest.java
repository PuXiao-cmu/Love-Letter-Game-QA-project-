package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class WhiteboxJesterCardTest {

    private UserInput mockUserInput;
    private Player spyPlayer;
    private Player spyPredictedPlayer;
    private PlayerList playerList;
    private Deck deck;
    private JesterAction jesterAction;

    @BeforeEach
    void setup() {
        deck = new Deck();
        deck.build();
        deck.shuffle();

        // Mock UserInput
        mockUserInput = mock(UserInput.class);

        // Create players
        spyPlayer = spy(new Player("Player"));
        spyPredictedPlayer = spy(new Player("PredictedPlayer"));

        // Initialize PlayerList and add players
        playerList = new PlayerList();
        playerList.addPlayer("Player");
        playerList.addPlayer("PredictedPlayer");

        jesterAction = new JesterAction();
    }

    /**
     * JesterT1:
     * Tests that the Jester action correctly sets the prediction for the player
     * the user selects as the target.
     */
    @Test
    void testExecuteSetsPrediction() {
        // Mock userInput.getOpponent to return the predicted player
        when(mockUserInput.getOpponent(playerList, spyPlayer, false, true)).thenReturn(spyPredictedPlayer);

        // Execute Jester action
        jesterAction.execute(mockUserInput, spyPlayer, playerList, deck);

        // Verify the prediction is set correctly
        verify(spyPredictedPlayer, times(1)).setJesterPredictor(spyPlayer);
    }
}
