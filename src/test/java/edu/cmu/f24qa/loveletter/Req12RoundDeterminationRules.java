package edu.cmu.f24qa.loveletter;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Req12RoundDeterminationRules {
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

    @Disabled("Issue #44: Round Winner Determination Logic always returns first player with cards, without comparing card values")
    @Test
    void testSingleWinnerScenario() {
        player1.receiveHandCard(Card.GUARD); // player1: value 1
        player2.receiveHandCard(Card.PRINCESS); // player2: value 8
        player3.receiveHandCard(Card.BARON); // player3: value 3

        assertTrue(player1.hasHandCards(), "Player1 should have a card");
        assertTrue(player2.hasHandCards(), "Player2 should have a card");
        assertTrue(player3.hasHandCards(), "Player3 should have a card");

        assertEquals(Card.GUARD, player1.viewHandCard(0), "Player1 should have Guard");
        assertEquals(Card.PRINCESS, player2.viewHandCard(0), "Player2 should have Princess");
        assertEquals(Card.BARON, player3.viewHandCard(0), "Player3 should have Baron");

        Player winner = playerList.getRoundWinner();

        assertNotNull(winner, "Winner should not be null");
        assertEquals(player2, winner, "Player2 should be the winner");
        assertEquals("Player2", winner.getName(), "Winner should be Player2");
        assertEquals(Card.PRINCESS, winner.viewHandCard(0), "Winner should hold Princess card");
    }

    @Disabled("Issue #45: Round Winner Determination Logic fails to compare discarded card values for tied hands")
    @Test
    void testHighestSumOfDiscardedValues() {
        player1.receiveHandCard(Card.KING); // value 6
        player2.receiveHandCard(Card.KING); // value 6
        player3.receiveHandCard(Card.BARON); // value 3

        player1.discardCard(Card.GUARD); // value 1
        player1.discardCard(Card.PRIEST); // value 2

        player2.discardCard(Card.BARON); // value 3
        player2.discardCard(Card.HANDMAIDEN); // value 4

        assertEquals(Card.KING, player1.viewHandCard(0), "Player1 should have King");
        assertEquals(Card.KING, player2.viewHandCard(0), "Player2 should have King");

        assertEquals(3, player1.discardedValue(), "Player1 should have discarded value 3");
        assertEquals(7, player2.discardedValue(), "Player2 should have discarded value 7");

        Player winner = playerList.getRoundWinner();
        assertEquals(player2, winner, "Player2 should win due to higher sum of discarded cards");
    }

    @Disabled("Issue #46: Round Winner Determination Logic fails to support tied winners scenario")
    @Test
    void testTiedDiscardedValues() {
        player1.receiveHandCard(Card.KING);
        player2.receiveHandCard(Card.KING);
        player3.receiveHandCard(Card.BARON);
    
        player1.discardCard(Card.GUARD);
        player1.discardCard(Card.PRIEST);
        player2.discardCard(Card.GUARD);
        player2.discardCard(Card.PRIEST);
    
        assertEquals(Card.KING, player1.viewHandCard(0), "Player1 should have King");
        assertEquals(Card.KING, player2.viewHandCard(0), "Player2 should have King");
    
        assertEquals(3, player1.discardedValue(), "Player1 should have discarded value 3");
        assertEquals(3, player2.discardedValue(), "Player2 should have discarded value 3");
    
        Player winner = playerList.getRoundWinner();
        assertNotNull(winner, "There should be a winner");
    }
}