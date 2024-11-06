package edu.cmu.f24qa.loveletter;

public class GuardAction implements CardAction {

    /**
     * Allows the user to guess a card that a player's hand contains (excluding another guard).
     * If the user is correct, the opponent loses the round and must lay down their card.
     * If the user is incorrect, the opponent is not affected.
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

        String cardName = userInput.getCardName();

        Card opponentCard = opponent.viewHandCard(0);
        if (opponentCard.getName().equalsIgnoreCase(cardName)) {
            System.out.println("You have guessed correctly!");
            opponent.eliminate();
        } else {
            System.out.println("You have guessed incorrectly");
        }
    }
}
