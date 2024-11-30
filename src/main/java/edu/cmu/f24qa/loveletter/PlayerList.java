package edu.cmu.f24qa.loveletter;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PlayerList {

    private Deque<Player> players;

    /**
     * Initializes a PlayerList.
     */
    public PlayerList() {
        this.players = new LinkedList<>();
    }

    /**
     * Adds a new Player object with the given name to the PlayerList.
     *
     * @param name
     *             the given player name
     *
     * @return true if the player is not already in the list and can be added, false
     *         if not
     */
    public boolean addPlayer(String name) {
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(name)) {
                return false;
            }
        }
        players.addLast(new Player(name));
        return true;
    }

    /**
     * Gets the first player in the list and adds them to end of the list.
     *
     * @return the first player in the list
     */
    public Player getCurrentPlayer() {
        Player current = players.removeFirst();
        players.addLast(current);
        return current;
    }

    /**
     * Resets all players within the list.
     */
    public void reset() {
        for (Player p : players) {
            p.clearHand();
            p.clearDiscarded();
        }
    }

    /**
     * Prints the used pile of each Player in the list.
     */
    public void printUsedPiles() {
        for (Player p : players) {
            System.out.println("\n" + p.getName());
            p.printDiscarded();
        }
    }

    /**
     * Prints each Player in the list.
     */
    public void print() {
        System.out.println();
        for (Player p : players) {
            System.out.println(p);
        }
        System.out.println();
    }

    /**
     * Checks the list for a single Player with remaining cards.
     * 
     * @return true if there is a winner, false if not
     */
    public boolean checkForRoundWinner() {
        int count = 0;
        for (Player p : players) {
            if (p.hasHandCards()) {
                count++;
            }
        }
        return count == 1;
    }

    /**
     * Gets the winner(s) of the current round.
     * 
     * @return List of winning players based on card values and discarded cards
     * @throws IllegalStateException if no valid players exist in game
     */
    public @NonNull List<Player> getRoundWinner() {
        validateGameState();

        // Find players with highest card value
        List<Player> playersWithHighestCard = findPlayersWithHighestCard();

        // If only one player has highest card, return that player
        if (playersWithHighestCard.size() == 1) {
            return playersWithHighestCard;
        }

        // For tied players, compare discarded card values
        return findWinnersWithHighestDiscardedValue(playersWithHighestCard);
    }

    /**
     * Validates that game has active players.
     * 
     * @throws IllegalStateException if player list is null or empty
     */
    private void validateGameState() {
        if (players == null || players.isEmpty()) {
            throw new IllegalStateException("No players in the game");
        }
    }

    /**
     * Finds all players holding the highest value card.
     * 
     * @return List of players with highest card value
     * @throws IllegalStateException if no players have cards
     */
    private List<Player> findPlayersWithHighestCard() {
        int highestCardValue = -1;
        List<Player> result = new ArrayList<>();

        // Compare each player's card value
        for (Player player : players) {
            if (player.hasHandCards()) {
                int cardValue = player.viewHandCard(0).getValue();
                if (cardValue > highestCardValue) {
                    highestCardValue = cardValue;
                    result.clear();
                    result.add(player);
                } else if (cardValue == highestCardValue) {
                    result.add(player);
                }
            }
        }

        if (result.isEmpty()) {
            throw new IllegalStateException("No player with cards found");
        }
        return result;
    }

    /**
     * Determines winners among tied players by discarded card values.
     * 
     * @param candidates List of players tied for highest card value
     * @return List of winners with highest discarded value
     */
    private List<Player> findWinnersWithHighestDiscardedValue(List<Player> candidates) {
        int highestDiscardedSum = -1;
        List<Player> winners = new ArrayList<>();

        // Compare discarded card values
        for (Player player : candidates) {
            int discardedSum = player.discardedValue();
            if (discardedSum > highestDiscardedSum) {
                highestDiscardedSum = discardedSum;
                winners.clear();
                winners.add(player);
            } else if (discardedSum == highestDiscardedSum) {
                winners.add(player);
            }
        }

        return winners;
    }

    /**
     * Returns the winner of the game.
     *
     * @return the game winner
     */
    public @Nullable Player getGameWinner() {
        int playerCount = players.size();
        int tokensNeeded;

        // Set victory condition based on number of players
        if (playerCount == 2) {
            tokensNeeded = 7;
        } else if (playerCount == 3) {
            tokensNeeded = 5;
        } else if (playerCount == 4) {
            tokensNeeded = 4;
        } else {
            return null;
        }

        // Check if any player has reached the required number of tokens
        for (Player p : players) {
            if (p.getTokens() == tokensNeeded) {
                return p;
            }
        }
        // throw new IllegalStateException("No player has won the game yet");
        return null;
    }

    /**
     * Deals a card to each Player in the list.
     *
     * @param deck
     *             the deck of cards
     */
    public void dealCards(Deck deck) {
        for (Player p : players) {
            p.receiveHandCard(deck.draw());
        }
    }

    /**
     * Gets the player with the given name.
     *
     * @param name
     *             the name of the desired player
     *
     * @return the player with the given name or null if there is no such player
     */
    public @Nullable Player getPlayer(String name) {
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Returns the player with the highest used pile value.
     *
     * @return the player with the highest used pile value
     */
    public Player compareUsedPiles() {
        Player winner = players.getFirst();
        for (Player p : players) {
            if (p.discardedValue() > winner.discardedValue()) {
                winner = p;
            }
        }
        return winner;
    }

    /**
     * Returns the first player found who still has cards in hand.
     *
     * @return the first player with cards, or null if no such player exists
     */
    public @Nullable Player getFirstPlayerWithCards() {
        for (Player p : players) {
            if (p.hasHandCards()) {
                return p;
            }
        }
        return null;
    }
}
