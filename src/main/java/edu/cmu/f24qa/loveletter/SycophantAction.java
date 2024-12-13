package edu.cmu.f24qa.loveletter;

public class SycophantAction implements CardAction {
    /**
     * When plays Sycophant, player selects a player; if the next discarded card requires
     * to select a player or players, it must contains the player selected by the player of
     * Sycophant.
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
        Player player = userInput.getOpponent(players, user, true);

        userInput.setSycophantChoice(player);

        System.out.println("Next card's player selections will be affected by your choice.");
    }
}
