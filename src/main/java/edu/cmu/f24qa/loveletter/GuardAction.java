package edu.cmu.f24qa.loveletter;

import java.util.Scanner;

import org.checkerframework.checker.nullness.qual.Nullable;

public class GuardAction implements CardAction {

    /**
     * Allows the user to guess a card that a player's hand contains (excluding another guard).
     * If the user is correct, the opponent loses the round and must lay down their card.
     * If the user is incorrect, the opponent is not affected.
     *
     * @param inputScanner
     *          the input stream
     * @param user
     *          the player playing the card
     * @param opponent
     *          the player targeted by the card
     */
    @Override
    public void execute(Scanner inputScanner, Player user, @Nullable Player opponent) {
        if (opponent == null) {
            throw new IllegalArgumentException();
        }

        System.out.print("Which card would you like to guess: ");
        String cardName = inputScanner.nextLine();

        Card opponentCard = opponent.viewHandCard(0);
        if (opponentCard.getName().equalsIgnoreCase(cardName)) {
            System.out.println("You have guessed correctly!");
            opponent.eliminate();
        } else {
            System.out.println("You have guessed incorrectly");
        }
    }
}
