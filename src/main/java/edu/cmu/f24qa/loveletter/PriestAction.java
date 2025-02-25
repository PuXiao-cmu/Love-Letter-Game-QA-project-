package edu.cmu.f24qa.loveletter;

public class PriestAction implements CardAction {
    /**
     * Allows the user to see one card in the opponent's hand.
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
        Player opponent = userInput.getOpponent(players, user);

        System.out.println(opponent.getName() + " shows you a " + opponent.viewHandCard(0));
    }
}
