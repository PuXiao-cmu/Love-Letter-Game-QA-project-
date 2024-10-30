package edu.cmu.f24qa.loveletter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * Represents a deck of cards used in the Love Letter game.
 */
public class Deck {
    /**
     * The queue of cards representing the deck.
     */
    private Deque<Card> deck;

    /**
     * Constructor for creating an empty deck.
     */
    public Deck() {
        this.deck = new ArrayDeque<>();
    }

    /**
     * Builds the deck with a predefined set of cards.
     */
    public void build() {
        for (int i = 0; i < 5; i++) {
            deck.push(Card.GUARD);
        }
        for (int i = 0; i < 2; i++) {
            deck.push(Card.PRIEST);
            deck.push(Card.BARON);
            deck.push(Card.HANDMAIDEN);
            deck.push(Card.PRINCE);
        }
        deck.push(Card.KING);
        deck.push(Card.COUNTESS);
        deck.push(Card.PRINCESS);
    }

    /**
     * Shuffles the deck.
     */
    public void shuffle() {
        List<Card> list = new ArrayList<>(deck);
        Collections.shuffle(list);
        deck.clear();
        deck.addAll(list);
    }

    /**
     * Draws a card from the deck.
     *
     * @return the card that was drawn from the deck
     */
    public Card draw() {
        return deck.pop();
    }

    /**
     * Checks if there is any card left in the deck.
     *
     * @return true if there are cards remaining in the deck, false otherwise
     */
    public boolean hasMoreCards() {
        return !deck.isEmpty();
    }
}
