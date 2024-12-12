package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DowagerQueenActionTest {
    private UserInput mockUserInput;
    private Player mockUser;
    private Player mockOpponent;
    private PlayerList mockPlayerList;
    private Deck deck;
    private DowagerQueenAction dowagerQueenAction;

    @BeforeEach
    void setUp() {
        mockUserInput = mock(UserInput.class);
        mockUser = spy(new Player("User"));
        mockOpponent = spy(new Player("Opponent"));
        mockPlayerList = mock(PlayerList.class);
        deck = new Deck();
        deck.build();
        deck.shuffle();
        dowagerQueenAction = new DowagerQueenAction();
        
        when(mockUserInput.getOpponent(any(PlayerList.class), any(Player.class)))
            .thenReturn(mockOpponent);
    }

    /**
     * Test ID: DowagerQueenT1(Rule 1)
     * Branch ID: DowagerQueen-W1
     * Test when user has higher card value (should be eliminated)
     */
    @Test
    void testUserHigherNumberGetsEliminated() {
        mockUser.receiveHandCard(Card.KING);      // value 6
        mockOpponent.receiveHandCard(Card.GUARD);  // value 1

        dowagerQueenAction.execute(mockUserInput, mockUser, mockPlayerList, deck);

        verify(mockUser).eliminate();
        verify(mockOpponent, never()).eliminate();
    }

    /**
     * Test ID: DowagerQueenT2(Rule 2)
     * Branch ID: DowagerQueen-W2
     * Test when opponent has higher card value (should be eliminated)
     */
    @Test
    void testOpponentHigherNumberGetsEliminated() {
        mockUser.receiveHandCard(Card.GUARD);     // value 1
        mockOpponent.receiveHandCard(Card.KING);  // value 6

        dowagerQueenAction.execute(mockUserInput, mockUser, mockPlayerList, deck);

        verify(mockOpponent).eliminate();
        verify(mockUser, never()).eliminate();
    }

    /**
     * Test ID: DowagerQueenT3(Rule 3)
     * Branch ID: DowagerQueen-W3
     * Test when both players have equal card values
     */
    @Test
    void testEqualNumbersNoElimination() {
        mockUser.receiveHandCard(Card.PRIEST);     // value 2
        mockOpponent.receiveHandCard(Card.PRIEST); // value 2

        dowagerQueenAction.execute(mockUserInput, mockUser, mockPlayerList, deck);

        verify(mockUser, never()).eliminate();
        verify(mockOpponent, never()).eliminate();
    }
}
