package edu.cmu.f24qa.loveletter;

import java.util.ArrayList;
import java.util.Collections;
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
     * Returns the winner of the round.
     *
     * @return the round winner
     */
    public @NonNull List<Player> getRoundWinner() {
        // for (Player p : players) {
        // if (p.hasHandCards()) {
        // return p;
        // }
        // }
        // throw new IllegalStateException("No player with cards found");
        // return null;
        if (players == null || players.isEmpty()) {
            throw new IllegalStateException("No players in the game");
        }

        int highestCardValue = -1;
        List<Player> playersWithHighestCard = new ArrayList<>();

        // find the player with highest held card
        for (Player player : players) {
            if (player.hasHandCards()) {
                int cardValue = player.viewHandCard(0).getValue();
                if (cardValue > highestCardValue) {
                    highestCardValue = cardValue;
                    playersWithHighestCard.clear();
                    playersWithHighestCard.add(player);
                } else if (cardValue == highestCardValue) {
                    playersWithHighestCard.add(player);
                }
            }
        }

        if (playersWithHighestCard.isEmpty()) {
            throw new IllegalStateException("No player with cards found");
        }

        // rule 12.1: if there is only one player holding highest card
        if (playersWithHighestCard.size() == 1) {
            return new ArrayList<>(Collections.singletonList(playersWithHighestCard.get(0)));
        }

        // rule 12.2: If multiple players hold the highest card value,
        // the system shall compare the sum of their discarded card values
        int highestDiscardedSum = -1;
        List<Player> winners = new ArrayList<>();

        for (Player player : playersWithHighestCard) {
            int discardedSum = player.discardedValue();
            if (discardedSum > highestDiscardedSum) {
                highestDiscardedSum = discardedSum;
                winners.clear();
                winners.add(player);
            } else if (discardedSum == highestDiscardedSum) {
                winners.add(player);
            }
        }

        // if there is a tie, return all the winners
        return winners;
    }

    /**
     * Returns the winner of the game.
     *
     * @return the game winner
     */
    public @Nullable Player getGameWinner() {
        for (Player p : players) {
            if (p.getTokens() == 5) {
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
    public Player getFirstPlayerWithCards() {
        for (Player p : players) {
            if (p.hasHandCards()) {
                return p;
            }
        }
        return null;
    }

}
