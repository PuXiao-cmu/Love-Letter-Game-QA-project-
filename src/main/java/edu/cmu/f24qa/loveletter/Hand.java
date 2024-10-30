package edu.cmu.f24qa.loveletter;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    /** The cards in the hand. */
    private List<Card> hand;

    /**
     * Creates an empty hand.
     */
    public Hand() {
        this.hand  = new ArrayList<>();
    }

    /**
     * Peeks the card held by the player.
     *
     * @param idx
     *          the index of the Card to peek
     *
     * @return the card held by the player
     */
    public Card peek(int idx) {
        return this.hand.get(idx);
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param card the card to add
     */
    public void add(Card card) {
        this.hand.add(card);
    }

    /**
     * Removes the card at the given index from the hand.
     *
     * @param idx
     *          the index of the card
     *
     * @return the card at the given index
     */
    public Card remove(int idx) {
        return this.hand.remove(idx);
    }

    /**
     * Finds the position of a royal card in the hand.
     *
     * @return the position of a royal card, -1 if no royal card is in hand
     */
    public int royaltyPos() {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getValue() == 5 || hand.get(i).getValue() == 6) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks if the hand is empty.
     *
     * @return true if the hand has at least one card, false if empty
     */
    public boolean hasCards() {
        return !this.hand.isEmpty();
    }

    /**
     * Clears all the cards from the hand.
     */
    public void clear() {
        this.hand.clear();
    }


    /**
     * Prints each card in the hand to the console.
     */
    public void print() {
        for (Card c : this.hand) {
            System.out.println(c);
        }
    }
}
