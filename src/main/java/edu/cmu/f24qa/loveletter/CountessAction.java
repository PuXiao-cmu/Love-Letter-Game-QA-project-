package edu.cmu.f24qa.loveletter;

import org.checkerframework.checker.nullness.qual.Nullable;

public class CountessAction implements CardAction {

    /**
     * If a player has the King or Prince in hand, they must discard the Countess card.
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
        assert true;
    }
}
