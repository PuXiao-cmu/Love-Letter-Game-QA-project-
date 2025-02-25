package edu.cmu.f24qa.loveletter;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Req12RoundDeterminationRulesTest {
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

        List<Player> winners = playerList.getRoundWinner();

        assertNotNull(winners, "Winners list should not be null");
        assertEquals(1, winners.size(), "Should have exactly one winner");
        assertEquals(player2, winners.get(0), "Player2 should be the winner");
        assertEquals("Player2", winners.get(0).getName(), "Winner should be Player2");
        assertEquals(Card.PRINCESS, winners.get(0).viewHandCard(0), "Winner should hold Princess card");    
}

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

        List<Player> winners = playerList.getRoundWinner();
        assertEquals(1, winners.size(), "Should have exactly one winner");
        assertEquals(player2, winners.get(0), "Player2 should win due to higher sum of discarded cards");
       }

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
    
        List<Player> winners = playerList.getRoundWinner();
        assertNotNull(winners, "Winners list should not be null");
        assertEquals(2, winners.size(), "Should have two winners for a tie");
        assertTrue(winners.contains(player1), "Player1 should be one of the winners");
        assertTrue(winners.contains(player2), "Player2 should be one of the winners");    
    }
}
