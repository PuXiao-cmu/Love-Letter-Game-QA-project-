package edu.cmu.f24qa.loveletter;

public class ConstableAction implements CardAction{
    /**
     * When a player is knocked out of the round with the Constable card 
     * in their discard pile, they gain one Token of Affection.
     *
     *
     * @param userInput
     *          the input stream
     * @param user
     *          the player playing the card
     * @param players
     *          the player list
     * @param deck
     *          the deck
     */
    @Override
    public void execute(UserInput userInput, Player user, PlayerList players, Deck deck) {
        System.out.println("Constable Viktor will award a token if you are eliminated.");
    }
}
