package edu.cmu.f24qa.loveletter;

import java.util.Scanner;

/**
 * The main class for the Love Letter game.
 */
public final class Main {

    private Main() {
        // Utility class.
    }

    /**
     * Main method that starts the Love Letter game.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Game g = new Game(in);
        g.setPlayers();
        g.start();
    }

}
