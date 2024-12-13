package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class WhiteboxDowagerQueenTest {
    private UserInput mockUserInput;
    private Player mockUser;
    private Player mockOpponent;
    private PlayerList mockPlayerList;
    private Deck deck;
    private DowagerQueenAction dowagerQueenAction;

    @BeforeEach
    void setUp() {
        mockUserInput = mock(UserInput.class);
        mockPlayerList = mock(PlayerList.class);
        
        mockUser = spy(new Player("User"));
        mockOpponent = spy(new Player("Opponent"));
        Player player3 = new Player("Player3");
        Player player4 = new Player("Player4");
        Player player5 = new Player("Player5");
        
        when(mockPlayerList.numPlayer()).thenReturn(5);
        
        deck = new Deck();
        deck.buildPremium();
        deck.shuffle();
        dowagerQueenAction = new DowagerQueenAction();
        
        when(mockUserInput.getOpponent(any(PlayerList.class), any(Player.class)))
            .thenReturn(mockOpponent);
    }

    /**
     * Test ID: DowagerQueenT1
     * Branch ID: DowagerQueen-W1
     * Test when user has higher card value (should be eliminated)
     */
    @Test
    void testUserHigherNumberGetsEliminated() {
        // Capture System.out
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        
        try {
            mockUser.receiveHandCard(Card.KING);      // value 6
            mockOpponent.receiveHandCard(Card.GUARD);  // value 1

            dowagerQueenAction.execute(mockUserInput, mockUser, mockPlayerList, deck);

            verify(mockUser).eliminate();
            verify(mockOpponent, never()).eliminate();
            assertTrue(outContent.toString().contains("You have been eliminated!"));
        } finally {
            System.setOut(originalOut);
        }
    }

    /**
     * Test ID: DowagerQueenT2
     * Branch ID: DowagerQueen-W2
     * Test when opponent has higher card value (should be eliminated)
     */
    @Test
    void testOpponentHigherNumberGetsEliminated() {
        // Capture System.out
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        
        try {
            mockUser.receiveHandCard(Card.GUARD);     // value 1
            mockOpponent.receiveHandCard(Card.KING);  // value 6

            dowagerQueenAction.execute(mockUserInput, mockUser, mockPlayerList, deck);

            verify(mockOpponent).eliminate();
            verify(mockUser, never()).eliminate();
            assertTrue(outContent.toString().contains("has been eliminated!"));
        } finally {
            System.setOut(originalOut);
        }
    }

    /**
     * Test ID: DowagerQueenT3
     * Branch ID: DowagerQueen-W3
     * Test when both players have equal card values
     */
    @Test
    void testEqualNumbersNoElimination() {
        // Capture System.out
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        
        try {
            mockUser.receiveHandCard(Card.PRIEST);     // value 2
            mockOpponent.receiveHandCard(Card.PRIEST); // value 2

            dowagerQueenAction.execute(mockUserInput, mockUser, mockPlayerList, deck);

            verify(mockUser, never()).eliminate();
            verify(mockOpponent, never()).eliminate();
            assertTrue(outContent.toString().contains("It's a tie! No one is eliminated."));
        } finally {
            System.setOut(originalOut);
        }
    }
}
