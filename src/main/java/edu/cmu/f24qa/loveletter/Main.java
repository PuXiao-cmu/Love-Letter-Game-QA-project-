package edu.cmu.f24qa.loveletter;

import java.util.Scanner;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public final class Main {

    private Main() {
        // Utility class.
    }

    /**
     * Main method that starts the Love Letter game.
     *
     * @param args command line arguments
     */
    @SuppressFBWarnings(
        value = "DM_DEFAULT_ENCODING",
        justification = "It's fine for console reads to rely on default encoding")
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        PlayerList players = new PlayerList();
        Deck deck = new Deck();
        GameState gameState = new GameState(players, deck);
        Game g = new Game(in, gameState);
        g.setPlayers();
        g.start();
    }

}
