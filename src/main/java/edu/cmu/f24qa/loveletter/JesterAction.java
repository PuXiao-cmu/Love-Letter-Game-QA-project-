package edu.cmu.f24qa.loveletter;

public class JesterAction implements CardAction {

    /**
     * Executes the Jester card action.
     * Allows the user to predict a player who they believe will win the round.
     *
     * @param userInput the input stream
     * @param user the player playing the card
     * @param players the player list
     * @param deck the deck
     */
    @Override
    public void execute(UserInput userInput, Player user, PlayerList players, Deck deck) {
        // Prompt the user to choose a player to predict
        System.out.println("Choose a player you think will win the round:");
        Player predictedPlayer = userInput.getOpponent(players, user, false, true);

        // Set the Jester prediction in the target player
        predictedPlayer.setJesterPredictor(user);

        System.out.println(user.getName() + " predicts that " + predictedPlayer.getName() + " will win the round.");
    }
}
