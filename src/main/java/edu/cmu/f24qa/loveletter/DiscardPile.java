package edu.cmu.f24qa.loveletter;

import java.util.ArrayList;
import java.util.List;

public class DiscardPile {
    private List<Card> cards;

    public DiscardPile() {
        this.cards = new ArrayList<>();
    }

    public void add(Card c) {
        this.cards.add(c);
    }

    public int value() {
        int ret = 0;
        for (Card c : this.cards) {
            ret = c.getValue();
        }
        return ret;
    }

    public void clear() {
        this.cards.clear();
    }

    public void print() {
        for (Card c : this.cards) {
            System.out.println(c);
        }
    }
}
