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
     * Prompts the user to enter the number of a card for guessing.
     * @return the number guessed by the user
     */
    @Override
    public int getCardNumber() {
    while (true) {
        System.out.print("Which number would you like to guess: ");
        try {
            int guess = Integer.parseInt(scanner.nextLine());
            return guess;
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }
}

    /**
     * Prompts the user to choose an opponent from the player list.
     *
     * @param playerList the list of players in the game
     * @param user the player making the selection
     * @return the opponent Player
     */
    @Override
    public Player getOpponent(PlayerList playerList, Player user) {
        while (true) {
            System.out.print("Who would you like to target: ");
            String opponentName = scanner.nextLine();
            Player opponent = playerList.getPlayer(opponentName);
            if (opponent == null) {
                System.out.println("Invalid player name. Please try again.");
                continue;
            }

            if (opponent == user) {
                System.out.println("You cannot target yourself. Please choose another player.");
                continue;
            }

            if (opponent.isProtected()) {
                System.out.println("You cannot choose protected player!");
                continue;
            }

            if (!opponent.hasHandCards()) {
                System.out.println("You cannot choose eliminated player!");
                continue;
            }

            return opponent;
        }
    }

    /**
     * Prompts the user to choose an opponent from the player list.
     *
     * @param playerList the list of players in the game
     * @param user the player making the selection
     * @param selectSelf whether to allow the user to select themselves
     * @return the opponent Player
     */
    @Override
    public Player getOpponent(PlayerList playerList, Player user, boolean selectSelf) {
        while (true) {
            System.out.print("Who would you like to target: ");
            String opponentName = scanner.nextLine();
            Player opponent = playerList.getPlayer(opponentName);
            if (opponent == null) {
                System.out.println("Invalid player name. Please try again.");
                continue;
            }

            if (opponent == user && !selectSelf) {
                System.out.println("You cannot target yourself. Please choose another player.");
                continue;
            }

            if (opponent.isProtected()) {
                System.out.println("You cannot choose protected player!");
                continue;
            }

            if (!opponent.hasHandCards()) {
                System.out.println("You cannot choose eliminated player!");
                continue;
            }

            return opponent;
        }
    }
}
