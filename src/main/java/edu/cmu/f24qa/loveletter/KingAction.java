package edu.cmu.f24qa.loveletter;

public class KingAction implements CardAction {

    /**
     * Allows the user to switch cards with an opponent.
     * Swaps the user's hand for the opponent's.
     *
     * @param userInput
     *          the input stream
     * @param user
     *          the initiator of the swap
     * @param players
     *          the player list
     * @param deck
     *          the deck
     */
    @Override
    public void execute(UserInput userInput, Player user, PlayerList players, Deck deck) {
        Player opponent = userInput.getOpponent(players, user);

        Card userCard = user.playHandCard(0);
        Card opponentCard = opponent.playHandCard(0);
        user.receiveHandCard(opponentCard);
        opponent.receiveHandCard(userCard);
    }
}
