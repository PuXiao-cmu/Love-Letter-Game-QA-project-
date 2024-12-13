package edu.cmu.f24qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class WhiteboxCountCardTest {

    private UserInput mockUserInput;
    private Player mockUser;
    private PlayerList mockPlayers;
    private Deck mockDeck;
    private CountAction countAction;

    @BeforeEach
    void setup() {
        // Initialize mocks for dependencies
        mockUserInput = mock(UserInput.class);
        mockUser = mock(Player.class);
        mockPlayers = mock(PlayerList.class);
        mockDeck = mock(Deck.class);

        // Initialize the CountAction instance
        countAction = new CountAction();
    }

    @Test
    void testExecute() {
        // Act
        assertDoesNotThrow(() -> countAction.execute(mockUserInput, mockUser, mockPlayers, mockDeck));

        // Verify no unexpected interactions with mocks
        verifyNoInteractions(mockUserInput, mockUser, mockPlayers, mockDeck);
    }
}
