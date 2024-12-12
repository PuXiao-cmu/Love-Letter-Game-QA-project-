package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BlackboxJesterCardTest {

    private CommandLineUserInput userInput;
    private Scanner mockScanner;
    private Player spyPredictor;
    private Player spyPredictedPlayer;
    private Player otherPlayer;
    private PlayerList playerList;
    private Deck deck;
    private Card jesterCard;
    private Game game;

    @BeforeEach
    void setup() throws Exception {
        // Initialize deck and game
        deck = new Deck();
        deck.build();
        deck.shuffle();

        // Mock Scanner
        mockScanner = mock(Scanner.class);

        // Inject mock Scanner into CommandLineUserInput
        userInput = new CommandLineUserInput();
        Field scannerField = CommandLineUserInput.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(userInput, mockScanner);

        // Create players
        spyPredictor = spy(new Player("Predictor"));
        spyPredictedPlayer = spy(new Player("PredictedWinner"));
        otherPlayer = new Player("OtherPlayer");
        spyPredictor.receiveHandCard(Card.GUARD);
        spyPredictedPlayer.receiveHandCard(Card.GUARD);
        otherPlayer.receiveHandCard(Card.GUARD);

        // Initialize PlayerList
        playerList = new PlayerList();

        // Use reflection to modify the internal Deque<Player> players
        Field playersField = PlayerList.class.getDeclaredField("players");
        playersField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Deque<Player> players = (Deque<Player>) playersField.get(playerList);

        // Add spy players to the PlayerList
        players.add(spyPredictor);
        players.add(spyPredictedPlayer);
        players.add(otherPlayer);

        // Initialize Jester card and game
        jesterCard = Card.JESTER;
        game = new Game(userInput, playerList, deck);
    }

    /**
     * Rule 1:
     * Tests that when the predicted player wins the round and the predictor
     * correctly predicted the winner, the predictor gets an additional token.
     */
    @Test
    void testJesterPredictionWithWinningPlayer() {
        // Mock Scanner input to select the predicted player
        when(mockScanner.nextLine()).thenReturn("PredictedWinner");

        // Execute Jester card
        jesterCard.execute(userInput, spyPredictor, playerList, deck);

        // Verify prediction is set
        verify(spyPredictedPlayer, times(1)).setJesterPredictor(spyPredictor);

        // Simulate the predicted player winning the round
        game.handleRoundWinner(List.of(spyPredictedPlayer));

        // Assert that the predicted player gets a token for winning
        assertEquals(1, spyPredictedPlayer.getTokens());

        // Assert that the predictor gets an additional token for a correct prediction
        assertEquals(1, spyPredictor.getTokens());
    }

    /**
     * Rule 2:
     * Tests that when the predicted player does not win the round,
     * the predictor does not get an additional token for an incorrect prediction.
     */
    @Test
    void testJesterPredictionWithNonWinningPlayer() {
        // Mock Scanner input to select the predicted player
        when(mockScanner.nextLine()).thenReturn("PredictedWinner");

        // Execute Jester card
        jesterCard.execute(userInput, spyPredictor, playerList, deck);

        // Verify prediction is set
        verify(spyPredictedPlayer, times(1)).setJesterPredictor(spyPredictor);

        // Simulate another player winning the round (not the predicted player)
        game.handleRoundWinner(List.of(otherPlayer));

        // Assert that the predicted player does not get a token
        assertEquals(0, spyPredictedPlayer.getTokens());

        // Assert that the predictor does not get a token for an incorrect prediction
        assertEquals(0, spyPredictor.getTokens());
    }

    /**
     * Rule 3:
     * Tests that when the predictor provides an invalid selection,
     * the retry logic is executed and the correct prediction is set.
     */
    @Test
    void testJesterInvalidSelectionRetry() {
        // Mock invalid input followed by valid input
        when(mockScanner.nextLine())
                .thenReturn("InvalidPlayer") // Invalid first selection
                .thenReturn("PredictedWinner"); // Valid second selection

        // Execute Jester card
        jesterCard.execute(userInput, spyPredictor, playerList, deck);

        // Verify retry behavior and correct prediction is set
        verify(mockScanner, times(2)).nextLine();
        verify(spyPredictedPlayer, times(1)).setJesterPredictor(spyPredictor);
    }
}
