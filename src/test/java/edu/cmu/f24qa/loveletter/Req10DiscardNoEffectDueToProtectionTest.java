package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class Req10DiscardNoEffectDueToProtectionTest {

    private PlayerList playerList;
    private Deck mockDeck;
    private UserInput mockUserInput;
    private Player alice;
    private Player bob;
    private Player charlie;
    private Card spyGuard;

    @BeforeEach
    void setUp() {
        // Create a PlayerList
        playerList = new PlayerList();
        playerList.addPlayer("Alice");
        playerList.addPlayer("Bob");
        playerList.addPlayer("Charlie");

        // Get players
        alice = playerList.getPlayer("Alice");
        bob = playerList.getPlayer("Bob");
        charlie = playerList.getPlayer("Charlie");

        // Mock UserInput
        mockUserInput = mock(UserInput.class);

        // Mock the deck
        mockDeck = mock(Deck.class);

        // Create a spy for the GUARD card
        spyGuard = spy(Card.GUARD);

        // Protect all players except the user
        bob.switchProtection();
        charlie.switchProtection();
    }

    /*
     *  Requirement 10: If all valid targets are unavailable, the system shall discard the card with no effect applied.
     */
    @Disabled("Issue #93: If all valid targets are protected, the system does not discard the card and goes into a infinite loop.")
    @Test
    void testExecuteCardWithoutCallingGetOpponent() {
        //Create a case that spyGuard.execute() can end normally
        bob.receiveHandCard(Card.COUNTESS);
        when(mockUserInput.getOpponent(playerList, alice)).thenReturn(bob);
        when(mockUserInput.getCardName()).thenReturn("Countess");

        // Alice plays GUARD
        spyGuard.execute(mockUserInput, alice, playerList, mockDeck);

        // Verify that getOpponent is not called
        verify(mockUserInput, never()).getOpponent(any(PlayerList.class), any(Player.class));
    }
}


