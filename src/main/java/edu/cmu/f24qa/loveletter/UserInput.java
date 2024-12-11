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
     * Prompts the user to enter a number guess
     * @return the number guessed by the user
     */
    int getCardNumber();

    /**
     * Get the opponent from user input.
     *
     * @param playerList the list of players in the game
     * @param user the player making the selection
     * @return the opponent Player
     */
    Player getOpponent(PlayerList playerList, Player user);

    /**
     * Get the opponent from user input.
     *
     * @param playerList the list of players in the game
     * @param user the player making the selection
     * @param selectSelf whether to allow the user to select themselves
     * @return the opponent Player
     */
    Player getOpponent(PlayerList playerList, Player user, boolean selectSelf);

    /**
     * Get the number of opponents the user selects.
     *
     * @return the number of opponents as an integer.
     */
    Integer getNumOpponent();
}
