package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Assertions;

/**
 * Blackbox tests for Constable card in 5-8 player game.
 */
class BlackboxConstableLogicTest {
    private UserInput mockUserInput;
    private PlayerList playerList;
    private Player player;
    private Deck deck;

    @BeforeEach
    void setup() {
        mockUserInput = mock(UserInput.class);
        playerList = new PlayerList();
        deck = new Deck();
        deck.buildPremium();

        playerList.addPlayer("TestPlayer");
        playerList.addPlayer("Player2");
        playerList.addPlayer("Player3");
        playerList.addPlayer("Player4");
        playerList.addPlayer("Player5");
        
        player = playerList.getPlayer("TestPlayer");
        assertNotNull(player, "Player should not be null");
    }

    /**
     * Rule 1: Tests that playing Constable only displays message
     */
    @Test
    void testConstableActionDisplaysMessage() {
        Assertions.assertDoesNotThrow(() -> {
            ConstableAction constableAction = new ConstableAction();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outputStream));

            try {
                constableAction.execute(mockUserInput, player, playerList, deck);

                String expectedOutput = "Constable Viktor will award a token if you are eliminated";
                String actualOutput = outputStream.toString().trim();
                assertTrue(actualOutput.contains(expectedOutput));
            } finally {
                System.setOut(originalOut);
            }
        });
    }

    /**
     * Rule 2: Tests that player does not get token when eliminated without Constable
     */
    @Test
    void testPlayerDoesNotGetTokenWhenEliminatedWithoutConstable() {
        Assertions.assertDoesNotThrow(() -> {
            player.receiveHandCard(Card.GUARD);
            player.discardCard(Card.GUARD);
            int initialTokens = player.getTokens();
            
            player.eliminate();

            assertEquals(initialTokens, player.getTokens(),
                "Player should not gain token when eliminated without Constable");
        });
    }

    /**
     * Rule 3: Tests that player wins game when getting final token from Constable
     */
    @Test
    void testPlayerWinsGameWithConstableToken() {
        Assertions.assertDoesNotThrow(() -> {
            // Setup player to be one token away from victory (3 tokens for 5-8 player game)
            for (int i = 0; i < 3; i++) {
                player.addToken();
            }
            
            player.receiveHandCard(Card.GUARD);
            player.discardCard(Card.CONSTABLE);
            
            player.eliminate();

            assertEquals(4, player.getTokens(),
                "Player should gain one token when eliminated with Constable");
            assertNotNull(playerList.getGameWinnerCandidates(),
                "There should be game winners");
            assertTrue(playerList.getGameWinnerCandidates().contains(player),
                "Player should be in the winners list");
        });
    }

    /**
     * Rule 4: Tests that player only gets token but doesn't win when needing more tokens
     */
    @Test
    void testPlayerGetsTokenButNotWinWithConstable() {
        Assertions.assertDoesNotThrow(() -> {
            // Start with 0 tokens, needs 4 for victory in 5-8 player game
            player.receiveHandCard(Card.GUARD);
            player.discardCard(Card.CONSTABLE);
            int initialTokens = player.getTokens();
            
            player.eliminate();

            assertEquals(initialTokens + 1, player.getTokens(),
                "Player should gain one token when eliminated with Constable");
            assertTrue(playerList.getGameWinnerCandidates() == null || playerList.getGameWinnerCandidates().isEmpty(),
                "Player should not win the game when needing more tokens");
        });
    }
}