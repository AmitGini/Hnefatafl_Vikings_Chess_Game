public class ConcretePlayer implements Player{

    private final boolean playerOne; //player1 = true, player2(king) = false
    private int numberOfWins; //counting the number of wins

    //Constructor, player1 = false, player2(king) = true, 0 number of wins for start.
    public ConcretePlayer(boolean player){
        this.playerOne = player;
        this.numberOfWins = 0;
    }

    //copy constructor, used for increasing number of wins
    public ConcretePlayer(boolean player, int winsCounter){
        this.playerOne = player;
        this.numberOfWins = winsCounter + 1;
    }

    // method return true if its player1 and false if not.
    @Override
    public boolean isPlayerOne(){
        return !(playerOne); //false if its player1;
    }

    //return the number of times the player has won.
    @Override
    public int getWins(){
        return numberOfWins;
    }


}
