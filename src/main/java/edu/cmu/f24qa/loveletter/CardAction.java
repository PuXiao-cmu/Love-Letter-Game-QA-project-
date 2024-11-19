package edu.cmu.f24qa.loveletter;

public interface CardAction {

    /**
     * Executes the action of the card.
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
    void execute(UserInput userInput, Player user, PlayerList players, Deck deck);
}
