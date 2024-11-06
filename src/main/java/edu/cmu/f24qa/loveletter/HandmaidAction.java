package edu.cmu.f24qa.loveletter;

import org.checkerframework.checker.nullness.qual.Nullable;

public class HandmaidAction implements CardAction {

    /**
     * Allows the user to gain temporary protection until their next turn.
     * If the user is not already protected, they are protected from being
     * targeted by another player's card until their next turn.
     *
     * @param userInput
     *          the input stream
     * @param user
     *          the player playing the card
     * @param opponent
     *          the targeted player
     */
    @Override
    public void execute(UserInput userInput, Player user, @Nullable Player opponent) {
        if (opponent != null) {
            throw new IllegalArgumentException();
        }
        if (!user.isProtected()) {
            user.switchProtection();
        }
        System.out.println("You are now protected until your next turn");
    }
}
