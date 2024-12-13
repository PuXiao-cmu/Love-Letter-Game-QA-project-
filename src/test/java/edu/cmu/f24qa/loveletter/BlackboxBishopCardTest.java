package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;

import java.lang.reflect.Field;
import java.util.Deque;

class BlackboxBishopCardTest {

    private UserInput mockUserInput;
    private Player spyPlayer;
    private Player spyOpponent;
    private Player spyProtectedOpponent;
    private PlayerList playerList;
    private Deck deck;
    private Card bishopCard;

    @BeforeEach
    void setup() throws Exception {
        deck = new Deck();
        deck.build();
        deck.shuffle();
        
        mockUserInput = mock(UserInput.class);

        spyPlayer = new Player("Player");
        spyOpponent = new Player("Opponent");
        spyProtectedOpponent = new Player("ProtectedOpponent");

        playerList = new PlayerList();

        Field playersField = PlayerList.class.getDeclaredField("players");
        playersField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Deque<Player> players = (Deque<Player>) playersField.get(playerList);
        players.add(spyPlayer);
        players.add(spyOpponent);
        players.add(spyProtectedOpponent);

        bishopCard = Card.BISHOP;

        spyProtectedOpponent.switchProtection();

        when(mockUserInput.getOpponent(any(PlayerList.class), any(Player.class))).thenReturn(spyOpponent);
        when(mockUserInput.getOpponent(any(PlayerList.class), any(Player.class), anyBoolean())).thenReturn(spyOpponent);
        when(mockUserInput.getOpponent(any(PlayerList.class), any(Player.class), anyBoolean(), anyBoolean())).thenReturn(spyOpponent);
    }

    /**
     * Rule 1: Valid opponent selection and correct guess on normal card
     */
    @Test
    void testValidOpponentAndCorrectGuess() {
        int initialTokens = spyPlayer.getTokens();
        spyOpponent.receiveHandCard(Card.PRIEST); // Value 2

        when(mockUserInput.getCardNumber()).thenReturn(2); // Guess value 2 (Priest)

        bishopCard.execute(mockUserInput, spyPlayer, playerList, deck);

        assertEquals(initialTokens + 1, spyPlayer.getTokens(), "Player should gain a token");
        assertTrue(deck.hasMoreCards() || deck.hasHiddenCard(), "Deck should have cards for redraw");
    }

    /**
     * Rule 2: Valid opponent selection and correct guess on Princess
     */
    @Test
    void testValidOpponentAndCorrectGuessPrincess() {
        int initialTokens = spyPlayer.getTokens();
        spyOpponent.receiveHandCard(Card.PRINCESS); // Value 8

        when(mockUserInput.getCardNumber()).thenReturn(8); // Guess value 8 (Princess)

        bishopCard.execute(mockUserInput, spyPlayer, playerList, deck);

        assertEquals(initialTokens + 1, spyPlayer.getTokens(), "Player should gain a token");
        assertTrue(spyOpponent.hasHandCards(), "Opponent should keep Princess card");
    }

    /**
     * Rule 3: Valid opponent selection but incorrect guess
     */
    @Test
    void testValidOpponentButIncorrectGuess() {
        int initialTokens = spyPlayer.getTokens();
        spyOpponent.receiveHandCard(Card.BARON); // Value 3

        when(mockUserInput.getCardNumber()).thenReturn(2); // Wrong guess

        bishopCard.execute(mockUserInput, spyPlayer, playerList, deck);

        assertEquals(initialTokens, spyPlayer.getTokens(), "Player should not gain token");
        assertTrue(spyOpponent.hasHandCards(), "Opponent should keep their card");
    }

    /**
     * Rule 4: Select protected opponent(Verified in getOpponent)
     */

    /**
     * Rule 5: Select non-existent opponent(Verified in getOpponent)
     */

    /**
     * Rule 6: Correct guess with empty deck
     */
    @Test
    void testCorrectGuessWithEmptyDeck() {
        int initialTokens = spyPlayer.getTokens();
        spyOpponent.receiveHandCard(Card.PRIEST);

        // Setup empty deck
        Deck emptyDeck = mock(Deck.class);
        when(emptyDeck.hasMoreCards()).thenReturn(false);
        when(emptyDeck.hasHiddenCard()).thenReturn(false);

        when(mockUserInput.getCardNumber()).thenReturn(2);

        bishopCard.execute(mockUserInput, spyPlayer, playerList, emptyDeck);

        assertEquals(initialTokens + 1, spyPlayer.getTokens(), "Player should gain token");
        assertFalse(spyOpponent.hasHandCards(), "Opponent should have no cards after discard");
    }

    /**
     * Rule 7: Correct guess with one token away from winning
     */
    @Test
    void testCorrectGuessLeadsToGameWin() {
        for (int i = 0; i < 4; i++) {
            spyPlayer.addToken();
        }

        spyOpponent.receiveHandCard(Card.PRIEST);
        int initialTokens = spyPlayer.getTokens();

        when(mockUserInput.getCardNumber()).thenReturn(2); // Correctly guess Priest

        bishopCard.execute(mockUserInput, spyPlayer, playerList, deck);

        assertEquals(initialTokens + 1, spyPlayer.getTokens(), "Player should win with 5 tokens");
        assertTrue(deck.hasMoreCards() || deck.hasHiddenCard(), "Deck should have cards for redraw");
    }
}