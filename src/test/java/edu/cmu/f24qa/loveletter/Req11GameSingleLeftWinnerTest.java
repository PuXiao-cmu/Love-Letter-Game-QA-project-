package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class Req11GameSingleLeftWinnerTest {

    private PlayerList playerList;
    private Deck mockDeck;
    private UserInput mockUserInput;
    private Game game;
    private Player spyAlice;
    private Player spyBob;
    private Player spyCharlie;

    @BeforeEach
    void setUp() {
        // Create a PlayerList
        playerList = spy(new PlayerList());
        playerList.addPlayer("Alice");
        playerList.addPlayer("Bob");
        playerList.addPlayer("Charlie");

        // Spy on players
        spyAlice = spy(playerList.getPlayer("Alice"));
        spyBob = spy(playerList.getPlayer("Bob"));
        spyCharlie = spy(playerList.getPlayer("Charlie"));

        // Assign initial cards to each player
        spyAlice.receiveHandCard(Card.COUNTESS);
        spyBob.receiveHandCard(Card.COUNTESS);
        spyCharlie.receiveHandCard(Card.COUNTESS);

        // Mock Deck and UserInput
        mockDeck = mock(Deck.class);
        mockUserInput = mock(UserInput.class);

        // Create the game and ignore resetGame at the beginning of a round
        game = spy(new Game(mockUserInput, playerList, mockDeck));
        doNothing().when(game).resetGame();
    }

    /*
     * Requirement 11: A player shall win when they are the only player left in the game.
     */
    @Test
    void testSinglePlayerWinsWhenOnlyActivePlayer() {
        //Eliminate Bob and Charlie
        spyBob.eliminate();
        spyCharlie.eliminate();

        // Play the round
        game.playRound();

        // Verify determineRoundWinner returns Alice
        List<Player> winners = game.determineRoundWinner();
        assertNotNull(winners, "Winners list should not be null.");
        assertEquals(1, winners.size(), "There should be exactly one winner.");
        assertEquals("Alice", winners.get(0).getName(), "Alice should be declared the winner.");

    }
}

