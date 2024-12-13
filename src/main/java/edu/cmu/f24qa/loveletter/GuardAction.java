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
     * @param deck
     *          the deck
     */
    @Override
    public void execute(UserInput userInput, Player user, PlayerList players, Deck deck) {
        Player opponent = userInput.getOpponent(players, user);

        int guessedNumber = userInput.getCardNumber();
        Card opponentCard = opponent.viewHandCard(0);

        // Check if the guessed number is valid (excluding Guard, which is 1)
        if (guessedNumber == 1) {
            System.out.println("You cannot guess Guard!");
            return;
        }

        // Compare guessed number with the opponent's card value
        if (opponentCard.getValue() == guessedNumber) {
            System.out.println("You have guessed correctly!");
            opponent.eliminate();
        } else {
            System.out.println("You have guessed incorrectly.");
        }
    }
}
