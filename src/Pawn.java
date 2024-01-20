public class Pawn extends ConcretePiece {

    private int numOfCaptured;

    public Pawn(Player player){
        //define player field
        super.player = player;
        //enter unicode of pawn
        super.pieceType = "♟"; //2659 = pawn unicode
        //number of captured piece by this specific pawn
        this.numOfCaptured = 0;


        //todo: Counting the pieces by the player type, King is number 7 although its different piece
        //todo: Need to think on other option where to count and name the pieces.
        //naming the pieces, and counting them
//        if(player.isPlayerOne()) {
//            this.pieceNumber = pieceCounterD;
//            String pieceNameNumber = Integer.toString(pieceNumber);
//            super.pieceName = "D"+pieceNameNumber;
//            pieceCounterD++;
//        }else{
//            this.pieceNumber = pieceCounterA;
//            String pieceNameNumber = Integer.toString(pieceNumber);
//            super.pieceName = "A"+pieceNameNumber;
//            pieceCounterA++;
//        }
    }


    public int getCaptured(){return this.numOfCaptured;}

    public void addCapture(){this.numOfCaptured++;}

}

