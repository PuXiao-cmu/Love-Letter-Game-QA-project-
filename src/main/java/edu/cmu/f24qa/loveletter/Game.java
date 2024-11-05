package edu.cmu.f24qa.loveletter;

import java.util.Scanner;
import org.checkerframework.checker.nullness.qual.NonNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class Game {
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "It's fine for console reads")
    private Scanner in;
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "It's fine for console reads")
    private GameState gameState;

    /**
     * Constructor for the Game class.
     *
     * @param scannerInput the input scanner
     * @param curGameState the current game state
     */
    public Game(Scanner scannerInput, GameState curGameState) {
        this.in = scannerInput;
        this.gameState = curGameState;
    }

    /**
     * Sets players for the game.
     */
    public void setPlayers() {
        System.out.print("Enter player name (empty when done): ");
        String name = in.nextLine();
        while (!name.isBlank()) {
            gameState.getPlayers().addPlayer(name);
            System.out.print("Enter player name (empty when done): ");
            name = in.nextLine();
        }
    }

    /**
     * The main game loop.
     */
    public void start() {
        PlayerList players = gameState.getPlayers();
        Deck deck = gameState.getDeck();

        while (players.getGameWinner() == null) {
            gameState.resetGameState();
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
        gameState.getPlayers().printUsedPiles();
        System.out.println("\n" + turn.getName() + "'s turn:");
        if (turn.isProtected()) {
            turn.switchProtection();
        }

        turn.receiveHandCard(gameState.getDeck().draw());

        int royaltyPos = turn.handRoyaltyPos();

        if (royaltyPos == 0 && turn.viewHandCard(1).getValue() == 7) {
            playCard(turn.playHandCard(1), turn);
        } else if (royaltyPos == 1 && turn.viewHandCard(0).getValue() == 7) {
            playCard(turn.playHandCard(0), turn);
        } else {
            playCard(getCard(turn), turn);
        }
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
        String name = card.getName();
        int value = card.getValue();
        user.discardCard(card);

        if (value < 4 || value == 5 || value == 6) {
            executeActionCard(name, user);
        } else if (value == 4) {
            System.out.println("You are now protected until your next turn");
        } else if (value == 8) {
            user.eliminate();
        }
    }

    /**
     * Executes the action associated with an action card.
     * Depending on the card's name, performs specific actions
     * involving the user and an opponent.
     *
     * @param name the name of the action card
     * @param user the player playing the card
     */
    private void executeActionCard(String name, Player user) {
        Player opponent = getOpponent(in, gameState.getPlayers(), user);

        switch (name) {
            case "guard" -> useGuard(in, opponent);
            case "preist" -> System.out.println(opponent.getName() + " shows you a " + opponent.viewHandCard(0));
            case "baron" -> useBaron(user, opponent);
            case "prince" -> opponent.eliminate();
            case "king" -> useKing(opponent, user);
            default -> System.out.println("Invalid card");
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
     * Allows the user to guess a card that a player's hand contains (excluding another guard).
     * If the user is correct, the opponent loses the round and must lay down their card.
     * If the user is incorrect, the opponent is not affected.
     *
     * @param inputScanner
     *          the input stream
     * @param opponent
     *          the targeted player
     */
    private void useGuard(Scanner inputScanner, Player opponent) {
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

    /**
     * Allows the user to compare cards with an opponent.
     * If the user's card is of higher value, the opposing player loses the round and their card.
     * If the user's card is of lower value, the user loses the round and their card.
     * If the two players have the same card, their used pile values are compared in the same manner.
     *
     * @param user
     *          the initiator of the comparison
     * @param opponent
     *          the targeted player
     */
    private void useBaron(Player user, Player opponent) {
        Card userCard = user.viewHandCard(0);
        Card opponentCard = opponent.viewHandCard(0);

        int cardComparison = Integer.compare(userCard.getValue(), opponentCard.getValue());
        if (cardComparison > 0) {
            System.out.println("You have won the comparison!");
            opponent.eliminate();
        } else if (cardComparison < 0) {
            System.out.println("You have lost the comparison");
            user.eliminate();
        } else {
            System.out.println("You have the same card!");
            if (opponent.discardedValue() > user.discardedValue()) {
                System.out.println("You have lost the used pile comparison");
                opponent.eliminate();
            } else {
                System.out.println("You have won the used pile comparison");
                user.eliminate();
            }
        }
    }

    /**
     * Allows the user to switch cards with an opponent.
     * Swaps the user's hand for the opponent's.
     *
     * @param user
     *          the initiator of the swap
     * @param opponent
     *          the targeted player
     */
    private void useKing(Player user, Player opponent) {
        Card userCard = user.playHandCard(0);
        Card opponentCard = opponent.playHandCard(0);
        user.receiveHandCard(opponentCard);
        opponent.receiveHandCard(userCard);
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
