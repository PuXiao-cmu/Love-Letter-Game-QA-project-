package edu.cmu.f24qa.loveletter;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Represents a player in the Love Letter game.
 */
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

    /**
     * Records the player who predicted this player will win.
     */
    private Player jesterPredictor;

    /**
     * Constructs a new player with the specified name.
     *
     * @param playerName The name of the player
     */
    public Player(String playerName) {
        this.name = playerName;
        this.hand = new Hand();
        this.discarded = new DiscardPile();
        this.protectedStatus = false;
        this.tokens = 0;
    }

    /**
     * Adds a token.
     */
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
     * Checks to see if the player is protected by a handmaiden.
     *
     * @return true if the player is protected, false otherwise
     */
    public boolean isProtected() {
        return this.protectedStatus;
    }

    /**
     * Gets the number of tokens the player has.
     *
     * @return the number of tokens the player has
     */
    public int getTokens() {
        return this.tokens;
    }

    /**
     * Gets the player's name.
     *
     * @return the player's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns a string of the player's name and token count.
     *
     * @return the string representation of the player
     */
    @Override
    public String toString() {
        return this.name + " (" + this.tokens + " tokens)";
    }

    /**
     * Sets the predictor who used Jester card on this player.
     *
     * @param predictor the player who made the prediction
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Design requires direct setting of predictor")
    public void setJesterPredictor(Player predictor) {
        this.jesterPredictor = predictor;
    }

    /**
     * Gets the player who predicted this player will win.
     *
     * @return the predictor, or null if none
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "Design requires direct access to predictor")
    public Player getJesterPredictor() {
        return jesterPredictor;
    }

    /**
     * Clears the Jester prediction.
     */
    public void clearJesterPrediction() {
        this.jesterPredictor = null;
    }
}
