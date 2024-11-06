package edu.cmu.f24qa.loveletter;

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
        Game g = new Game();
        g.setPlayers();
        g.start();
    }

}
