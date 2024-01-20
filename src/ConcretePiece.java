public class ConcretePiece implements Piece{

    protected Player player; //player that own this piece
    protected String pieceType; //type of piece

    //empty constructor
    public ConcretePiece(){
    }

    @Override
    public Player getOwner() {
        return this.player;
    }

    @Override
    public String getType(){
        return this.pieceType;
    }


}
