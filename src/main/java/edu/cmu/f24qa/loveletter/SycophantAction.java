package edu.cmu.f24qa.loveletter;

public class SycophantAction implements CardAction {

    // Q1: count rule
    // Q2: current test: blackbox + white box, + scenario
    // Q3: what does the grading care about
    // Q4: what does report need to cover?


    @Override
    public void execute(UserInput userInput, Player user, PlayerList players, Deck deck) {
        Player player = userInput.getOpponent(players, user, true);

        userInput.setSycophantChoice(player);

        // TODO: need to implement check for card in next turn.
    }
    
}
