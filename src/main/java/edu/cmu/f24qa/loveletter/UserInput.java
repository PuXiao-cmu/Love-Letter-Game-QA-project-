package edu.cmu.f24qa.loveletter;

/**
 * Interface for handling user input in the Love Letter game.
 */
public interface UserInput {

    /**
     * Retrieves a list of players for the game.
     *
     * @return a PlayerList containing the players
     */
    PlayerList getPlayers();

    /**
     * Prompts the user to select a card index from their hand.
     *
     * @param user the player making the selection
     * @return the index of the selected card as a string
     */
    String getCardIndex(Player user);

    /**
     * Prompts the user to enter the name of a card for guessing.
     *
     * @return the name of the card guessed by the user
     */
    String getCardName();

    /**
     * Get the opponent name from user input.
     * 
     * @return the name of the opponent
     */
    String getOpponentName();
}
