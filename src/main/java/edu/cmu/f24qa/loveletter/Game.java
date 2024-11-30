package edu.cmu.f24qa.loveletter;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class Game {
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "It's fine for console reads")
    private UserInput userInput;
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "DI is required for testing purposes")
    private PlayerList players;
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "DI is required for testing purposes")
    private Deck deck;
    @SuppressFBWarnings(value = "URF_UNREAD_FIELD", justification = "Might be used later")
    private int round;

    /**
     * Constructor for the Game class.
     *
     * @param curUserInput an object used to handle user input
     * @param curPlayers the current players
     * @param curDeck the current deck
     */
    public Game(UserInput curUserInput, PlayerList curPlayers, Deck curDeck) {
        this.userInput = curUserInput;
        this.players = curPlayers;
        this.deck = curDeck;
        this.round = 0;
    }

    /**
     * Sets players for the game.
     */
    public void setPlayers() {
        this.players = userInput.getPlayers();
    }

    /**
     * Reset players, rebuild deck, shuffle and deal cards.
     */
    public void resetGame() {
        players.reset();
        deck.build();
        deck.shuffle();
        deck.hideTopCard();
        players.dealCards(deck);
    }

    /**
     * The main game loop.
     */
    public void start() {
        while (!isGameOver()) {
            playRound();
        }
        announceGameWinner();
    }

    /**
     * Checks if the game has ended.
     * @return true if there is a winner, false if there is no winner now
     */
    private boolean isGameOver() {
        return players.getGameWinner() != null;
    }

    /**
     * Handles a complete round of the game.
     */
    private void playRound() {
        // Initialize round
        resetGame();

        // Play turns until round ends
        while (!players.checkForRoundWinner() && deck.hasMoreCards()) {
            Player turn = players.getCurrentPlayer();
            if (turn.hasHandCards()) {
                executeTurn(turn);
            }
        }

        // Handle round end
        Player winner = determineRoundWinner();
        handleRoundWinner(winner);
    }

    /**
     * Determines the winner of the current round.
     * @return the Player who won this round
     */
    private Player determineRoundWinner() {
        if (players.checkForRoundWinner() && players.getRoundWinner() != null) {
            return players.getRoundWinner();
        } else {
            Player winner = players.compareUsedPiles();
            winner.addToken();
            return winner;
        }
    }

    /**
     * Handles the round winner announcement and token award.
     * @param winner the Player who won this round
     */
    private void handleRoundWinner(Player winner) {
        winner.addToken();
        System.out.println(winner.getName() + " has won this round!");
        players.print();
    }

    /**
     * Announces the game winner.
     */
    private void announceGameWinner() {
        Player gameWinner = players.getGameWinner();
        System.out.println(gameWinner + " has won the game and the heart of the princess!");
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
            int idx = Integer.parseInt(userInput.getCardIndex(turn));
            playCard(turn.playHandCard(idx), turn);
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
        user.discardCard(card);
        card.execute(userInput, user, players, deck);
    }
}
