package edu.cmu.f24qa.loveletter;

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
     * Checks if there is at least one valid target for the current card.
     *
     * A valid target is a player who:
     * 1. Is not protected by the Handmaid card.
     * 2. (Optional) May or may not include the player using the card, based on the `includeSelf` parameter.
     *
     * @param players    the list of all players in the game
     * @param user       the player who is using the card
     * @param includeSelf whether to include the user as a potential target
     * @return true if at least one valid target is found; false otherwise
     */
    private boolean hasValidTarget(PlayerList players, Player user, boolean includeSelf) {
        for (Player player : players.players) {
            if ((includeSelf || player != user) && !player.isProtected()) {
                return true; // A valid target is found
            }
        }
        return false; // No valid targets
    }

    /**
     * Executes the action associated with the card.
     *
     * @param userInput
     *          the input stream
     * @param user
     *          the player playing the card
     * @param players
     *          the player list
     * @param deck
     *          the deck
     */
    public void execute(UserInput userInput, Player user, PlayerList players, Deck deck) {
        boolean hasValidTarget = false;

        // Determine if the card has valid targets
        if (this == PRINCE) {
            hasValidTarget = hasValidTarget(players, user, true); // Include self
        } else if (this == KING || this == BARON || this == PRIEST || this == GUARD) {
            hasValidTarget = hasValidTarget(players, user, false); // Exclude self
        }

        // If no valid targets are available, discard the card without applying the effect
        if (!hasValidTarget) {
            System.out.println("No valid targets available. Card discarded without effect.");
            user.discardCard(this);
            return;
        }

        // Execute the card action if there are valid targets
        this.action.execute(userInput, user, players, deck);
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
