package edu.cmu.f24qa.loveletter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WhiteboxBishopCardTest {
    private UserInput mockUserInput;
    private Player mockUser;
    private Player mockOpponent;
    private PlayerList mockPlayerList;
    private BishopAction bishopAction;
    private Deck deck;

    @BeforeEach
    void setUp() {
        mockUserInput = mock(UserInput.class);
        mockUser = mock(Player.class);
        mockOpponent = mock(Player.class);
        mockPlayerList = mock(PlayerList.class);
        bishopAction = new BishopAction();
        deck = new Deck();
        deck.build();
        deck.shuffle();

        when(mockUserInput.getOpponent(any(PlayerList.class), any(Player.class))).thenReturn(mockOpponent);
        when(mockOpponent.getName()).thenReturn("Opponent");

        when(mockUserInput.getOpponent(any(PlayerList.class), any(Player.class))).thenReturn(mockOpponent);
        when(mockOpponent.getName()).thenReturn("Opponent");
        when(mockOpponent.viewHandCard(0)).thenReturn(Card.PRIEST);
        when(mockOpponent.hasHandCards()).thenReturn(true);
    }

    /**
     * BishopT1: Correct guess with normal card(Bishop-W1)
     */
    @Test
    void testBishopT1_CorrectGuessWithNormalCard() {
        Card priestCard = Card.PRIEST;
        when(mockUserInput.getCardNumber()).thenReturn(2);
        when(mockOpponent.viewHandCard(0)).thenReturn(priestCard);
        when(mockOpponent.playHandCard(0)).thenReturn(priestCard);

        bishopAction.execute(mockUserInput, mockUser, mockPlayerList, deck);

        verify(mockUserInput).getOpponent(any(PlayerList.class), any(Player.class));
        verify(mockUserInput).getCardNumber();
        verify(mockOpponent).viewHandCard(0);
        verify(mockUser).addToken();
        verify(mockOpponent).playHandCard(0);
        verify(mockOpponent).discardCard(priestCard);
        verify(mockOpponent).receiveHandCard(any(Card.class));
    }

    /**
     * BishopT2: Correct guess with Princess(Bishop-W2)
     */
    @Test
    void testBishopT2_CorrectGuessWithPrincess() {
        when(mockUserInput.getCardNumber()).thenReturn(8);
        when(mockOpponent.viewHandCard(0)).thenReturn(Card.PRINCESS);

        bishopAction.execute(mockUserInput, mockUser, mockPlayerList, deck);

        verify(mockUser).addToken();
        verify(mockOpponent, never()).discardCard(any(Card.class));
        verify(mockOpponent, never()).receiveHandCard(any(Card.class));
    }

    /**
     * BishopT3: Incorrect guess(Bishop-W3)
     */
    @Test
    void testBishopT3_IncorrectGuess() {
        when(mockUserInput.getCardNumber()).thenReturn(2);
        when(mockOpponent.viewHandCard(0)).thenReturn(Card.BARON);

        bishopAction.execute(mockUserInput, mockUser, mockPlayerList, deck);

        verify(mockUser, never()).addToken();
        verify(mockOpponent, never()).discardCard(any(Card.class));
        verify(mockOpponent, never()).receiveHandCard(any(Card.class));
    }

    /**
     * BishopT4: Correct guess leads to immediate win(Bishop-W4)
     */
    @Test 
    void testBishopT4_CorrectGuessLeadsToWin() {
        Card priestCard = Card.PRIEST;
        when(mockUserInput.getCardNumber()).thenReturn(2);
        when(mockOpponent.viewHandCard(0)).thenReturn(priestCard);
        when(mockOpponent.playHandCard(0)).thenReturn(priestCard);

        List<Player> winners = Collections.singletonList(mockUser);
        when(mockPlayerList.getGameWinnerCandidates()).thenReturn(winners);

        bishopAction.execute(mockUserInput, mockUser, mockPlayerList, deck);

        verify(mockUserInput).getOpponent(any(PlayerList.class), any(Player.class));
        verify(mockUserInput).getCardNumber();
        verify(mockOpponent).viewHandCard(0);
        verify(mockUser).addToken();
        verify(mockPlayerList).getGameWinnerCandidates();

        verify(mockOpponent, never()).playHandCard(anyInt());
        verify(mockOpponent, never()).discardCard(any(Card.class));
        verify(mockOpponent, never()).receiveHandCard(any(Card.class));
    }

    /**
     * BishopT5: Correct guess when deck is empty(Bishop-W5)
     */
    @Test
    void testBishopT5_CorrectGuessEmptyDeck() {
        Card priestCard = Card.PRIEST;
        when(mockUserInput.getCardNumber()).thenReturn(2);
        when(mockOpponent.viewHandCard(0)).thenReturn(priestCard);
        when(mockOpponent.playHandCard(0)).thenReturn(priestCard);

        // Mock empty deck
        Deck emptyDeck = mock(Deck.class);
        when(emptyDeck.hasMoreCards()).thenReturn(false);

        bishopAction.execute(mockUserInput, mockUser, mockPlayerList, emptyDeck);

        verify(mockUserInput).getOpponent(any(PlayerList.class), any(Player.class));
        verify(mockUserInput).getCardNumber();
        verify(mockOpponent).viewHandCard(0);
        verify(mockUser).addToken();
        verify(mockOpponent).playHandCard(0);
        verify(mockOpponent).discardCard(priestCard);

        verify(emptyDeck).hasMoreCards();

        verify(mockOpponent, never()).receiveHandCard(any(Card.class));
    }
}
