package edu.cmu.f24qa.loveletter;

public class CardinalAction implements CardAction {

    /**
     * Executes the Cardinal card action.
     * Allows the player to choose two players to switch hands and then view one of the switched hands.
     *
     * @param userInput the input stream
     * @param user the player playing the card
     * @param players the player list
     * @param deck the deck
     */
    @Override
    public void execute(UserInput userInput, Player user, PlayerList players, Deck deck) {
        // Prompt the user to select two players for swapping
        System.out.println("Choose the first player to swap hands and view hand:");
        Player player1 = userInput.getOpponent(players, user, true);
        Player player2;
        do {
            System.out.println("Choose a different player to swap hands:");
            player2 = userInput.getOpponent(players, user, true);

            if (player1 == player2) {
                System.out.println("You cannot choose the same player twice. Try again.");
            }
        } while (player1 == player2);

        // Perform the hand swap
        Card temp = player1.playHandCard(0);
        player1.receiveHandCard(player2.playHandCard(0));
        player2.receiveHandCard(temp);

        System.out.println(player1.getName() + " and " + player2.getName() + " have swapped hands.");

        // Allow the user to view one of the swapped hands
        Card viewedCard = player1.viewHandCard(0);
        System.out.println("You viewed " + player1.getName() + "'s hand: " + viewedCard);
    }
}
