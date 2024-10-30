package edu.cmu.f24qa.loveletter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class Deck {
    private Deque<Card> deck;

    public Deck() {
        this.deck = new ArrayDeque<>();
    }

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

    public void shuffle() {
        List<Card> list = new ArrayList<>(deck);
        Collections.shuffle(list);
        deck.clear();
        deck.addAll(list);
    }

    public Card draw() {
        return deck.pop();
    }

    public boolean hasMoreCards() {
        return !deck.isEmpty();
    }
}