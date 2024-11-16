package edu.cmu.f24qa.loveletter;


public class BaronAction implements CardAction {

    /**
     * Allows the user to compare cards with an opponent.
     * If the user's card is of higher value, the opposing player loses the round and their card.
     * If the user's card is of lower value, the user loses the round and their card.
     * If the two players have the same card, their used pile values are compared in the same manner.
     *
     * @param userInput
     *          the input stream
     * @param user
     *          the player playing the card
     * @param players
     *          the player list
     */
    @Override
    public void execute(UserInput userInput, Player user, PlayerList players) {
        Player opponent = userInput.getOpponent(players, user);

        if (opponent == null) {
            System.out.println("No valid opponent selected.");
            return;
        }

        Card userCard = user.viewHandCard(0);
        Card opponentCard = opponent.viewHandCard(0);

        int cardComparison = Integer.compare(userCard.getValue(), opponentCard.getValue());
        if (cardComparison > 0) {
            System.out.println("You have won the comparison!");
            opponent.eliminate();
        } else if (cardComparison < 0) {
            System.out.println("You have lost the comparison");
            user.eliminate();
        } else {
            System.out.println("You have the same card - no one is eliminated!");
        }
    }
}
