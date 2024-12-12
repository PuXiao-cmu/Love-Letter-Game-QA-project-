package edu.cmu.f24qa.loveletter;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Req15GameVictoryRulesTest {
    private PlayerList playerList;
    private Game game;
    private Deck deck;

    @BeforeEach
    void setUp() {
        playerList = new PlayerList();
        deck = new Deck();
    }

    /**
     * Rule 15.2.1: Tests victory condition in 2-player game
     */
    @Disabled("Issue #85: Game winner determination logic needs to be updated for different player counts")
    @Test
    void testTwoPlayerGameVictory() {
        playerList.addPlayer("Player1");
        playerList.addPlayer("Player2");
        game = new Game(new CommandLineUserInput(), playerList, deck);

        Player player1 = playerList.getPlayer("Player1");
        
        // Add 6 tokens (not enough for victory)
        for (int i = 0; i < 6; i++) {
            player1.addToken();
        }
        assertNull(playerList.getGameWinnerCandidates().get(0), "No winner should be declared at 6 tokens");

        // Add 7th token (should trigger victory)
        player1.addToken();
        assertEquals(player1, playerList.getGameWinnerCandidates().get(0), "Player1 should win with 7 tokens in 2-player game");
    }

    /**
     * Rule 15.2.2: Tests victory condition in 3-player game
     */
    @Test
    void testThreePlayerGameVictory() {
        playerList.addPlayer("Player1");
        playerList.addPlayer("Player2");
        playerList.addPlayer("Player3");
        game = new Game(new CommandLineUserInput(), playerList, deck);

        Player player3 = playerList.getPlayer("Player3");
        
        // Add 4 tokens (not enough for victory)
        for (int i = 0; i < 4; i++) {
            player3.addToken();
        }
        assertNull(playerList.getGameWinnerCandidates().get(0), "No winner should be declared at 4 tokens");

        // Add 5th token (should trigger victory)
        player3.addToken();
        assertEquals(player3, playerList.getGameWinnerCandidates().get(0), "Player3 should win with 5 tokens in 3-player game");
    }

    /**
     * Rule 15.2.3: Tests victory condition in 4-player game
     */
    @Disabled("Issue #85: Game winner determination logic needs to be updated for different player counts")
    @Test
    void testFourPlayerGameVictory() {
        playerList.addPlayer("Player1");
        playerList.addPlayer("Player2");
        playerList.addPlayer("Player3");
        playerList.addPlayer("Player4");
        game = new Game(new CommandLineUserInput(), playerList, deck);

        Player player2 = playerList.getPlayer("Player2");
        
        // Add 3 tokens (not enough for victory)
        for (int i = 0; i < 3; i++) {
            player2.addToken();
        }
        assertNull(playerList.getGameWinnerCandidates().get(0), "No winner should be declared at 3 tokens");

        // Add 4th token (should trigger victory)
        player2.addToken();
        assertEquals(player2, playerList.getGameWinnerCandidates().get(0), "Player2 should win with 4 tokens in 4-player game");
    }

    /**
     * Rule 15.1: Tests token count monitoring between rounds
     */
    @Test
    void testTokenCountMaintainedBetweenRounds() {
        playerList.addPlayer("Player1");
        playerList.addPlayer("Player2");
        playerList.addPlayer("Player3");
        game = new Game(new CommandLineUserInput(), playerList, deck);

        Player player1 = playerList.getPlayer("Player1");
        Player player2 = playerList.getPlayer("Player2");

        // Award tokens and verify counts
        player1.addToken();
        player2.addToken();
        
        // Simulate round reset
        game.resetGame();
        
        // Verify token counts persist
        assertEquals(1, player1.getTokens(), "Player1 should maintain token count after round reset");
        assertEquals(1, player2.getTokens(), "Player2 should maintain token count after round reset");
    }
}