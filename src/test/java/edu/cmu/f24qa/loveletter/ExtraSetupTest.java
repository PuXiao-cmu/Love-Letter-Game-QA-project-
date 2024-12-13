package edu.cmu.f24qa.loveletter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtraSetupTest {
    private Deck deck;
    private PlayerList playerList;
    private UserInput userInput;

    @BeforeEach
    void setUp() {
        deck = spy(new Deck());
        playerList = spy(new PlayerList());
        userInput = mock(UserInput.class);
        for (int i = 0; i < 5; i++) {
            playerList.addPlayer("player" + String.valueOf(i));
        }
    }

    @Test
    void testResetGameWith5To8Players() {
        Game game = new Game(userInput, playerList, deck);
        doNothing().when(deck).hideTopCard();
        doNothing().when(playerList).dealCards(deck);

        game.resetGame();

        // Check that there are exactly 32 cards in the deck
        assertEquals(32, deck.getDeck().size(), "Deck should contain 32 cards.");

        // Count card occurrences
        Map<Card, Integer> cardCount = new HashMap<>();
        for (Card card : deck.getDeck()) {
            cardCount.put(card, cardCount.getOrDefault(card, 0) + 1);
        }

        // Verify card distribution
        assertEquals(8, cardCount.get(Card.GUARD), "There should be 8 Guards in the deck.");
        assertEquals(2, cardCount.get(Card.PRIEST), "There should be 2 Priests in the deck.");
        assertEquals(2, cardCount.get(Card.BARON), "There should be 2 Barons in the deck.");
        assertEquals(2, cardCount.get(Card.HANDMAIDEN), "There should be 2 Handmaidens in the deck.");
        assertEquals(2, cardCount.get(Card.PRINCE), "There should be 2 Princes in the deck.");
        assertEquals(1, cardCount.get(Card.KING), "There should be 1 King in the deck.");
        assertEquals(1, cardCount.get(Card.COUNTESS), "There should be 1 Countess in the deck.");
        assertEquals(1, cardCount.get(Card.PRINCESS), "There should be 1 Princess in the deck.");
        assertEquals(2, cardCount.get(Card.CARDINAL), "There should be 2 Cardinals in the deck.");
        assertEquals(2, cardCount.get(Card.BARONESS), "There should be 2 Baronesses in the deck.");
        assertEquals(2, cardCount.get(Card.SYCOPHANT), "There should be 2 Sycophants in the deck.");
        assertEquals(2, cardCount.get(Card.COUNT), "There should be 2 Counts in the deck.");
        assertEquals(1, cardCount.get(Card.JESTER), "There should be 1 Jester in the deck.");
        assertEquals(1, cardCount.get(Card.ASSASSIN), "There should be 1 Assassin in the deck.");
        assertEquals(1, cardCount.get(Card.CONSTABLE), "There should be 1 Constable in the deck.");
        assertEquals(1, cardCount.get(Card.DOWAGERQUEEN), "There should be 1 Dowager Queen in the deck.");
        assertEquals(1, cardCount.get(Card.BISHOP), "There should be 1 Bishop in the deck.");
    }
}

