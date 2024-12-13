package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Deque;
import java.util.Scanner;

import static org.mockito.Mockito.*;

class BlackboxCardinalCardTest {

    private CommandLineUserInput userInput;
    private Scanner mockScanner;
    private Player spyPlayer;
    private Player spyProtectedPlayer;
    private Player spyPlayer1;
    private Player spyPlayer2;
    private PlayerList playerList;
    private Deck deck;
    private Card cardinalCard;

    /**
     * Sets up the test environment before each test.
     * Initializes the deck, sets the Cardinal card, and sets up the mock scanner.
     * Creates spy players and initializes the player list with the spy players.
     * Uses reflection to modify the internal Deque<Player> players of the PlayerList.
     * Adds the spy players to the PlayerList.
     * @throws Exception if there is an error setting up the test environment
     */
    @BeforeEach
    void setup() throws Exception {
        deck = new Deck();
        deck.build();
        deck.shuffle();
        userInput = new CommandLineUserInput();

        // Set Card
        cardinalCard = Card.CARDINAL;

        // Mock Scanner
        mockScanner = mock(Scanner.class);

        // Use reflection to inject the mockScanner into CommandLineUserInput
        Field scannerField = CommandLineUserInput.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(userInput, mockScanner);

        // Mock Players
        spyPlayer = spy(new Player("Player"));
        spyProtectedPlayer = spy(new Player("ProtectedPlayer"));
        spyPlayer1 = spy(new Player("Player1"));
        spyPlayer2 = spy(new Player("Player2"));
        spyProtectedPlayer.switchProtection();

        // Initialize PlayerList
        playerList = new PlayerList();

        // Use reflection to modify the internal Deque<Player> players
        Field playersField = PlayerList.class.getDeclaredField("players");
        playersField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Deque<Player> players = (Deque<Player>) playersField.get(playerList);

        // Add spy players to the PlayerList
        players.add(spyPlayer);
        players.add(spyProtectedPlayer);
        players.add(spyPlayer1);
        players.add(spyPlayer2);
    }

    /**
     * Rule 1:
     * Tests the Cardinal card action when valid players are selected.
     * Verifies that the selected players swap cards and the cards are revealed.
     * Ensures that the swap and reveal actions are performed correctly by the players.
     */
    @Test
    void testRule1_ValidSelectionsSwapCardsAndReveal() {
        // Setup valid selections
        doReturn("Player1", "Player2").when(mockScanner).nextLine();

        // Mock hand cards
        spyPlayer1.receiveHandCard(Card.PRINCE);
        spyPlayer2.receiveHandCard(Card.BARON);

        // Execute Cardinal action
        cardinalCard.execute(userInput, spyPlayer, playerList, deck);

        // Verify swaps and reveal
        verify(spyPlayer1).playHandCard(0);
        verify(spyPlayer2).playHandCard(0);
        verify(spyPlayer1).receiveHandCard(Card.BARON);
        verify(spyPlayer2).receiveHandCard(Card.PRINCE);
        verify(spyPlayer1).viewHandCard(0);
    }

    /**
     * Rule 2:
     * Tests the retry logic when the same player is selected twice for the Cardinal card action.
     * Verifies that the system correctly prompts the user to reselect until valid different players
     * are chosen. Ensures that the card swap and reveal actions are performed correctly once valid
     * selections are made.
     */
    @Test
    void testRule2_RetrySelectionForSamePlayer() {
        // Setup invalid selection of the same player
        doReturn("Player1", "Player1", "Player2").when(mockScanner).nextLine();

        // Mock hand cards
        spyPlayer1.receiveHandCard(Card.PRINCE);
        spyPlayer2.receiveHandCard(Card.BARON);

        // Execute Cardinal action
        cardinalCard.execute(userInput, spyPlayer, playerList, deck);

        // Verify retries
        verify(mockScanner, times(3)).nextLine();
        verify(spyPlayer1).playHandCard(0);
        verify(spyPlayer2).playHandCard(0);
        verify(spyPlayer1).receiveHandCard(Card.BARON);
        verify(spyPlayer2).receiveHandCard(Card.PRINCE);
        verify(spyPlayer1).viewHandCard(0);
    }

