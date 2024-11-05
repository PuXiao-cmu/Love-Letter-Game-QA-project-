package edu.cmu.f24qa.loveletter;

import java.util.Scanner;

import org.checkerframework.checker.nullness.qual.Nullable;

public class CountessAction implements CardAction {

    /**
     * If the user has the King or Prince in their hand, the user
     * cannot play the Countess. Otherwise, the Countess acts as a
     * null card and does nothing.
     *
     * @param inputScanner
     *          the input stream
     * @param user
     *          the player playing the card
     * @param opponent
     *          the player targeted by the card
     */
    @Override
    public void execute(Scanner inputScanner, Player user, @Nullable Player opponent) {
        throw new UnsupportedOperationException();
    }
}
