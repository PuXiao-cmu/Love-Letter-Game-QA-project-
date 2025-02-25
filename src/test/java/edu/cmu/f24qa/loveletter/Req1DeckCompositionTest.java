package edu.cmu.f24qa.loveletter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.Deque;

public class Req1DeckCompositionTest {

    private Deck deck;

    @BeforeEach
    public void setUp() {
        // Initialize the deck and build it before each test
        deck = new Deck();
        deck.build();
    }

    /**
     * Test to verify that the deck contains the correct number and type of cards after being built.
     * Ensures that the initial composition matches the game rules.
     */
    @Test
    public void testDeckBuildCorrectComposition() throws NoSuchFieldException, IllegalAccessException {
        // Access the private deck field using reflection to inspect the cards
        Deque<Card> cards = getDeckField();

        // Verify the total number of cards in the deck
        assertEquals(16, cards.size(), "Deck should contain 16 cards initially");

        // Verify the count of each specific card type in the deck
        assertEquals(5, countCardType(cards, Card.GUARD), "Deck should contain 5 GUARD cards");
        assertEquals(2, countCardType(cards, Card.PRIEST), "Deck should contain 2 PRIEST cards");
        assertEquals(2, countCardType(cards, Card.BARON), "Deck should contain 2 BARON cards");
        assertEquals(2, countCardType(cards, Card.HANDMAIDEN), "Deck should contain 2 HANDMAIDEN cards");
        assertEquals(2, countCardType(cards, Card.PRINCE), "Deck should contain 2 PRINCE cards");
        assertEquals(1, countCardType(cards, Card.KING), "Deck should contain 1 KING card");
        assertEquals(1, countCardType(cards, Card.COUNTESS), "Deck should contain 1 COUNTESS card");
        assertEquals(1, countCardType(cards, Card.PRINCESS), "Deck should contain 1 PRINCESS card");
    }

    /**
     * Test to verify that shuffling the deck maintains the correct composition.
     * Ensures that the shuffle operation does not alter the total count or composition of cards.
     */
    @Test
    public void testDeckShuffleMaintainsComposition() throws NoSuchFieldException, IllegalAccessException {
        // Shuffle the deck
        deck.shuffle();

        // Access the private deck field using reflection to inspect the shuffled cards
        Deque<Card> cards = getDeckField();

        // Verify that the total number of cards remains unchanged after shuffle
        assertEquals(16, cards.size(), "Shuffled deck should still contain 16 cards");

        // Verify the count of each specific card type remains unchanged after shuffle
        assertEquals(5, countCardType(cards, Card.GUARD), "Shuffled deck should contain 5 GUARD cards");
        assertEquals(2, countCardType(cards, Card.PRIEST), "Shuffled deck should contain 2 PRIEST cards");
        assertEquals(2, countCardType(cards, Card.BARON), "Shuffled deck should contain 2 BARON cards");
        assertEquals(2, countCardType(cards, Card.HANDMAIDEN), "Shuffled deck should contain 2 HANDMAIDEN cards");
        assertEquals(2, countCardType(cards, Card.PRINCE), "Shuffled deck should contain 2 PRINCE cards");
        assertEquals(1, countCardType(cards, Card.KING), "Shuffled deck should contain 1 KING card");
        assertEquals(1, countCardType(cards, Card.COUNTESS), "Shuffled deck should contain 1 COUNTESS card");
        assertEquals(1, countCardType(cards, Card.PRINCESS), "Shuffled deck should contain 1 PRINCESS card");
    }

    /**
     * Helper method to count occurrences of a specific card type in the deck.
     *
     * @param cards the deque of cards
     * @param cardType the card type to count
     * @return the count of the specified card type in the deck
     */
    private int countCardType(Deque<Card> cards, Card cardType) {
        return (int) cards.stream().filter(card -> card.equals(cardType)).count();
    }

    /**
     * Helper method to access the private 'deck' field using reflection.
     *
     * @return the private deck field as a Deque<Card>
     * @throws NoSuchFieldException if the field does not exist
     * @throws IllegalAccessException if the field is not accessible
     */
    @SuppressWarnings("unchecked")
    private Deque<Card> getDeckField() throws NoSuchFieldException, IllegalAccessException {
        Field deckField = Deck.class.getDeclaredField("deck");
        deckField.setAccessible(true);
        return (Deque<Card>) deckField.get(deck);
    }
}
