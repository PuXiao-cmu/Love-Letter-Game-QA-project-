package edu.cmu.f24qa.loveletter;

public class BishopAction implements CardAction {
    /**
     * When Bishop is discarded:
     * 1. Player names a number and chooses a target player
     * 2. If target has that number: 
     *    - Player gains Token of Affection
     *    - Check for immediate win
     *    - Target may discard and draw (unless Princess)
     * 3. For hand comparisons, Princess beats Bishop
     * 4. For other effects, Bishop value is 9
     *
     * @param userInput the input stream
     * @param user the player playing the card
     * @param players the player list
     * @param deck the deck
     */
    @Override
    public void execute(UserInput userInput, Player user, PlayerList players, Deck deck) {
        Player opponent = userInput.getOpponent(players, user);

        // Get number guess from user
        int guessNumber = userInput.getCardNumber();
        Card opponentCard = opponent.viewHandCard(0);

        // Check if guess matches opponent's card value
        if (opponentCard.getValue() == guessNumber) {
            // Award Token of Affection
            user.addToken();

            // Check if this causes immediate win
            if (players.getGameWinnerCandidates() == user) {
                return; // Game ends immediately
            }

            // Handle target player's card (unless it's Princess)
            if (opponentCard != Card.PRINCESS) {
                opponent.discardCard(opponent.playHandCard(0));
                if (deck.hasMoreCards()) {
                    opponent.receiveHandCard(deck.draw());
                }
            }
        }
    }
}
