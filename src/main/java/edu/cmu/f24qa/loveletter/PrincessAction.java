package edu.cmu.f24qa.loveletter;

import org.checkerframework.checker.nullness.qual.Nullable;

public class PrincessAction implements CardAction {

    /**
     * The Princess eliminates the player who plays her.
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
        if (opponent != null) {
            throw new IllegalArgumentException();
        }
        user.eliminate();
    }
}
