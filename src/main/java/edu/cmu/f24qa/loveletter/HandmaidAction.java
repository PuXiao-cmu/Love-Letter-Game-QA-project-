package edu.cmu.f24qa.loveletter;

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
     * @param players
     *          the player list
     */
    @Override
    public void execute(UserInput userInput, Player user, PlayerList players) {
        if (!user.isProtected()) {
            user.switchProtection();
        }
        System.out.println("You are now protected until your next turn");
    }
}
