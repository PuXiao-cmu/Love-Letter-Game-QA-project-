package edu.cmu.f24qa.loveletter;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Req13TokenAwardRules {
    private PlayerList playerList;
    private Player player1;
    private Player player2;
    private Player player3;

    @BeforeEach
    void setUp() {
        playerList = new PlayerList();

        playerList.addPlayer("Player1");
        playerList.addPlayer("Player2");
        playerList.addPlayer("Player3");
    
        player1 = playerList.getPlayer("Player1");
        player2 = playerList.getPlayer("Player2");
        player3 = playerList.getPlayer("Player3");
    
        player1.clearHand();
        player2.clearHand();
        player3.clearHand();
    }

    /**
     * Rule 13.1: Test token award for single winner scenario
     * Setup: Player2 has highest card (Princess)
     * Expected: Only Player2 receives one token
     */
    @Test
    void testSingleWinnerTokenAward() {
        // Setup winning condition: Player2 has highest card
        player1.receiveHandCard(Card.GUARD);     // value 1
        player2.receiveHandCard(Card.PRINCESS);  // value 8
        player3.receiveHandCard(Card.BARON);     // value 3

        // Get winners and award tokens
        List<Player> winners = playerList.getRoundWinner();
        for (Player winner : winners) {
            winner.addToken();
        }

        // Verify token awards
        assertEquals(0, player1.getTokens(), "Player1 should have no tokens");
        assertEquals(1, player2.getTokens(), "Player2 should have exactly one token");
        assertEquals(0, player3.getTokens(), "Player3 should have no tokens");
        assertEquals(1, winners.size(), "Should have exactly one winner");
    }

    /**
     * Rule 13.2: Test token award for tied winners scenario
     * Setup: Player1 and Player2 have same highest card (King) and same discard value
     * Expected: Both Player1 and Player2 receive one token each
     */
    @Test
    void testTiedWinnersTokenAward() {
        // Setup tied winning condition
        player1.receiveHandCard(Card.KING);
        player2.receiveHandCard(Card.KING);
        player3.receiveHandCard(Card.BARON);

        // Add same value discarded cards to force a tie
        player1.discardCard(Card.GUARD);
        player1.discardCard(Card.PRIEST);
        player2.discardCard(Card.GUARD);
        player2.discardCard(Card.PRIEST);

        // Get winners and award tokens
        List<Player> winners = playerList.getRoundWinner();
        for (Player winner : winners) {
            winner.addToken();
        }

        // Verify token awards
        assertEquals(1, player1.getTokens(), "Player1 should have exactly one token");
        assertEquals(1, player2.getTokens(), "Player2 should have exactly one token");
        assertEquals(0, player3.getTokens(), "Player3 should have no tokens");
        assertEquals(2, winners.size(), "Should have exactly two winners");
    }

    /**
     * Additional test to verify tokens are properly tracked across multiple rounds
     */
    @Test
    void testTokenAccumulation() {
        // First round: Player2 wins
        player1.receiveHandCard(Card.GUARD);
        player2.receiveHandCard(Card.PRINCESS);
        player3.receiveHandCard(Card.BARON);

        List<Player> winners = playerList.getRoundWinner();
        for (Player winner : winners) {
            winner.addToken();
        }

        // Second round: Player1 and Player2 tie
        player1.clearHand();
        player2.clearHand();
        player3.clearHand();

        player1.receiveHandCard(Card.KING);
        player2.receiveHandCard(Card.KING);
        player3.receiveHandCard(Card.BARON);

        player1.discardCard(Card.GUARD);
        player1.discardCard(Card.PRIEST);
        player2.discardCard(Card.GUARD);
        player2.discardCard(Card.PRIEST);

        winners = playerList.getRoundWinner();
        for (Player winner : winners) {
            winner.addToken();
        }

        // Verify final token counts
        assertEquals(1, player1.getTokens(), "Player1 should have one token");
        assertEquals(2, player2.getTokens(), "Player2 should have two tokens");
        assertEquals(0, player3.getTokens(), "Player3 should have no tokens");
    }
}