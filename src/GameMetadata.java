import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class GameMetadata {

    private ArrayList<StepsNode> arrayListSteps;
    private ArrayList<DistanceAndKillsNode> arrayListDistanceAndKills;
    private Stack<Movements> moveHistory;

    public GameMetadata(String[][] boardSteps, int[] disPieces, int[] capturedPieces, boolean winSide, Stack<Movements> moveStackHistory){
        this.arrayListSteps = new ArrayList<StepsNode>();
        insertArrayListSteps(boardSteps); //steps board counter, insert with the sum of steps on each square
        this.arrayListDistanceAndKills = new ArrayList<DistanceAndKillsNode>();
        insertArrayListDisAndKills(disPieces,capturedPieces, winSide);
        //todo: edit sorting by winning side and number of steps, printing every steps on the way.

        init();
    }

    private void init(){
        //todo: make a class represent Node for the stack moves, and sort it.


        //Kills : Sort by the number of kills of every piece.
        Collections.sort(this.arrayListDistanceAndKills, new SortByKills());
        for(DistanceAndKillsNode node : this.arrayListDistanceAndKills){
            System.out.println(node.toString(false));
        }
        //Distance : sort by the distance every piece did
        Collections.sort(this.arrayListDistanceAndKills, new SortByDistance()); //todo:show only piece that moved at least once
        for(DistanceAndKillsNode node : this.arrayListDistanceAndKills){   //printing distance
            if(node.getPieceDistance() > 0) {
                System.out.println(node.toString(true));
            }
        }

        //Steps : sort by number of steps of different piece on a specific square on the board.
        Collections.sort(this.arrayListSteps, new SortBySquareSteps());
        for(StepsNode node : this.arrayListSteps){    //     printing by steps
            System.out.println(node.toString());
        }

    }


    //sum of steps on specific square
    private void insertArrayListSteps(String[][] boardStepsCounter) {
        if(boardStepsCounter != null){
            for (int i = 0; i < boardStepsCounter.length; i++) {
                for (int j = 0; j < boardStepsCounter[0].length; j++) {
                    String square = boardStepsCounter[i][j];
                    int counter = 0;
                    //if we found A,D,K it means attacker piece defence piece or the king piece been on the square, ever piece only once
                    if(square != null) {
                        for (int stringIndex = 0; stringIndex < square.length(); stringIndex++) {
                            if (square.charAt(stringIndex) == 'A' ||
                                    square.charAt(stringIndex) == 'D' ||
                                    square.charAt(stringIndex) == 'K')
                                counter++;
                        }
                        if(counter > 1) {
                            StepsNode tempNode = new StepsNode(i,j, counter);
                            this.arrayListSteps.add(tempNode);
                        }
                    }
                }
            }
        }
    }

    private void insertArrayListDisAndKills(int[] pieceDistance, int[] numCapture, boolean winner){
        if(pieceDistance != null){
            for(int i = 0; i < pieceDistance.length; i++){
                    int pieceNum = i < 13 ? i + 1 : i - 12;
                    String pieceName = i < 13 ? (i == 6 ?  "K" + pieceNum :"D" + pieceNum) : "A" + pieceNum;
                    this.arrayListDistanceAndKills.add(new DistanceAndKillsNode(pieceName,pieceNum,pieceDistance[i],numCapture[i],winner));

            }
        }
    }


}





//node for creating list to iterate and sort using collection
//node represent the square on board with field of steps and coordination
class StepsNode{

    private int x;
    private int y;
    private int numSteps;

    public StepsNode(int x, int y, int numSteps){
        this.x = x;
        this.y = y;
        this.numSteps = numSteps;
    }

    public int getX(){ return this.x; }

    public int getY(){ return this.y; }

    public int getNumSteps(){ return this.numSteps; }

    public int compareTo(StepsNode node){
        if(this.x == node.getX()) return Integer.compare(this.y, node.getY());
        return Integer.compare(this.x, node.getX());
    }

    public int compareTo(int numberOfSteps){
        return Integer.compare(this.numSteps, numberOfSteps);
    }

    //return the data represent as (x,y )z pieces
    @Override
    public String toString(){
        return "(" + this.x + ", " + this.y + ")" + numSteps + " pieces";
    }
}


//todo: edit
class DistanceAndKillsNode {

    private String pieceName;
    private int pieceNumber;
    private int pieceDistance;
    private int numOfKills;
    private boolean whoWon; //player1 won = false, player2 won = true;

    public DistanceAndKillsNode(String name, int number, int distance, int capture, boolean winner){
        this.pieceName = name;
        this.pieceNumber = number;
        this.pieceDistance = distance;
        this.numOfKills = capture;
        this.whoWon = winner;
    }

    public String getPieceName(){return this.pieceName;}

    public int getPieceNumber() {return this.pieceNumber;}

    public int getPieceDistance(){return this.pieceDistance;}

    public int getNumOfKills(){return this.numOfKills;}

    public boolean getWhoWon(){return this.whoWon;}

    //distance compare
    public int compareTo(DistanceAndKillsNode node){
        return Integer.compare(this.pieceDistance, node.getPieceDistance());
    }

    //compare by piece number
    public int compareTo(int pieceNum){
        return Integer.compare(this.pieceNumber, pieceNum);
    }

    public String toString(boolean printDistance){
        if(printDistance) {
            return this.pieceName + ": " + this.pieceDistance + " squares";
        }else{
            return this.pieceName + ": " + this.numOfKills + " kills";
        }
    }
}




