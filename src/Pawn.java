public class Pawn extends ConcretePiece {

    private int numOfKills;

    public Pawn(Player player){
        super.player = player;
        //enter unicode of pawn
        super.pieceType = "♟"; //2659 = white pawn unicode
        numOfKills = 0;
    }

    public void kill(){
        this.numOfKills++;
    }
}
