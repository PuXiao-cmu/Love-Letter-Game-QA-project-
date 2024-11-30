package edu.cmu.f24qa.loveletter;

public class PrinceAction implements CardAction {

    /**
     * Force a player of your choice to discard the card in their hand. They do not
     * perform the card’s action. (But if it’s the Princess, they are eliminated!) They immediately
     * draw a new card. If the deck is empty, the player draws the hidden card.
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
        // Get the opponent (Can be the player itself)
        Player opponent = userInput.getOpponent(players, user, true);

        // Get the opponent's card
        Card opponentCard = opponent.viewHandCard(0);

        if (opponentCard == Card.PRINCESS) {
            // Eliminate the opponent
            opponent.eliminate();
        } else {
            // Allow the opponent to draw a new card
            opponent.discardCard(opponent.playHandCard(0));

            // Check if the deck is empty
            if (deck.hasMoreCards()) {
                // Allow the opponent to draw a new card from the deck
                opponent.receiveHandCard(deck.draw());
            } else if (deck.hasHiddenCard()) {
                // If the deck is empty, draw the hidden card
                opponent.receiveHandCard(deck.useHiddenCard());
            }
        }
    }
}
