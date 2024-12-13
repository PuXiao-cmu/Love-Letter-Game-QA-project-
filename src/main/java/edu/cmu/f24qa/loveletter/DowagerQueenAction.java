package edu.cmu.f24qa.loveletter;

public class DowagerQueenAction implements CardAction {
    /**
     * When you discard the Dowager Queen, choose another player still in the round. 
     * You and that player secretly compare your hands. 
     * The player with the higher number is knocked out of the round. 
     * In case of a tie, nothing happens.
     *
     * @param userInput the input stream
     * @param user the player playing the card
     * @param players the player list
     * @param deck the deck
     */
    @Override
    public void execute(UserInput userInput, Player user, PlayerList players, Deck deck) {
        // Get opponent to compare cards
        Player opponent = userInput.getOpponent(players, user);

        // Compare card values
        Card userCard = user.viewHandCard(0);
        Card opponentCard = opponent.viewHandCard(0);

        int comparison = Integer.compare(userCard.getValue(), opponentCard.getValue());

        // Handle comparison result
        if (comparison > 0) {
            // User has higher number, so user is knocked out
            user.eliminate();
            System.out.println("You have been eliminated!");
        } else if (comparison < 0) {
            // Opponent has higher number, so opponent is knocked out
            opponent.eliminate();
            System.out.println(opponent.getName() + " has been eliminated!");
        } else {
            System.out.println("It's a tie! No one is eliminated.");
        }
    }
}
