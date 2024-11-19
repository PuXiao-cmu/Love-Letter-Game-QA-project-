package edu.cmu.f24qa.loveletter;

public class PrincessAction implements CardAction {

    /**
     * The Princess eliminates the player who plays her.
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
    @Override
    public void execute(UserInput userInput, Player user, PlayerList players, Deck deck) {
        user.eliminate();
    }
}
