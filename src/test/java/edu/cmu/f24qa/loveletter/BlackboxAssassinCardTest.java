package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Deque;
import java.util.Scanner;

import static org.mockito.Mockito.*;

class BlackboxAssassinCardTest {

    private CommandLineUserInput userInput;
    private Scanner mockScanner;
    private Player spyGuardPlayer;
    private Player spyAssassinPlayer;
    private Player spyOtherPlayer;
    private Deck mockDeck;
    private PlayerList playerList;
    private Card guardCard;
    private Card assassinCard;
    private Card kingCard;

    @BeforeEach
    void setup() throws Exception {
        mockDeck = mock(Deck.class);
        userInput = new CommandLineUserInput();

        // Mock Scanner
        mockScanner = mock(Scanner.class);

        // Inject Scanner into CommandLineUserInput
        Field scannerField = CommandLineUserInput.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(userInput, mockScanner);

        // Create players
        spyGuardPlayer = spy(new Player("GuardPlayer"));
        spyAssassinPlayer = spy(new Player("AssassinPlayer"));
        spyOtherPlayer = spy(new Player("OtherPlayer"));

        // Initialize PlayerList
        playerList = new PlayerList();

        // Use reflection to modify the internal Deque<Player> players
        Field playersField = PlayerList.class.getDeclaredField("players");
        playersField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Deque<Player> players = (Deque<Player>) playersField.get(playerList);
        players.add(spyGuardPlayer);
        players.add(spyAssassinPlayer);
        players.add(spyOtherPlayer);

        // Set up cards
        guardCard = Card.GUARD;
        assassinCard = Card.ASSASSIN;
        kingCard = Card.KING;

        // Give players their cards
        spyGuardPlayer.receiveHandCard(guardCard);
        spyAssassinPlayer.receiveHandCard(assassinCard);
        spyOtherPlayer.receiveHandCard(kingCard);
    }

    /**
     * Rule 1:
     * Tests that when the Guard targets the Assassin and the deck is not empty,
     * the Assassin player discards and redraws a card, and the Guard is eliminated.
     */
    @Test
    void testGuardTargetsAssassinWithDeckNotEmpty() {
        // Mock input for target
        when(mockScanner.nextLine()).thenReturn("AssassinPlayer").thenReturn("6");

        // Mock deck has more cards
        when(mockDeck.hasMoreCards()).thenReturn(true);
        when(mockDeck.draw()).thenReturn(Card.KING);

        // Execute Guard card
        guardCard.execute(userInput, spyGuardPlayer, playerList, mockDeck);

        // Verify the Guard player is eliminated
        verify(spyGuardPlayer, times(1)).eliminate();

        // Verify the Assassin player discards and redraws
        verify(spyAssassinPlayer, times(1)).discardCard(assassinCard);
        verify(spyAssassinPlayer, times(1)).receiveHandCard(Card.KING);
    }

    /**
     * Rule 2:
     * Tests that when the Guard targets the Assassin and the deck is empty but has a hidden card,
     * the Assassin player discards and redraws the hidden card, and the Guard is eliminated.
     */
    @Test
    void testGuardTargetsAssassinWithEmptyDeck() {
        // Mock input for target
        when(mockScanner.nextLine()).thenReturn("AssassinPlayer").thenReturn("6");

        // Mock deck is empty but has a hidden card
        when(mockDeck.hasMoreCards()).thenReturn(false);
        when(mockDeck.hasHiddenCard()).thenReturn(true);
        when(mockDeck.useHiddenCard()).thenReturn(Card.PRINCE);

        // Execute Guard card
        guardCard.execute(userInput, spyGuardPlayer, playerList, mockDeck);

        // Verify the Guard player is eliminated
        verify(spyGuardPlayer, times(1)).eliminate();

        // Verify the Assassin player discards and redraws the hidden card
        verify(spyAssassinPlayer, times(1)).discardCard(assassinCard);
        verify(spyAssassinPlayer, times(1)).receiveHandCard(Card.PRINCE);
    }

    /**
     * Rule 3:
     * Tests that when the Guard targets the Assassin and the deck is empty with no hidden card,
     * the Assassin player discards and does not redraw a card, and the Guard is eliminated.
     */
    @Test
    void testGuardTargetsAssassinWithNoRedraw() {
        // Mock input for target
        when(mockScanner.nextLine()).thenReturn("AssassinPlayer").thenReturn("6");

        // Mock deck is empty and no hidden card
        when(mockDeck.hasMoreCards()).thenReturn(false);
        when(mockDeck.hasHiddenCard()).thenReturn(false);

        // Execute Guard card
        guardCard.execute(userInput, spyGuardPlayer, playerList, mockDeck);

        // Verify the Guard player is eliminated
        verify(spyGuardPlayer, times(1)).eliminate();

        // Verify the Assassin player discards without drawing a card
        verify(spyAssassinPlayer, times(1)).discardCard(assassinCard);
    }

    /**
     * Rule 4:
     * Tests that when the Guard targets another player instead of the Assassin,
     * the Assassin card's effect does not trigger and the Assassin card remains in the player's hand.
     */
    @Test
    void testAssassinEffectDoesNotTriggerWithoutGuardTarget() {
        // Mock input for target other than Assassin
        when(mockScanner.nextLine()).thenReturn("OtherPlayer").thenReturn("6");

        // Execute Guard card
        guardCard.execute(userInput, spyAssassinPlayer, playerList, mockDeck);

        // Verify only the other player is eliminated
        verify(spyGuardPlayer, never()).eliminate();
        verify(spyAssassinPlayer, never()).eliminate();
        verify(spyOtherPlayer, times(1)).eliminate();

        // Verify the Assassin card remains
        verify(spyAssassinPlayer, never()).discardCard(assassinCard);
    }
}
