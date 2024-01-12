public class ConcretePlayer implements Player{

    private final boolean playerOne; //player1 = true, player2(king) = false
    private int numberOfWins; //todo: need to update the wins, counting the number of wins

    //Constructor, player1 = true, player2(king) = false, 0 number of wins for start.
    public ConcretePlayer(boolean player){
        this.playerOne = player;
        numberOfWins = 0;
    }

    // method return true if its player1 and false if not.
    public boolean isPlayerOne(){
        return !(playerOne); //true if its player1;
    }

    //return the number of times the player has won.
    public int getWins(){
        return numberOfWins;
    }

}
