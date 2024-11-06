package edu.cmu.f24qa.loveletter;

import org.checkerframework.checker.nullness.qual.Nullable;

public interface CardAction {

    /**
     * Executes the action of the card.
     *
     * @param userInput
     *          the input stream
     * @param user
     *          the player playing the card
     * @param opponent
     *          the player targeted by the card
     */
    void execute(UserInput userInput, Player user, @Nullable Player opponent);
}
