package edu.cmu.f24qa.loveletter;

import org.checkerframework.checker.nullness.qual.Nullable;

public class KingAction implements CardAction {

    /**
     * Allows the user to switch cards with an opponent.
     * Swaps the user's hand for the opponent's.
     *
     * @param userInput
     *          the input stream
     * @param user
     *          the initiator of the swap
     * @param opponent
     *          the targeted player
     */
    @Override
    public void execute(UserInput userInput, Player user, @Nullable Player opponent) {
        if (opponent == null) {
            throw new IllegalArgumentException();
        }

        Card userCard = user.playHandCard(0);
        Card opponentCard = opponent.playHandCard(0);
        user.receiveHandCard(opponentCard);
        opponent.receiveHandCard(userCard);
    }
}
