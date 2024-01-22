import java.lang.*;
import java.util.*;

public class Sorts{
    public Sorts(){

    }
}

//
class SortByKills implements Comparator<DistanceAndKillsNode> {

    @Override
    public int compare(DistanceAndKillsNode nodeA, DistanceAndKillsNode nodeB){
        //compare the number of kills of each piece
        //1nd level comparison
        int numOfCapture = Integer.compare(nodeB.getNumOfKills(), nodeA.getNumOfKills());
        //compare by the number of the piece
        //2nd level comparison
        int pieceNum = nodeA.compareTo(nodeB.getPieceNumber());
        //check the compressions
        int compare = (numOfCapture == 0) ? pieceNum : numOfCapture;
        //compare by the winner side in case the other compressions were equals.
        if(compare == 0){
            if (nodeA.getPieceName().charAt(0) == 'A') {
                return nodeA.getWhoWon() ? -1 : 1;
            } else {
                return nodeA.getWhoWon() ? 1 : -1;
            }
        }return compare;
    }
}

//sort distance of piece by 1- sum of the distance from top, 2- number of piece from down, 3- piece winner.
class SortByDistance implements Comparator<DistanceAndKillsNode> {
    //sort distance of piece by 1- sum of the distance from top, 2- number of piece from down, 3- piece winner.
    @Override
    public int compare(DistanceAndKillsNode nodeA, DistanceAndKillsNode nodeB) {
        //compare distance
        int distanceCompare = nodeB.compareTo(nodeA);
        //compare piece number
        int pieceNum = nodeA.compareTo(nodeB.getPieceNumber());
        //2nd level comparison
        int compare = (distanceCompare == 0) ? pieceNum : distanceCompare;
        //3nd level comparison
        if (compare == 0) {
            if (nodeA.getPieceName().charAt(0) == 'A') {
                return nodeA.getWhoWon() ? -1 : 1;
            } else {
                return nodeA.getWhoWon() ? 1 : -1;
            }
        } else return compare;
    }
}

//sort number of steps on a square of difference pieces 1. number of steps from down, 2. by X from down 3. by Y from down
class SortBySquareSteps implements Comparator<StepsNode>{

    @Override
    public int compare(StepsNode nodeA, StepsNode nodeB){
        //compare steps
        int stepsCompare = nodeA.compareTo(nodeB.getNumSteps());
        // compareTo function at stepsNode class, check if the x is equal if so it compare between the y
        int xYCompare = nodeA.compareTo(nodeB);
        //if stepsCompare = 0 the number of steps equal so we will sort according the x and then y if the x also will be equal
        return stepsCompare == 0 ? xYCompare : stepsCompare;
    }
}