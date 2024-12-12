package edu.cmu.f24qa.loveletter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class tieBreakingTest {
    @Mock
    private UserInput userInput;

    @Mock
    private Deck deck;

    private PlayerList playerList;

    private Player player0;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    private Game game;

    @BeforeEach
    void setUp() {
        playerList = spy(new PlayerList());
        for (int i = 0; i < 5; i++) {
            playerList.addPlayer("player" + String.valueOf(i));
        }
        player0 = playerList.getPlayer("player0");
        player1 = playerList.getPlayer("player1");
        player2 = playerList.getPlayer("player2");
        player3 = playerList.getPlayer("player3");
        player4 = playerList.getPlayer("player4");

        // Player 0 and player 1 have 4 tokens
        for (int i = 0; i < 4; i++) {
            player0.addToken();
            player1.addToken();
        }
        // Other players have 2 tokens
        for (int i = 0; i < 2; i++) {
            player2.addToken();
            player3.addToken();
            player4.addToken();
        }

        game = spy(new Game(userInput, playerList, deck));
        doNothing().when(game).playRound();
        // Player0 wins the tie-breaking round
        doReturn(Collections.singletonList(player0)).when(game).determineRoundWinner();
    }

    @Test
    void testGameEndsWhenPlayerReachesFourTokens() {
        // Both Player 0 and player 1 get 4 tokens, they need a tie breaking round.
        List<Player> gameWinners = playerList.getGameWinnerCandidates();
        // Testing only tied players participate the tie-breaking round.
        assertTrue(gameWinners.size() == 2, "There should be 2 game winner candidate before determining the final winner.");
        
        Player finalWinner = game.getFinalGameWinner();

        // Testing the final winner is determined correctly.
        assertNotNull(finalWinner, "The final winner should not be null."); 
        assertEquals(player0, finalWinner, "The final winner should be Player 0."); 
    }
}
