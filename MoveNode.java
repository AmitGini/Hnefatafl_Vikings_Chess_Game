//todo: add note
class MoveNode{

    private String pieceName;
    private String allMoves;
    private int numMoves = 0;
    private boolean isPlayer2Won;
    private Position currPos;

    public MoveNode(Position currPos, String pieceName, String move, boolean winner){
        this.currPos = currPos;
        this.pieceName = pieceName;
        this.allMoves = move;
        this.isPlayer2Won = winner;
        this.numMoves++;
    }

    public Position getCurrPos(){
        return this.currPos;
    }
    public int getPieceNum(){
        String nameNum = this.pieceName.substring(1);
        return Integer.parseInt(nameNum);
    }

    public String getPieceName() {
        return this.pieceName;
    }

    public boolean getPlayer2Won() {
        return this.isPlayer2Won;
    }

    public int getNumMoves(){
        return this.numMoves;
    }

    public void setToCurrPos(Position pos){
        this.currPos = pos;
    }

    public void setAllMoves(String move){
        this.allMoves = this.allMoves + ", " + move;
        this.numMoves++;
    }

    public int compareTo(MoveNode node) {
        if (this.pieceName.charAt(0) == 'A') {
            if(node.getPieceName().charAt(0) == 'A') return 0;
            if(this.isPlayer2Won) return 1;
            return -1;
        } else {
            if(node.getPieceName().charAt(0) == 'D' || node.getPieceName().charAt(0) == 'K') return 0;
            if(this.isPlayer2Won) return -1;
            return 1;
        }
    }

    @Override
    public String toString(){
        return this.pieceName + ": [" + this.allMoves + "]";
    }

}
