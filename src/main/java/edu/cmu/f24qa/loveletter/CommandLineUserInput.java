package edu.cmu.f24qa.loveletter;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * CommandLineUserInput is a concrete implementation of the UserInput interface
 * that uses the command line (Scanner) for input.
 */
public class CommandLineUserInput implements UserInput {

    private Scanner scanner;

    /**
     * Constructor of the CommandLineUserInput class.
     */
    public CommandLineUserInput() {
        scanner = new Scanner(System.in, StandardCharsets.UTF_8);
    }

    /**
     * Prompts the user to enter player names and returns a PlayerList.
     *
     * @return a PlayerList containing the players entered by the user
     */
    @Override
    public PlayerList getPlayers() {
        PlayerList players = new PlayerList();
        System.out.print("Enter player name (empty when done): ");
        String name = scanner.nextLine();
        while (!name.isBlank()) {
            players.addPlayer(name);
            System.out.print("Enter player name (empty when done): ");
            name = scanner.nextLine();
        }
        return players;
    }

    /**
     * Prompts the user to select a card index from their hand.
     *
     * @param user the player making the selection
     * @return the index of the selected card as a string
     */
    @Override
    public String getCardIndex(Player user) {
        user.printHand();
        System.out.println();
        System.out.print("Which card would you like to play (0 for first, 1 for second): ");
        return scanner.nextLine();
    }

    /**
     * Prompts the user to enter the name of a card for guessing.
     *
     * @return the name of the card guessed by the user
     */
    @Override
    public String getCardName() {
        System.out.print("Which card would you like to guess: ");
        return scanner.nextLine();
    }

    /**
     * Prompts the user to choose an opponent from the player list.
     */
    @Override
    public String getOpponentName() {
        return scanner.nextLine();
    }
}