    /**
     * Rule 3:
     * Tests the retry logic when the first player selected is the protected player for the Cardinal
     * card action. Verifies that the system correctly prompts the user to reselect until valid
     * different players are chosen. Ensures that the card swap and reveal actions are performed
     * correctly once valid selections are made.
     */
    @Test
    void testRule3_RetrySelectionForInvalidPlayer1() {
        // Setup invalid first player selection
        doReturn("ProtectedPlayer", "Player1", "Player2").when(mockScanner).nextLine();

        // Mock hand cards

        spyPlayer1.receiveHandCard(Card.PRINCE);
        spyPlayer2.receiveHandCard(Card.BARON);

        // Execute Cardinal action
        cardinalCard.execute(userInput, spyPlayer, playerList, deck);

        // Verify retries and swaps
        verify(mockScanner, times(3)).nextLine();
        verify(spyPlayer1).playHandCard(0);
        verify(spyPlayer2).playHandCard(0);
        verify(spyPlayer1).receiveHandCard(Card.BARON);
        verify(spyPlayer2).receiveHandCard(Card.PRINCE);
        verify(spyPlayer1).viewHandCard(0);
    }

    /**
     * Rule 4:
     * Tests the retry logic when the second player selected is the protected player for the Cardinal
     * card action. Verifies that the system correctly prompts the user to reselect until valid
     * different players are chosen. Ensures that the card swap and reveal actions are performed
     * correctly once valid selections are made.
     */
    @Test
    void testRule4_RetrySelectionForInvalidPlayer2() {
        // Setup invalid second player selection
        doReturn("Player1", "ProtectedPlayer", "Player2").when(mockScanner).nextLine();

        // Mock hand cards
        spyPlayer1.receiveHandCard(Card.PRINCE);
        spyPlayer2.receiveHandCard(Card.BARON);

        // Execute Cardinal action
        cardinalCard.execute(userInput, spyPlayer, playerList, deck);

        // Verify retries and swaps
        verify(mockScanner, times(3)).nextLine();
        verify(spyPlayer1).playHandCard(0);
        verify(spyPlayer2).playHandCard(0);
        verify(spyPlayer1).receiveHandCard(Card.BARON);
        verify(spyPlayer2).receiveHandCard(Card.PRINCE);
        verify(spyPlayer1).viewHandCard(0);
    }

    /**
     * Rule 5:
     * Tests that when there are insufficient valid players in the PlayerList, the Cardinal card action
     * does not perform any swaps or reveals. Verifies that no card swap or reveal actions are performed
     * when not enough valid players are present.
     */
    @Test
    void testRule5_InsufficientValidPlayers() throws Exception {
        // Use reflection to access the internal Deque<Player>
        Field playersField = PlayerList.class.getDeclaredField("players");
        playersField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Deque<Player> players = (Deque<Player>) playersField.get(playerList);

        // Clear existing players and re-add only Player and ProtectedPlayer
        players.clear();
        players.add(spyPlayer);
        players.add(spyProtectedPlayer);

        // Setup selections
        doReturn("Player1", "Player2").when(mockScanner).nextLine();

        // Mock hand cards
        spyPlayer1.receiveHandCard(Card.PRINCE);
        spyPlayer2.receiveHandCard(Card.BARON);

        // Execute Cardinal action
        cardinalCard.execute(userInput, spyPlayer, playerList, deck);

        // Verify no swaps or reveals occurred
        verify(spyPlayer1, never()).playHandCard(0);
        verify(spyPlayer2, never()).playHandCard(0);
        verify(spyPlayer1, never()).viewHandCard(0);
        verify(mockScanner, never()).nextLine();
    }
}
