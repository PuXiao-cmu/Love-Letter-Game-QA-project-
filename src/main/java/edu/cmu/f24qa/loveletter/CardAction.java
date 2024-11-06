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
     */
    void execute(UserInput userInput, Player user, PlayerList players);
}
