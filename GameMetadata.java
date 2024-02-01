import java.util.*;

public class GameMetadata {

    private ArrayList<StepsNode> arrayListSteps;
    private ArrayList<DistanceAndKillsNode> arrayListDistanceAndKills;
    private ArrayList<MoveNode> arrayListMoveHistory;

    public GameMetadata(Stack<Movements> moveStackHistory, String[][] boardSteps, int[] disPieces, int[] capturedPieces, boolean isplayer2Won) {
        //turn the stack upside down and insert to the array Move History field, so the first out will be the first pieces created.
        //and the moves will add next by counting and String for sorting, as well as other data that most of it constant.
        this.arrayListMoveHistory = new ArrayList<MoveNode>();
        insertArrayMoves(moveStackHistory, isplayer2Won); //arrayMoveHistory data insertions.

        this.arrayListDistanceAndKills = new ArrayList<DistanceAndKillsNode>();
        insertArrayListDisAndKills(disPieces,capturedPieces, isplayer2Won);

        this.arrayListSteps = new ArrayList<StepsNode>();
        insertArrayListSteps(boardSteps); //steps board counter, insert with the sum of steps on each square

    }

    //todo: add note
    public void sortAndPrint() {
        //Moves: Sort by the winning side, number of moves, and number of piece
        Collections.sort(this.arrayListMoveHistory, new SortByMovesWinner());
        for (MoveNode node : this.arrayListMoveHistory) {
            if (node.getNumMoves() > 1) {
                System.out.println(node.toString());
            }
        }
        System.out.println("*".repeat(75));

        //Kills : Sort by the number of kills of every piece.
        Collections.sort(this.arrayListDistanceAndKills, new SortByKills());
        for (DistanceAndKillsNode node : this.arrayListDistanceAndKills) {
            if (node.getNumOfKills() > 0) {
                System.out.println(node.toString(false));
            }
        }
        System.out.println("*".repeat(75));

        //Distance : sort by the distance every piece did
        Collections.sort(this.arrayListDistanceAndKills, new SortByDistance());
        for (DistanceAndKillsNode node : this.arrayListDistanceAndKills) {   //printing distance
            if (node.getPieceDistance() > 0) {
                System.out.println(node.toString(true));
            }
        }
        System.out.println("*".repeat(75));

        //Steps : sort by number of steps of different piece on a specific square on the board.
        Collections.sort(this.arrayListSteps, new SortBySquareSteps());
        for (StepsNode node : this.arrayListSteps) {    //     printing by steps
            System.out.println(node.toString());
        }
        System.out.println("*".repeat(75));
    }

    private void insertArrayMoves(Stack<Movements> movesStack, boolean whoWon) {
        //reverse the stack so the first moves will be the first to pop out
        Stack<Movements> movementsStack = new Stack<Movements>();
        Queue<Movements> queue = new LinkedList<>();
        int k = 1;
        while (!movesStack.isEmpty()) {
            queue.add(movesStack.pop());
            k++;
        }
        while (!queue.isEmpty()) {
            movementsStack.add(queue.remove());
        }
        //insert the starting position of every piece;
            int i = 0;
            while(i < 37){
                Movements startingPointMoves = movementsStack.pop();
                Position startPosition = startingPointMoves.getCurrentPosition();
                this.arrayListMoveHistory.add(new MoveNode(startPosition,getPieceName(i),startPosition.toString(),whoWon));
                i++;
            }

            while(!movementsStack.isEmpty()){
                Movements move = movementsStack.pop();
                int start = move.getPiece().getOwner().isPlayerOne() ? 0 : 13;
                int end = move.getPiece().getOwner().isPlayerOne() ? 13 : 37;
                Position curr = move.getCurrentPosition();
                Position prev = move.getPreviousPosition();
                while(start < end){
                    if(this.arrayListMoveHistory.get(start).getCurrPos().equals(prev)){
                        this.arrayListMoveHistory.get(start).setAllMoves(curr.toString());
                        this.arrayListMoveHistory.get(start).setToCurrPos(curr);
                        break;
                    }
                    start++;
                }

            }
     }



    //sum of steps on specific square
    private void insertArrayListSteps(String[][] boardStepsCounter) {
        if(boardStepsCounter != null){
            for (int i = 0; i < boardStepsCounter.length; i++) {
                for (int j = 0; j < boardStepsCounter[0].length; j++) {
                    String square = boardStepsCounter[i][j];
                    int counter = 0;
                    //if we found A,D,K it means attacker piece defence piece or the king piece been on the square, every piece only once
                    //we make sure of that, when the boardStepCounter was made, and now we're just counting the number of piece at 2D array
                    if(square != null) {
                        for (int stringIndex = 0; stringIndex < square.length(); stringIndex++) {
                            if (square.charAt(stringIndex) == 'A' ||
                                    square.charAt(stringIndex) == 'D' ||
                                    square.charAt(stringIndex) == 'K')
                                counter++;
                        }
                        if(counter > 1) {
                            StepsNode tempNode = new StepsNode(j,i, counter);
                            this.arrayListSteps.add(tempNode);
                        }
                    }
                }
            }
        }
    }

    //todo: add note
    private void insertArrayListDisAndKills(int[] pieceDistance, int[] numCapture, boolean winner){
        if(pieceDistance != null){
            for(int i = 0; i < pieceDistance.length; i++){
                    String pieceName = getPieceName(i);
                    int pieceNum = getPieceNumber(i);
                    this.arrayListDistanceAndKills.add(new DistanceAndKillsNode(pieceName,pieceNum,pieceDistance[i],numCapture[i],winner));
            }
        }
    }

    //calculate the piece name by its place at the array.
    private String getPieceName(int indexInArray){
        int pieceNum = getPieceNumber(indexInArray);
        String pieceName = indexInArray < 13 ? (indexInArray == 6 ?  "K" + pieceNum :"D" + pieceNum) : "A" + pieceNum;
        return pieceName;
    }

    //todo: add note
    private int getPieceNumber(int indexInArray){
        int pieceNum = indexInArray < 13 ? indexInArray + 1 : indexInArray - 12;
        return pieceNum;
    }

}








