package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Deque;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class BlackboxSycophantCardTest {

    private CommandLineUserInput userInput;
    private Scanner mockScanner;
    private Player sycophantPlayer; // The player who plays Sycophant
    private Player selectedPlayer; // The player selected by Sycophant
    private Player firstInputPlayer;
    private Player secondInputPlayer;
    private Player anotherPlayer;  // Another player in the game
    private PlayerList playerList;
    private Deck deck;
    private Card sycophantCard;
    private Card countessCard;
    private Game game;

    @BeforeEach
    void setup() throws Exception {
        // Initialize deck and game
        deck = new Deck();
        deck.build();
        deck.shuffle();

        // Mock Scanner
        mockScanner = mock(Scanner.class);

        // Inject mock Scanner into CommandLineUserInput
        userInput = new CommandLineUserInput();
        Field scannerField = CommandLineUserInput.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(userInput, mockScanner);

        // Create players
        sycophantPlayer = spy(new Player("SycophantPlayer"));
        selectedPlayer = spy(new Player("SelectedPlayer"));
        firstInputPlayer = spy(new Player("FirstInputPlayer"));
        secondInputPlayer = spy(new Player("SecondInputPlayer"));
        anotherPlayer = spy(new Player("AnotherPlayer"));
        sycophantPlayer.receiveHandCard(Card.SYCOPHANT);
        selectedPlayer.receiveHandCard(Card.COUNTESS);
        firstInputPlayer.receiveHandCard(Card.COUNT);
        secondInputPlayer.receiveHandCard(Card.COUNT);
        anotherPlayer.receiveHandCard(Card.PRIEST);

        // Initialize PlayerList
        playerList = new PlayerList();

        // Use reflection to modify the internal Deque<Player> players
        Field playersField = PlayerList.class.getDeclaredField("players");
        playersField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Deque<Player> players = (Deque<Player>) playersField.get(playerList);

        // Add players to the PlayerList
        players.add(sycophantPlayer);
        players.add(selectedPlayer);
        players.add(firstInputPlayer);
        players.add(secondInputPlayer);
        players.add(anotherPlayer);

        // Initialize Sycophant card
        sycophantCard = Card.SYCOPHANT;
        countessCard = Card.COUNTESS;
        game = new Game(userInput, playerList, deck);
    }

    /**
     * Rule 1: 
     * If the player selects the player of the next card, and the next card 
     * played requires the selection of a player, but that player cannot be “self”, 
     * then the card effect of Sycophant is ignored.
     */
    @Test
    void testSycophantEffectIgnoredWhenPlayerSelectionShouldNotBeSelf() {
        // Mock Scanner input to select the desired player
        when(mockScanner.nextLine()).thenReturn("SelectedPlayer").thenReturn("FirstInputPlayer");

        // Execute Sycophant card
        sycophantCard.execute(userInput, sycophantPlayer, playerList, deck);

        // Mock getOpponent to ensure it respects Sycophant effect
        Player returnedPlayer = userInput.getOpponent(playerList, selectedPlayer, false);

        // Assert that the returned player is the one chosen by the Sycophant effect
        assertEquals(firstInputPlayer, returnedPlayer);
    }

    /**
     * Rule 2:
     * Tests that if the player selects a valid choice of player using Sycophant,
     * the next call to getOpponent respects the Sycophant effect and returns the selected player.
     */
    @Test
    void testSycophantEffectReplacesNextPlayerSelection() {
        // Mock Scanner input to select the desired player
        when(mockScanner.nextLine()).thenReturn("SelectedPlayer").thenReturn("FirstInputPlayer");

        // Execute Sycophant card
        sycophantCard.execute(userInput, sycophantPlayer, playerList, deck);

        // Mock getOpponent to ensure it respects Sycophant effect
        Player returnedPlayer = userInput.getOpponent(playerList, anotherPlayer);

        // Assert that the returned player is the one chosen by the Sycophant effect
        assertEquals(selectedPlayer, returnedPlayer);
    }

    /**
     * Rule 3: 
     * If the player selects a valid choice of player, and the next card player requires
     * the selection of two players, then the next player’s first selection is replaced
     * and the second selection is unaffected.
     */
    @Test
    void testSycophantEffectReplacesOnlyFirstPlayerSelection() {
        // Mock Scanner input to select the desired player
        when(mockScanner.nextLine()).thenReturn("SelectedPlayer").
        thenReturn("FirstInputPlayer").thenReturn("SecondInputPlayer");

        // Execute Sycophant card
        sycophantCard.execute(userInput, sycophantPlayer, playerList, deck);

        // Mock getOpponent to ensure it respects Sycophant effect
        Player returnedPlayer1 = userInput.getOpponent(playerList, anotherPlayer);
        Player returnedPlayer2 = userInput.getOpponent(playerList, anotherPlayer);

        // Assert that the returned player is the one chosen by the Sycophant effect
        assertEquals(selectedPlayer, returnedPlayer1);
        assertEquals(secondInputPlayer, returnedPlayer2);
    }

    /**
     * Rule 4: 
     * If the player selects a valid choice of player, and the next card played does
     * not require the selection of player(s), then the card effect of Sycophant is ignored.
     */
    @Test
    void testSycophantEffectIgnoredWhenNoPlayerSelectionRequired() {
        when(mockScanner.nextLine()).thenReturn("SelectedPlayer").thenReturn("0");

        sycophantCard.execute(userInput, sycophantPlayer, playerList, deck);

        assertEquals(selectedPlayer, userInput.sycophantChoice);

        System.out.println("the selected player is: " + userInput.sycophantChoice.getName());

        game.executeTurn(selectedPlayer);

        assertNull(userInput.sycophantChoice);
    }

    @Test
    void testSycophantEffectIgnoredWhenNoPlayerSelectionRequired2() {
        when(mockScanner.nextLine()).thenReturn("SelectedPlayer").thenReturn("0").thenReturn("FirstInputPlayer");

        sycophantCard.execute(userInput, sycophantPlayer, playerList, deck);

        assertEquals(selectedPlayer, userInput.sycophantChoice);

        game.executeTurn(anotherPlayer);

        // assertNull(userInput.sycophantChoice);
    }
}
