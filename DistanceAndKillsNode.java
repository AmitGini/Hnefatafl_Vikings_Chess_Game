
//todo: add note
class DistanceAndKillsNode {

    private String pieceName;
    private int pieceNumber;
    private int pieceDistance;
    private int numOfKills;
    private boolean isPlayer2Won; //player1 won = false, player2 won = true;

    public DistanceAndKillsNode(String name, int number, int distance, int capture, boolean winner){
        this.pieceName = name;
        this.pieceNumber = number;
        this.pieceDistance = distance;
        this.numOfKills = capture;
        this.isPlayer2Won = winner;
    }

    public String getPieceName(){return this.pieceName;}

    public int getPieceNumber() {return this.pieceNumber;}

    public int getPieceDistance(){return this.pieceDistance;}

    public int getNumOfKills(){return this.numOfKills;}

    public boolean getPlayer2Won(){return this.isPlayer2Won;}


    public int compareTo(DistanceAndKillsNode node){
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

    public String toString(boolean printDistance){
        if(printDistance) {
            return this.pieceName + ": " + this.pieceDistance + " squares";
        }else{
            return this.pieceName + ": " + this.numOfKills + " kills";
        }
    }
}
