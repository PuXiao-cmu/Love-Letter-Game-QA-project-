package edu.cmu.f24qa.loveletter;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class GameState {
    @SuppressFBWarnings(value = "URF_UNREAD_FIELD", justification = "Might be used later")
    private int round;
    @SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"}, justification = "It's fine for console reads")
    private PlayerList players;
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "It's fine for console reads")
    private Deck deck;

    /**
     * Constructor for the GameState class.
     *
     * @param curPlayers
     * @param curDeck
     */
    public GameState(PlayerList curPlayers, Deck curDeck) {
        this.round = 0;
        this.players = curPlayers;
        this.deck = curDeck;
    }

    /**
     * @return current round
     */
    public int getRound() {
        return round;
    }

    /**
     * Go to next round.
     */
    public void incrementRound() {
        round++;
    }

    /**
     * @return current players
     */
    public PlayerList getPlayers() {
        return players;
    }

    /**
     * @return current deck
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Reset players, rebuild deck and shuffle.
     */
    public void resetGameState() {
        players.reset();
        deck.build();
        deck.shuffle();
    }
}

