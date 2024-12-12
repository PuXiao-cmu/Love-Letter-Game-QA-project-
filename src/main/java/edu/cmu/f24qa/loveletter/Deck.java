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
    private Card topCard;
    private List<Card> revealedCards;

    /**
     * Constructor for creating an empty deck.
     */
    public Deck() {
        this.deck = new ArrayDeque<>();
        topCard = null;
        revealedCards = new ArrayList<>();
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
     * Builds the deck for the 5-8 players mode.
     */
    public void buildPremium() {
        build();
        for (int i = 0; i < 3; i++) {
            deck.push(Card.GUARD);
        }
        for (int i = 0; i < 2; i++) {
            deck.push(Card.CARDINAL);
            deck.push(Card.BARONESS);
            deck.push(Card.SYCOPHANT);
            deck.push(Card.COUNT);
        }
        deck.push(Card.JESTER);
        deck.push(Card.ASSASSIN);
        deck.push(Card.CONSTABLE);
        deck.push(Card.DOWAGERQUEEN);
        deck.push(Card.BISHOP);
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
     * Hides the top card of each new set of card in the deck.
     */
    public void hideTopCard() {
        topCard = deck.pop();
    }

    /**
     * Deals user the top card of the initial deck of each round.
     * @return Return the top card of the initial deck.
     */
    public Card useHiddenCard() {
        Card temp = topCard;
        return temp;
    }

    /**
     * Checks if the hidden card has been dealt to a player.
     * @return true if the hidden card has not been dealt, false otherwise.
     */
    public boolean hasHiddenCard() {
        return topCard != null;
    }

    /**
     * Removes another three cards and reveals them to the players.
     */
    public void removeAnotherThreeCards() {
        for (int i = 0; i < 3; i++) {
            Card temp = draw();
            revealedCards.add(temp);
            System.out.println("Face-up card: " + temp.getName());
        }
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
