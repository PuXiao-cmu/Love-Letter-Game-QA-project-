package edu.cmu.f24qa.loveletter;

import org.checkerframework.checker.nullness.qual.Nullable;

public class PriestAction implements CardAction {
    /**
     * Allows the user to see one card in the opponent's hand.
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

        System.out.println(opponent.getName() + " shows you a " + opponent.viewHandCard(0));
    }
}
