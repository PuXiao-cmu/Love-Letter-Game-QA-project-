package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class Req5GameRoundTest {

    private PlayerList playerList;

    @BeforeEach
    void setUp() {
        playerList = new PlayerList();
        playerList.addPlayer("Alice");
        playerList.addPlayer("Bob");
        playerList.addPlayer("Charlie");
        playerList.addPlayer("Doe");
    }

    /**
     * Requirement 5: Determine the First Player of a Round
     * Tests that the first player in the predefined order goes first in the initial round.
     * Validates that "Alice" is the first player in the first round.
     */
    @Test
    void testFirstRoundFirstPlayer() {
        // Step 1: Simulate the first round
        // Expected: The first player in the predefined order should go first
        Player firstPlayer = playerList.getCurrentPlayer();

        // Step 2: Validate that "Alice" is the first player
        assertEquals("Alice", firstPlayer.getName(), "The first player should be Alice in the first round.");
    }

    /**
     * Requirement 5: Determine the First Player of a Round
     * Tests that the winner of the previous round goes first in the subsequent round.
     * Simulates a scenario where "Bob" wins the round and verifies that he is the first player
     * in the next round.
     */
    // @Disabled("Issue #64: This bug will be fixed in a future PR.")
    @Test
    void testSubsequentRoundWinnerGoesFirst() {
        // Step 1: Simulate a round where Bob is the winner
        Player winner = playerList.getPlayer("Bob");
        assertNotNull(winner, "The winner should not be null.");

        // Set Bob as the first player for the next round
        playerList.setStartingPlayer(winner);

        // Expected: Bob should go first
        Player firstPlayerNextRound = playerList.getCurrentPlayer();
        assertEquals("Bob", firstPlayerNextRound.getName(), "The winner of the previous round (Bob) should go first.");
    }

    /**
     * Requirement 5: Determine the First Player of a Round
     * Tests that in the event of a tie in the previous round, the latest player
     * in the predefined order goes first in the subsequent round.
     * Simulates a scenario where both "Bob" and "Charlie" have the same token count,
     * indicating a tie, and verifies that the player order defaults to the predefined
     * order, with "Bob" being the first player in the next round.
     */
    // @Disabled("Issue #65: This bug will be fixed in a future PR.")
    @Test
    void testSubsequentRoundTieGoesToPredefinedOrder() {
        // Step 1: Simulate a tied round
        Player winner1 = playerList.getPlayer("Bob");
        Player winner2 = playerList.getPlayer("Charlie");
        List<Player> tiedWinners = List.of(winner1, winner2);

        // Step 2: Find the earliest added player among tied winners
        Player startingPlayer = playerList.findEarliestAddedPlayer(tiedWinners);
        playerList.setStartingPlayer(startingPlayer);

        // Expected: The first player in the predefined order (Alice) goes first
        Player firstPlayerNextRound = playerList.getCurrentPlayer();
        assertEquals("Bob", firstPlayerNextRound.getName(), "The first player in the predefined order (Bob) should go first in a tie.");
    }
}
