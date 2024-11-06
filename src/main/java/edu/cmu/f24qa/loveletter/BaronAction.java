package edu.cmu.f24qa.loveletter;

import org.checkerframework.checker.nullness.qual.Nullable;


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
     * @param opponent
     *          the player targeted by the card
     */
    @Override
    public void execute(UserInput userInput, Player user, @Nullable Player opponent) {
        if (opponent == null) {
            throw new IllegalArgumentException();
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
            System.out.println("You have the same card!");
            if (opponent.discardedValue() > user.discardedValue()) {
                System.out.println("You have lost the used pile comparison");
                opponent.eliminate();
            } else {
                System.out.println("You have won the used pile comparison");
                user.eliminate();
            }
        }
    }
}
