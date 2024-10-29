package edu.cmu.f24qa.loveletter;

import java.util.Scanner;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class Main {

    @SuppressFBWarnings(value = "DM_DEFAULT_ENCODING", justification = "It's fine for console reads to rely on default encoding")
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Game g = new Game(in);
        g.setPlayers();
        g.start();
    }

}
