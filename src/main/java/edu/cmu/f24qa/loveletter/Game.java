package edu.cmu.f24qa.loveletter;

import java.util.Scanner;
import org.checkerframework.checker.nullness.qual.NonNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class Game {
    private PlayerList players;
    private Deck deck;
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "It's fine for console reads")
    private Scanner in;
    @SuppressFBWarnings(value = "URF_UNREAD_FIELD", justification = "Might be used later")
    private int round;

    /**
     * Constructor for the Game class.
     *
     * @param scannerInput the input scanner
     */
    public Game(Scanner scannerInput) {
        this.players = new PlayerList();
        this.deck = new Deck();
        this.in = scannerInput;
        this.round = 0;
    }

    /**
     * Sets players for the game.
     */
    public void setPlayers() {
        System.out.print("Enter player name (empty when done): ");
        String name = in.nextLine();
        while (!name.isBlank()) {
            this.players.addPlayer(name);
            System.out.print("Enter player name (empty when done): ");
            name = in.nextLine();
        }
    }

    /**
     * The main game loop.
     */
    public void start() {
        while (players.getGameWinner() == null) {
            players.reset();
            setDeck();
            players.dealCards(deck);
            while (!players.checkForRoundWinner() && deck.hasMoreCards()) {
                Player turn = players.getCurrentPlayer();

                if (turn.hasHandCards()) {
                    executeTurn(turn);
                }
            }

            Player winner;
            if (players.checkForRoundWinner() && players.getRoundWinner() != null) {
                winner = players.getRoundWinner();
            } else {
                winner = players.compareUsedPiles();
                winner.addToken();
            }
            winner.addToken();
            System.out.println(winner.getName() + " has won this round!");
            players.print();
        }
        Player gameWinner  = players.getGameWinner();
        System.out.println(gameWinner  + " has won the game and the heart of the princess!");

    }

    /**
     * Executes a player's turn in the game.
     *
     * @param turn
     *      the player whose turn it is
     */
    private void executeTurn(Player turn) {
        players.printUsedPiles();
        System.out.println("\n" + turn.getName() + "'s turn:");
        if (turn.isProtected()) {
            turn.switchProtection();
        }

        turn.receiveHandCard(deck.draw());
        int royaltyPos = turn.handRoyaltyPos();

        if (royaltyPos == 0 && turn.viewHandCard(1).getValue() == 7) {
            playCard(turn.playHandCard(1), turn);
        } else if (royaltyPos == 1 && turn.viewHandCard(0).getValue() == 7) {
            playCard(turn.playHandCard(0), turn);
        } else {
            playCard(getCard(turn), turn);
        }
    }

    private void setDeck() {
        this.deck.build();
        this.deck.shuffle();
    }

    /**
     * Plays a card from the user's hand.
     *
     * @param card
     *          the played card
     * @param user
     *          the player of the card
     */
    private void playCard(Card card, Player user) {
        int value = card.getValue();
        user.discardCard(card);

        if (value < 4 || value == 5 || value == 6) {
            Player opponent = getOpponent(in, players, user);
            card.execute(in, user, opponent);
        } else {
            card.execute(in, user, null);
        }
    }

    /**
     * Allows for the user to pick a card from their hand to play.
     *
     * @param user
     *      the current player
     * @return the chosen card
     */
    private Card getCard(Player user) {
        user.printHand();
        System.out.println();
        System.out.print("Which card would you like to play (0 for first, 1 for second): ");
        String cardPosition = in.nextLine();
        int idx = Integer.parseInt(cardPosition);
        return user.playHandCard(idx);
    }

    /**
     * Useful method for obtaining a chosen target from the player list.
     *
     * @param inputScanner
     *          the input stream
     * @param playerList
     *          the list of players
     * @param user
     *          the player choosing an opponent
     * @return the chosen target player
     */
    private @NonNull Player getOpponent(Scanner inputScanner, PlayerList playerList, Player user) {
        while (true) {
            System.out.print("Who would you like to target: ");
            String opponentName = inputScanner.nextLine();
            Player opponent = playerList.getPlayer(opponentName);
            if (opponent == null) {
                System.out.println("Invalid player name. Please try again.");
                continue;
            }

            if (opponent == user) {
                System.out.println("You cannot target yourself. Please choose another player.");
                continue;
            }

            return opponent;
        }
    }
}
