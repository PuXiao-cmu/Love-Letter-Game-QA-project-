package edu.cmu.f24qa.loveletter;

public class Player {
    private String name;
    private Hand hand;

    private DiscardPile discarded;

    /**
     * True if the player is protected by a handmaiden, false if not.
     */
    private boolean protectedStatus;

    /**
     * The number of blocks the player has won.
     */
    private int tokens;

    public Player(String name) {
        this.name = name;
        this.hand = new Hand();
        this.discarded = new DiscardPile();
        this.protectedStatus = false;
        this.tokens = 0;
    }

    public void addToken() {
        this.tokens++;
    }

    /**
     * Eliminates the player from the round by discarding their hand.
     */
    public void eliminate() {
        this.discarded.add(this.hand.remove(0));
    }

    /**
     * Switches the user's level of protection.
     */
    public void switchProtection() {
        this.protectedStatus = !this.protectedStatus;
    }

    /**
     * Removes the card at the given index from the player's hand.
     * 
     * @param index the index of the card to remove
     * @return the card that was removed
     */
    public Card playHandCard(int index) {
        return this.hand.remove(index);
    }

    /**
     * Adds a card to the player's hand.
     * 
     * @param card the card to add
     */
    public void receiveHandCard(Card card) {
        this.hand.add(card);
    }

    /**     
     * Returns the card at the given index in the player's hand.
     * 
     * @param index the index of the card to return
     * @return the card at the given index
     */
    public Card viewHandCard(int index) {
        return this.hand.peek(index);
    }

    /**
     * Checks if the player has any cards in their hand.
     * 
     * @return true if the player has any cards, false if not
     */
    public boolean hasHandCards() {
        return this.hand.hasCards();
    }

    /**
     * Prints the player's hand.
     */
    public void printHand() {
        this.hand.print();
    }

    /**
     * Finds the position of a royal card in the hand.
     * 
     * @return the position of a royal card, -1 if no royal card is in hand
     */
    public int handRoyaltyPos() {
        return this.hand.royaltyPos();
    }

    /**
     * Clears all the cards in the player's hand.
     */
    public void clearHand() {
        this.hand.clear();
    }

    /**
     * Adds a card to the player's discard pile.
     *
     * @param card the card to be discarded
     */
    public void discardCard(Card card) {
        this.discarded.add(card);
    }

    /**
     * Calculates the total value of the cards in the player's discard pile.
     * 
     * @return the total value of the discarded cards
     */
    public int discardedValue() {
        return this.discarded.value();
    }

    /**
     * Prints the cards in the player's discard pile.
     */
    public void printDiscarded() {
        this.discarded.print();
    }

    /**
     * Clears all the cards in the player's discard pile.
     */
    public void clearDiscarded() {
        this.discarded.clear();
    }

    /**
     * Checks to see if the user is protected by a handmaiden.
     *
     * @return true, if the player is protected, false if not
     */
    public boolean isProtected() {
        return this.protectedStatus;
    }

    public int getTokens() {
        return this.tokens;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name + " (" + this.tokens + " tokens)";
    }
}
