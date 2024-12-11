package edu.cmu.f24qa.loveletter;

public class BaronessAction implements CardAction {

    /**
     * Based on the user's choice, reveal the hand card of one or two other player(s)
     * to the user only.
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
        int numOpponent = (int) userInput.getNumOpponent();
        Player opponent1 = userInput.getOpponent(players, user);
        Player opponent2 = null;
        if (numOpponent == 2) {
            opponent2 = userInput.getOpponent(players, user);
        }

        System.out.println(opponent1.getName() + " shows you a " + opponent1.viewHandCard(0));
        if (opponent2 != null) {
            System.out.println(opponent2.getName() + " shows you a " + opponent2.viewHandCard(0));
        }
    }
}
