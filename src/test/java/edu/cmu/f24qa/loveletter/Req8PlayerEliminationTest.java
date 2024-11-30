package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class Req8PlayerEliminationTest {

    private PlayerList playerList;

    @BeforeEach
    void setUp() {
        // Initialize the PlayerList
        playerList = new PlayerList();
        playerList.addPlayer("Alice");
        playerList.addPlayer("Bob");
        playerList.addPlayer("Charlie");
    }

    /**
     * Requirement 8: Card Discard Without Effect for Eliminated Players
     * 8.1 When a player is eliminated, the system shall discard their current card face-up.
     */
    @Test
    void testCardDiscardOnElimination() {
        // Set up the scenario where Alice has a card and gets eliminated
        Player eliminatedPlayer = playerList.getPlayer("Alice");
        eliminatedPlayer.receiveHandCard(Card.KING); // Alice's card is King
        assertTrue(eliminatedPlayer.hasHandCards(), "Player should have cards before elimination.");

        // Simulate elimination
        eliminatedPlayer.eliminate();

        // Validate that the player's card is discarded face-up
        assertFalse(eliminatedPlayer.hasHandCards(), "Player should not have any cards after elimination.");
        assertEquals(6, eliminatedPlayer.discardedValue(),"The discarded card should match the player's card.");
    }

    /**
     * Requirement 8: Card Discard Without Effect for Eliminated Players
     * 8.2: The system shall ensure the discarded card's effect is not applied.
     */
    @Test
    void testNoEffectOnEliminatedCard() {
        // Set up the scenario where Alice has a mocked card
        Card mockedCard = mock(Card.class);
        Player eliminatedPlayer = playerList.getPlayer("Alice");
        eliminatedPlayer.receiveHandCard(mockedCard);
        assertTrue(eliminatedPlayer.hasHandCards(), "Player should have cards before elimination.");

        // Simulate elimination
        eliminatedPlayer.eliminate();

        // Verify that the execute() method of the card was never called
        verify(mockedCard, never()).execute(any(UserInput.class), eq(eliminatedPlayer), any(PlayerList.class), any(Deck.class));
    }
    
}

