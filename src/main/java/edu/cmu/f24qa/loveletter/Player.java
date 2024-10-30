package edu.cmu.f24qa.loveletter;

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
    private boolean isProtected;

    /**
     * The number of blocks the player has won.
     */
    private int tokens;

    /**
     * Constructs a new player with the specified name.
     *
     * @param playerName The name of the player
     */
    public Player(String playerName) {
        this.name = playerName;
        this.hand = new Hand();
        this.discarded = new DiscardPile();
        this.isProtected = false;
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
        this.isProtected = !this.isProtected;
    }

    /**
     * Gets the player's hand of cards.
     *
     * @return the player's hand
     */
    public Hand getHand() {
        return this.hand;
    }

    /**
     * Gets the player's discarded pile.
     *
     * @return the player's discarded pile
     */
    public DiscardPile getDiscarded() {
        return this.discarded;
    }

    /**
     * Checks to see if the player is protected by a handmaiden.
     *
     * @return true if the player is protected, false otherwise
     */
    public boolean isProtected() {
        return this.isProtected;
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
}
