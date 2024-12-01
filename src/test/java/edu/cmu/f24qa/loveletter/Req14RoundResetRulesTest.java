package edu.cmu.f24qa.loveletter;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Req14RoundResetRulesTest {
    private PlayerList playerList;
    private Player player1;
    private Player player2;
    private Player player3;
    private Deck deck;
    private Game game;

    @BeforeEach
    void setUp() {
        playerList = new PlayerList();
        deck = new Deck();
        
        playerList.addPlayer("Player1");
        playerList.addPlayer("Player2");
        playerList.addPlayer("Player3");
        
        player1 = playerList.getPlayer("Player1");
        player2 = playerList.getPlayer("Player2");
        player3 = playerList.getPlayer("Player3");
        
        game = new Game(new CommandLineUserInput(), playerList, deck);
    }

    /**
     * Tests that players' discard piles are cleared during round reset
     */
    @Test
    void testDiscardPilesAreClearedOnReset() {
        // Setup: Add cards to discard piles
        player1.discardCard(Card.GUARD);
        player2.discardCard(Card.PRIEST);
        player3.discardCard(Card.BARON);

        // Initial state verification
        assertTrue(player1.discardedValue() > 0, "Player1 should have cards in discard pile before reset");
        assertTrue(player2.discardedValue() > 0, "Player2 should have cards in discard pile before reset");
        assertTrue(player3.discardedValue() > 0, "Player3 should have cards in discard pile before reset");

        // Perform reset
        game.resetGame();

        // Verify discard piles are cleared
        assertEquals(0, player1.discardedValue(), "Player1's discard pile should be empty after reset");
        assertEquals(0, player2.discardedValue(), "Player2's discard pile should be empty after reset");
        assertEquals(0, player3.discardedValue(), "Player3's discard pile should be empty after reset");
    }

    /**
     * Tests that players receive new hand cards during round reset
     */
    @Test
    void testPlayersReceiveNewHandCardsOnReset() {
        // Perform reset
        game.resetGame();

        // Verify each player has a card
        assertTrue(player1.hasHandCards(), "Player1 should have cards after reset");
        assertTrue(player2.hasHandCards(), "Player2 should have cards after reset");
        assertTrue(player3.hasHandCards(), "Player3 should have cards after reset");
    }

    /**
     * Tests that hand cards change after round reset
     */
    @Test
    void testHandCardsChangeBetweenRounds() {
        // First round setup
        game.resetGame();
        Card player1FirstCard = player1.viewHandCard(0);
        Card player2FirstCard = player2.viewHandCard(0);
        Card player3FirstCard = player3.viewHandCard(0);

        // Simulate round end and reset for new round
        game.resetGame();
        Card player1SecondCard = player1.viewHandCard(0);
        Card player2SecondCard = player2.viewHandCard(0);
        Card player3SecondCard = player3.viewHandCard(0);

        // Verify cards were shuffled and redistributed
        // Note: There's a small chance cards might be the same due to random shuffle
        boolean cardsChanged = !player1FirstCard.equals(player1SecondCard) ||
                             !player2FirstCard.equals(player2SecondCard) ||
                             !player3FirstCard.equals(player3SecondCard);
                             
        assertTrue(cardsChanged, "At least some cards should be different after reset");
    }

    /**
     * Tests that game state is properly initialized during round reset
     */
    @Test
    void testGameStateInitializationOnReset() {
        // Setup initial game state with some played cards
        player1.discardCard(Card.GUARD);
        player2.discardCard(Card.PRIEST);
        player3.discardCard(Card.BARON);

        // Record initial deck state
        int initialCardsInDeck = countRemainingCards(deck);

        // Perform reset
        game.resetGame();

        // Verify deck is rebuilt and reshuffled
        assertTrue(deck.hasMoreCards(), "Deck should have cards after reset");
        int newCardsInDeck = countRemainingCards(deck);
        assertTrue(newCardsInDeck > initialCardsInDeck, "Deck should be replenished after reset");

        // Verify players are in valid state
        assertNotNull(playerList.getCurrentPlayer(), "Should have a valid current player");
    }

    /**
     * Helper method to count remaining cards in deck
     */
    private int countRemainingCards(Deck deck) {
        int count = 0;
        while (deck.hasMoreCards()) {
            deck.draw();
            count++;
        }
        return count;
    }
}
