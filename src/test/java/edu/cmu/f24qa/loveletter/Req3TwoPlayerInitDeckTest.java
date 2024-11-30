package edu.cmu.f24qa.loveletter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class Req3TwoPlayerInitDeckTest {

    private Deck deck;
    private PlayerList players;
    private Game game;

    @BeforeEach
    public void setUp() {
        // Initialize deck and players
        deck = new Deck();
        players = new PlayerList();
        players.addPlayer("Player 1");
        players.addPlayer("Player 2");

        // Create the game instance
        game = new Game(new CommandLineUserInput(), players, deck);
    }

    /**
     * Tests that for a 2-player game, resetGame() should:
     * 1. Leave 10 cards in the deck (16 - 1 hidden - 3 face-up - 2 dealt).
     * 2. Print the 2nd, 3rd, and 4th cards from the top of the deck to the terminal.
     */
    @Disabled("Pending implementation: resetGame() must draw and print 2nd, 3rd, and 4th top cards for a 2-player game.")
    @Test
    public void testDeckSizeAndFaceUpCardsPrintedToTerminal() {
        // Capture terminal output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Call resetGame to simulate game setup
            game.resetGame();

            // Verify that the deck contains 10 cards after setup
            int expectedDeckSize = 10;
            int actualDeckSize = countRemainingCards(deck);
            assertEquals(expectedDeckSize, actualDeckSize,
                "After resetGame, the deck should contain 10 cards for a 2-player game.");

            // Verify that 3 face-up cards are printed to the terminal
            String terminalOutput = outputStream.toString();
            int countFaceUpCardsPrinted = countFaceUpCardsInOutput(terminalOutput);
            assertEquals(3, countFaceUpCardsPrinted,
                "resetGame() should print exactly 3 face-up cards for a 2-player game.");
        } finally {
            // Restore the original System.out
            System.setOut(originalOut);
        }
    }

    /**
     * Helper method to count remaining cards in the deck with public methods.
     *
     * @param deck the Deck object
     * @return the number of cards remaining in the deck
     */
    private int countRemainingCards(Deck deck) {
        int count = 0;
        while (deck.hasMoreCards()) {
            deck.draw();
            count++;
        }
        return count;
    }

    /**
     * Helper method to count the number of face-up cards in the terminal output.
     * Assumption: Each face-up card is printed with format: "Face-up card: [card name]".
     *
     * @param output the terminal output as a string
     * @return the number of face-up cards found in the output
     */
    private int countFaceUpCardsInOutput(String output) {
        String faceUpCardMarker = "Face-up card:";
        return (int) output.lines().filter(line -> line.contains(faceUpCardMarker)).count();
    }
}
