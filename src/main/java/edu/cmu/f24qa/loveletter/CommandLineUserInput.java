package edu.cmu.f24qa.loveletter;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * CommandLineUserInput is a concrete implementation of the UserInput interface
 * that uses the command line (Scanner) for input.
 */
public class CommandLineUserInput implements UserInput {

    private Scanner scanner;
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "This is required for card action.")
    private Player sycophantChoice;

    /**
     * Constructor of the CommandLineUserInput class.
     */
    public CommandLineUserInput() {
        scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        sycophantChoice = null;
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

            if (this.sycophantChoice != null) {
                System.out.println("Your choice is subject to Sycophant Morris's action.");
                opponent = this.sycophantChoice;
                this.sycophantChoice = null;
            }

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

            if (this.sycophantChoice != null) {
                System.out.println("Your choice is subject to Sycophant Morris's action.");
                opponent = this.sycophantChoice;
                this.sycophantChoice = null;
            }

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

    /**
     * Prompts the user to choose an opponent from the player list.
     *
     * @param playerList the list of players in the game
     * @param user the player making the selection
     * @param selectSelf whether to allow the user to select themselves
     * @param selectProtected whether to allow the user to select protected players
     * @return the opponent Player
     */
    public Player getOpponent(PlayerList playerList, Player user, boolean selectSelf, boolean selectProtected) {
        while (true) {
            System.out.print("Who would you like to target: ");
            String opponentName = scanner.nextLine();
            Player opponent = playerList.getPlayer(opponentName);

            if (this.sycophantChoice != null) {
                System.out.println("Your choice is subject to Sycophant Morris's action.");
                opponent = this.sycophantChoice;
                this.sycophantChoice = null;
            }

            if (opponent == null) {
                System.out.println("Invalid player name. Please try again.");
                continue;
            }

            if (opponent == user && !selectSelf) {
                System.out.println("You cannot target yourself. Please choose another player.");
                continue;
            }

            if (opponent.isProtected() && !selectProtected) {
                System.out.println("You cannot choose a protected player!");
                continue;
            }

            if (!opponent.hasHandCards()) {
                System.out.println("You cannot choose an eliminated player!");
                continue;
            }

            return opponent;
        }
    }

    /**
     * Sets the player that needs to be selected for next round (if select player action is involved).
     * @param sycophantChoice Player selected.
     */
    @Override
    public void setSycophantChoice(Player sycophantChoice) {
        this.sycophantChoice = sycophantChoice;
    }

    /**
     * Get the number of opponents the user selects.
     *
     * @return the number of opponents as an integer.
     */
    @Override
    public Integer getNumOpponent() {
        while (true) {
            System.out.println("How many players would you like to select (please enter either 1 or 2): ");
            String number = scanner.nextLine();

            if (!number.equals("1") || !number.equals("2")) {
                System.out.println("You can choose either 1 or 2 players. Please try again.");
            }

            return Integer.valueOf(number);
        }
    }
}
