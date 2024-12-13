package edu.cmu.f24qa.loveletter;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Whitebox tests for Constable card functionality in 5-8 player game.
 */
public class WhiteboxConstableLogicTest {
    private UserInput mockUserInput;
    private Player mockUser;
    private PlayerList mockPlayerList;
    private ConstableAction constableAction;
    private Deck deck;

    @BeforeEach
    void setUp() {
        mockUserInput = mock(UserInput.class);
        mockUser = mock(Player.class);
        mockPlayerList = mock(PlayerList.class);
        constableAction = new ConstableAction();
        deck = new Deck();
        deck.buildPremium();

        when(mockUser.getName()).thenReturn("User");
    }

    /**
     * ConstableT1: Test message display when playing Constable (Constable-W1)
     */
    @Test
    void testConstableT1_DisplayMessage() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            constableAction.execute(mockUserInput, mockUser, mockPlayerList, deck);
            
            String output = outputStream.toString().trim();
            assert(output.contains("Constable Viktor will award a token if you are eliminated"));
            
            verify(mockUser, never()).addToken();
        } finally {
            System.setOut(originalOut);
        }
    }

    /**
     * ConstableT2: Player is eliminated with Constable in discard pile (Constable-W2)
     */
    @Test
    void testConstableT2_TokenAwardOnElimination() {
        Player realUser = new Player("TestPlayer");
        realUser.receiveHandCard(Card.GUARD);
        realUser.discardCard(Card.CONSTABLE);
        int initialTokens = realUser.getTokens();

        realUser.eliminate();

        assert(realUser.getTokens() == initialTokens + 1);
    }

    /**
     * ConstableT3: Multiple Constables only award one token (Constable-W3)
     */
    @Test
    void testConstableT3_MultipleConstables() {
        Player realUser = new Player("TestPlayer");
        realUser.receiveHandCard(Card.GUARD);
        realUser.discardCard(Card.CONSTABLE);
        realUser.discardCard(Card.CONSTABLE);
        int initialTokens = realUser.getTokens();

        realUser.eliminate();

        assert(realUser.getTokens() == initialTokens + 1);
    }

    /**
     * ConstableT4: No token awarded without Constable (Constable-W4)
     */
    @Test
    void testConstableT4_NoConstableNoToken() {
        Player realUser = new Player("TestPlayer");
        realUser.receiveHandCard(Card.GUARD);
        realUser.discardCard(Card.GUARD);
        int initialTokens = realUser.getTokens();

        realUser.eliminate();

        assert(realUser.getTokens() == initialTokens);
    }

    /**
     * ConstableT5: Token from Constable leads to game win (Constable-W5)
     */
    @Test
    void testConstableT5_TokenLeadsToWin() {
        PlayerList realPlayerList = new PlayerList();
        for (int i = 0; i < 5; i++) {
            realPlayerList.addPlayer("Player" + i);
        }
        
        Player mainPlayer = realPlayerList.getPlayer("Player0");
        for (int i = 0; i < 3; i++) {
            mainPlayer.addToken();
        }
        
        mainPlayer.receiveHandCard(Card.GUARD);
        mainPlayer.discardCard(Card.CONSTABLE);
        
        mainPlayer.eliminate();

        assert(mainPlayer.getTokens() == 4);
        assert(realPlayerList.getGameWinnerCandidates().contains(mainPlayer));
    }
}