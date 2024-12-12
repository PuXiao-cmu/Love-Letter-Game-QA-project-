package edu.cmu.f24qa.loveletter;

import java.util.Set;

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
    PRINCESS("Princess", 8, new PrincessAction()),
    JESTER("Jester", 0, new JesterAction()),
    ASSASSIN("Assassin", 0, new AssassinAction()),
    CARDINAL("Cardinal", 2, new CardinalAction()),
    BARONESS("Baroness", 3, new BaronessAction()),
    SYCOPHANT("Sycophant", 4, new SycophantAction()),
    COUNT("Count", 5, new CountAction()),
    CONSTABLE("Constable", 6, new ConstableAction()),
    DOWAGERQUEEN("DowagerQueen", 7, new DowagerQueenAction()),
    BISHOP("Bishop", 9, new BishopAction());


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
        "princess",
        "jester",
        "assassin",
        "bishop",
        "dowager queen",
        "constable",
        "count",
        "sycophant",
        "baroness",
        "cardinal"
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
     * Counts the number of valid targets for the current card.
     *
     * A valid target is a player who:
     * 1. Is not protected by the Handmaid card.
     * 2. (Optional) May or may not include the player using the card, based on the `includeSelf` parameter.
     *
     * @param players    the list of all players in the game
     * @param user       the player who is using the card
     * @param includeSelf whether to include the user as a potential target
     * @return the number of valid targets
     */
    private int countValidTargets(PlayerList players, Player user, boolean includeSelf) {
        int validTargetCount = 0;
        for (Player player : players.players) {
            if ((includeSelf || player != user) && !player.isProtected() && player.hasHandCards()) {
                validTargetCount++;
            }
        }
        return validTargetCount;
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
        int countValidTargets;

        switch (this.name) {
            case "Guard": // 1
            case "Priest": // 2
            case "Baron": // 3
            case "King": // 6
            case "Baroness": // 3
            case "Dowager Queen": // 7
            case "Bishop": // 9
                countValidTargets = countValidTargets(players, user, false);
                if (countValidTargets < 1) {
                    System.out.println("No valid targets available. Card discarded without effect.");
                    return;
                }
                this.action.execute(userInput, user, players, deck);
                break;

            case "Handmaiden": // 4
            case "Countess": // 7
            case "Princess": // 8
                this.action.execute(userInput, user, players, deck);
                break;

            case "Prince": // 5
            case "Sycophant": // 9
                countValidTargets = countValidTargets(players, user, true);
                if (countValidTargets < 1) {
                    System.out.println("No valid targets available. Card discarded without effect.");
                    return;
                }
                this.action.execute(userInput, user, players, deck);
                break;

            case "Cardinal": // 2
                countValidTargets = countValidTargets(players, user, true);
                if (countValidTargets < 2) {
                    System.out.println("No valid targets available. Card discarded without effect.");
                    return;
                }
                this.action.execute(userInput, user, players, deck);
                break;

            default:
                System.out.println("Unknown card: " + this.name);
                return;
        }
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
