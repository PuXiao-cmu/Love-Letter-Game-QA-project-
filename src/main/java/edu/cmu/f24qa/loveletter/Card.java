package edu.cmu.f24qa.loveletter;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Enumeration for different types of cards with names and values.
 */
public enum Card {
    GUARD("Guard", 1, new GuardAction()),
    PRIEST("Priest", 2, new PriestAction()),
    BARON("Baron", 3, new BaronAction()),
    HANDMAIDEN("Handmaiden", 4, new HandmaidAction()),
    PRINCE("Prince", 5, new PrinceAction()),
    KING("King", 6, new KingAction()),
    COUNTESS("Countess", 7, new CountessAction()),
    PRINCESS("Princess", 8, new PrincessAction());

    /**
     * The name of the card.
     */
    private final String name;

    /**
     * The value of the card.
     */
    private final int value;

    /**
     * The action associated with the card.
     */
    private final CardAction action;

    /**
     * All possible card names.
     */
    public static final String[] CARD_NAMES = {
        "guard",
        "priest",
        "baron",
        "handmaiden",
        "prince",
        "king",
        "countess",
        "princess"
    };

    /**
     * Constructor for a card object.
     *
     * @param cardName
     *          the name of the card
     * @param cardValue
     *          the value of the card
     * @param cardAction
     *          the action associated with the card
     */
    Card(String cardName, int cardValue, CardAction cardAction) {
        this.name = cardName;
        this.value = cardValue;
        this.action = cardAction;
    }

    /**
     * Gets the value of the card.
     *
     * @return the integer value of the card
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Gets the name of the card.
     *
     * @return the name of the card
     */
    public String getName() {
        return this.name;
    }

    /**
     * Executes the action associated with the card.
     *
     * @param userInput
     *          the input stream
     * @param user
     *          the player playing the card
     * @param opponent
     *          the targeted player
     */
    public void execute(UserInput userInput, Player user, @Nullable Player opponent) {
        this.action.execute(userInput, user, opponent);
    }

    /**
     * Provides a string representation of the card in the format "name (value)".
     *
     * @return the string representation of the card
     */
    @Override
    public String toString() {
        return this.name + " (" + value + ")";
    }
}
