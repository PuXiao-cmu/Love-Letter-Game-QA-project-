package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PrincessDiscardRulesTest {
    private UserInput mockUserInput;
    private PlayerList mockPlayerList;
    private Player spyPlayer;
    private Player spyOpponent;
    private Deck deck;

    @BeforeEach
    void setup() {
        mockUserInput = mock(UserInput.class);
        mockPlayerList = mock(PlayerList.class);
        spyPlayer = spy(new Player("Player"));
        spyOpponent = spy(new Player("Opponent"));
        deck = new Deck();
        deck.build();
        deck.shuffle();
    }

    @Test
    void testPrinceActionWithPrincessCausesEliminationAndStopsEffect() {
        // Setup
        spyOpponent = spy(new Player("Opponent"));
        spyOpponent.receiveHandCard(Card.PRINCESS);
        when(mockUserInput.getOpponent(mockPlayerList, spyPlayer, true)).thenReturn(spyOpponent);

        PrinceAction princeAction = new PrinceAction();
        princeAction.execute(mockUserInput, spyPlayer, mockPlayerList, deck);

        // Verify opponent was eliminated and no card was drawn
        verify(spyOpponent).eliminate();
        verify(spyOpponent, times(1)).viewHandCard(0);
        verify(spyOpponent, never()).playHandCard(0);
        verify(spyOpponent, never()).discardCard(any(Card.class));
    }

    @Disabled("Issue #[104]: This bug will be fixed in a future PR")
    @Test
    void testKingActionWithPrincessCausesEliminationAndStopsEffect() {
        Player realPlayer = new Player("Player");
        Player realOpponent = new Player("Opponent");
        
        realOpponent.receiveHandCard(Card.PRINCESS);
        realPlayer.receiveHandCard(Card.KING);
        
        spyPlayer = spy(realPlayer);
        spyOpponent = spy(realOpponent);
        
        when(mockUserInput.getOpponent(mockPlayerList, spyPlayer)).thenReturn(spyOpponent);
    
        KingAction kingAction = new KingAction();
        kingAction.execute(mockUserInput, spyPlayer, mockPlayerList, deck);
    
        // verify
        verify(spyOpponent).viewHandCard(0);
        verify(spyOpponent).eliminate();
        verify(spyPlayer, never()).receiveHandCard(any());
        verify(spyOpponent, never()).receiveHandCard(any());
    }
}