package edu.cmu.f24qa.loveletter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import edu.umd.cs.findbugs.annotations.Nullable;
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
    private List<Player> lastRoundWinners;

    /**
     * Constructor for the Game class.
     *
     * @param curUserInput an object used to handle user input
     * @param curPlayers   the current players
     * @param curDeck      the current deck
     */
    public Game(UserInput curUserInput, PlayerList curPlayers, Deck curDeck) {
        this.userInput = curUserInput;
        this.players = curPlayers;
        this.deck = curDeck;
        this.round = 0;
        this.lastRoundWinners = new ArrayList<>();
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
        int numPlayers = players.numPlayer();
        if (numPlayers >= 2 && numPlayers <= 4) {
            deck.build();
        } else if (numPlayers >= 5 && numPlayers <= 8) {
            deck.buildPremium();
        }
        deck.shuffle();
        deck.hideTopCard();

        if (numPlayers == 2) {
            deck.removeAnotherThreeCards();
        }

        players.dealCards(deck);
    }

    /**
     * The main game loop.
     */
    public void start() {
        while (!isGameOver()) {
            playRound();
        }
        Player finalWinner = getFinalGameWinner();
        if (finalWinner != null) {
            System.out.println(finalWinner + " has won the game and the heart of the princess!");
        }
    }

    /**
     * Checks if the game has ended.
     *
     * @return true if there is a winner, false if there is no winner now
     */
    private boolean isGameOver() {
        return !players.getGameWinnerCandidates().isEmpty();
    }

    /**
     * Handles a complete round of the game.
     */
    protected void playRound() {
        // Initialize round
        resetGame();

        // Set starting player based on last round's winners
        if (lastRoundWinners != null && !lastRoundWinners.isEmpty()) {
            if (lastRoundWinners.size() == 1) {
                // If only one winner, start with that player
                players.setStartingPlayer(lastRoundWinners.get(0));
            } else {
                // If multiple winners, find the one who joined first
                Player startingPlayer = players.findEarliestAddedPlayer(lastRoundWinners);
                players.setStartingPlayer(startingPlayer);
            }
        }

        // Play turns until round ends
        while (!players.checkForRoundWinner() && deck.hasMoreCards()) {
            Player turn = players.getCurrentPlayer();
            if (turn.hasHandCards()) {
                executeTurn(turn);
            }
        }

        // Handle round end
        List<Player> winners = determineRoundWinner();
        handleRoundWinner(winners);
    }

    /**
     * Determines the winner of the current round.
     *
     * @return the Player who won this round
     */
    protected List<Player> determineRoundWinner() {
        if (players.checkForRoundWinner()) {
            // case 1: only one player left
            Player winner = players.getFirstPlayerWithCards();
            if (winner != null) {
                return Collections.singletonList(winner);
            }
            throw new IllegalStateException("No player with cards found");
        } else {
            // case 2: multiple players, so we need to check discard cards
            return players.getRoundWinner();
        }
    }

    /**
     * Handles the round winner announcement and token award.
     *
     * @param winners the List of Players who won this round
     */
    protected void handleRoundWinner(List<Player> winners) {
        lastRoundWinners.clear(); // clear last round winners
        List<Player> correctPredictors = new ArrayList<>(); // Collect all predictors who guessed correctly
        for (Player winner : winners) {
            winner.addToken();
            lastRoundWinners.add(winner); // add new winners
            Player predictor = winner.getJesterPredictor();
            if (predictor != null && !correctPredictors.contains(predictor)) {
                correctPredictors.add(predictor);
            }
        }

        if (winners.size() == 1) {
            System.out.println(winners.get(0).getName() + " has won this round!");
        } else {
            System.out.println("This round ended in a tie! Winners: "
                    + winners.stream().map(Player::getName).collect(Collectors.joining(", ")));
        }

        for (Player predictor : correctPredictors) {
            predictor.addToken();
            System.out.println(predictor.getName() + " correctly predicted a winner and earns a Token!");
        }

        players.print();
    }

    /**
     * Get the final game winner.
     *
     * @return the final game winner
     */
    protected @Nullable Player getFinalGameWinner() {
        List<Player> gameWinners = players.getGameWinnerCandidates();
        while (gameWinners.size() > 1) {
            System.out.println("Tie detected! Players involved in the tie: "
                    + gameWinners.stream().map(Player::getName).collect(Collectors.joining(", ")));
            System.out.println("Playing a tie-breaking round...");
            players.setActivePlayers(gameWinners);
            playRound();
            gameWinners = determineRoundWinner();
        }
        if (!gameWinners.isEmpty()) {
            return gameWinners.get(0);
        }
        return null;
    }

    /**
     * Executes a player's turn in the game.
     *
     * @param turn
     *             the player whose turn it is
     */
    @SuppressFBWarnings(value = "NP_NONNULL_PARAM_VIOLATION", justification = "Passing null is valid.")
    protected void executeTurn(Player turn) {
        players.printUsedPiles();
        System.out.println("\n" + turn.getName() + "'s turn:");
        if (turn.isProtected()) {
            turn.switchProtection();
        }

        turn.receiveHandCard(deck.draw());

        int royaltyPos = turn.handRoyaltyPos();
        
        int cardToPlay;
        if (royaltyPos == 0 && turn.viewHandCard(1).getName().equalsIgnoreCase("countess")) {
            // playCard(turn.playHandCard(1), turn);
            cardToPlay = 1;
        } else if (royaltyPos == 1 && turn.viewHandCard(1).getName().equalsIgnoreCase("countess")) {
            // playCard(turn.playHandCard(0), turn);
            cardToPlay = 0;
        } else {
            cardToPlay = Integer.parseInt(userInput.getCardIndex(turn));
            // playCard(turn.playHandCard(idx), turn);
        }

        Set<String> cardsSelectPlayer = new HashSet<>() {{
            add("King");
            add("Prince");
            add("Baron");
            add("Priest");
            add("Guard");
            add("Bishop");
            add("DowagerQueen");
            add("Sycophant");
            add("Baroness");
            add("Cardinal");
            add("Jester");
        }};

        if (!cardsSelectPlayer.contains(turn.viewHandCard(cardToPlay).getName())) {
            userInput.setSycophantChoice(null);
        }

        playCard(turn.playHandCard(cardToPlay), turn);        
    }

    /**
     * Plays a card from the user's hand.
     *
     * @param card
     *             the played card
     * @param user
     *             the player of the card
     */
    private void playCard(Card card, Player user) {
        user.discardCard(card);
        card.execute(userInput, user, players, deck);
    }
}
