package edu.cmu.f24qa.loveletter;

public class PrinceAction implements CardAction {

    /**
     * Forces the opponent to discard their hand and be eliminated from the round.
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

        opponent.eliminate();
    }
}
