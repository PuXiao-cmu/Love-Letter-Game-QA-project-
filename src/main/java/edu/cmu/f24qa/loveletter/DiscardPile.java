package edu.cmu.f24qa.loveletter;

import java.util.List;
import java.util.ArrayList;

/**
 * Class representing cards discarded for the round.
 */
public class DiscardPile {
    /**
     * The list of discarded cards.
     */
    private List<Card> cards;

    /**
     * Constructs an empty discard pile.
     */
    public DiscardPile() {
        this.cards = new ArrayList<>();
    }

    /**
     * Adds a card to the discard pile.
     *
     * @param c the card to add
     */
    public void add(Card c) {
        this.cards.add(c);
    }

    /**
     * Returns the value of the last card in the discard pile.
     *
     * @return the value of the last card in the discard pile
     */
    public int value() {
        int ret = 0;
        for (Card c : this.cards) {
            ret += c.getValue();
        }
        return ret;
    }

    /**
     * Clears all cards from the discard pile.
     */
    public void clear() {
        this.cards.clear();
    }

    /**
     * Prints the cards in the discard pile to the console.
     */
    public void print() {
        for (Card c : this.cards) {
            System.out.println(c);
        }
    }

    /**
     * Counts the number of "Count" cards in the discard pile.
     *
     * @return The number of "Count" cards in the discard pile.
     */
    public int numCountCard() {
        int num = 0;
        for (Card card: cards) {
            if (card.getName().equalsIgnoreCase("count")) {
                num++;
            }
        }
        return num;
    }
}
