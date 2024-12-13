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
        // Eliminate the player if opponent card is Assassin
        if (opponentCard == Card.ASSASSIN) {
            System.out.println("Opponent has assassinated you!");
            // Eliminate the player
            user.eliminate();

            // Opponent redraw a new card
            opponent.discardCard(opponent.playHandCard(0));
            // Check if the deck is empty
            if (deck.hasMoreCards()) {
                // Allow the opponent to draw a new card from the deck
                opponent.receiveHandCard(deck.draw());
            } else if (deck.hasHiddenCard()) {
                // If the deck is empty, draw the hidden card
                opponent.receiveHandCard(deck.useHiddenCard());
            }
            return;
        }

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
