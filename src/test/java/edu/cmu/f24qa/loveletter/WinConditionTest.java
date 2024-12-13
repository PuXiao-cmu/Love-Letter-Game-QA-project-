package edu.cmu.f24qa.loveletter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;


class WinConditionTest {
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

        // Player 0 has 4 tokens
        for (int i = 0; i < 4; i++) {
            player0.addToken();
        }
        // Other players have 2 tokens
        for (int i = 0; i < 2; i++) {
            player1.addToken();
            player2.addToken();
            player3.addToken();
            player4.addToken();
        }

        game = new Game(userInput, playerList, deck);
    }

    @Test
    void testGameEndsWhenPlayerReachesFourTokens() {
        
        game.start();

        List<Player> gameWinners = playerList.getGameWinnerCandidates();
        assertNotNull(gameWinners);
        assertEquals(1, gameWinners.size());
        assertEquals(player0, gameWinners.get(0), "Player 0 should be the game winner.");
    }
}

