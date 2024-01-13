public class GameLogic implements PlayableLogic {

    private final int BOARD_SIZE = 11;
    private final Piece[][] boardPieces = new ConcretePiece[BOARD_SIZE][BOARD_SIZE];

    private Player player1; //defender
    private Player player2; //attacker (first move in new game)
    private boolean player2Turn;

    //constructor, initialize game logic
    public GameLogic(){
        this.player1 = new ConcretePlayer(false); //defender
        this.player2 = new ConcretePlayer(true); //attacker (first move in new game)
        initGame();
    }

    //initial the board to a new game.
    private void initGame(){
        resetBoard(); //reset the board to null;
        createPlayer1Pieces(); //create defender - player 1 pawns and king
        createPlayer2Pieces();  //create attacker - player 2 pawns
        this.player2Turn = true; //set attacker - player 2 first turn.
    }

    //reset the board pieces, all the cells defined as null.
    private void resetBoard(){
        for (int row = 0; row < BOARD_SIZE; row++){
            for (int col = 0; col < BOARD_SIZE; col++){
                boardPieces[row][col] = null;
            }
        }
    }

    // creating the defender(pawns and king) - Player 1
    /*                   [3,5]
                    [4,4][4,5][4,6]
               [5,3][5,4][5,5][5,6][5,7]
                    [6,4][6,5][6,6]
                         [7,5]
  */
    private void createPlayer1Pieces(){
        for(int defRowPosition = 3; defRowPosition < 8; defRowPosition++) {
            for(int defColPosition = 3; defColPosition < 8; defColPosition++) {
                //[5,5] KING
                if(defRowPosition == BOARD_SIZE/2 && defColPosition == BOARD_SIZE/2) {  //set King at [5,5]
                    boardPieces[defRowPosition][defColPosition] = new King(getFirstPlayer());
                }
                //Pawns(around the King) - [4,4],[4,5],[4,6],[5,4],[5,6],[6,4],[6,5],[6,6]
                else if(defRowPosition > 3 && defColPosition > 3 && defRowPosition < 7 && defColPosition < 7) {
                    boardPieces[defRowPosition][defColPosition] = new Pawn(getFirstPlayer());
                }
                //Pawns - [3,5], [5,3], [5,7], [7,5]
                else if((defRowPosition + 2 == defColPosition || defColPosition + 2 == defRowPosition)){
                    boardPieces[defRowPosition][defColPosition] = new Pawn(getFirstPlayer());
                }
            }
        }
    }

    //  creating the attacker - Player 2
    private void createPlayer2Pieces(){
        for(int defPositions = 1; defPositions < BOARD_SIZE-1; defPositions++){
            if(defPositions > 2 && defPositions < 8) {
                boardPieces[0][defPositions] = new Pawn(getSecondPlayer());
                boardPieces[BOARD_SIZE-1][defPositions] = new Pawn(getSecondPlayer());
                boardPieces[defPositions][0] = new Pawn(getSecondPlayer());
                boardPieces[defPositions][BOARD_SIZE-1] = new Pawn(getSecondPlayer());
            }
            if(defPositions == 9 || defPositions == 1){
                boardPieces[defPositions][5] = new Pawn(getSecondPlayer());
                boardPieces[5][defPositions] = new Pawn(getSecondPlayer());
            }
        }
    }


    //check if the move is valid, return true if not.
    private boolean isNotValidMove(Position a, Position b){
        // position out of boundaries bigger then the size of the board
        if(a.getX() >= getBoardSize() || b.getX() >= getBoardSize() || a.getY() >= getBoardSize() || b.getY() >= getBoardSize()){
            return true;
        }

        // position out of boundaries smaller than the size of the board
        if(a.getX() < 0 || b.getX() < 0  || a.getY() < 0  || b.getY() < 0 ){
            return true;
        }

        if(a.getX() != b.getX() && a.getY() != b.getY()){ //illegal move (move not in a row or column)
            return true;
        }

        if(a.getX() == b.getX() && a.getY() == b.getY()){ //same position (click on the same position)
            return true;
        }

        //illegal move for pawns: [0,0],[0,10],[10,0],[10,10]
        if(!(isThatTheKing(a))) {
            if (b.getX() == 0 && b.getY() == 0) return true; // [0,0]
            if (b.getX() == 10 && b.getY() == 0) return true;// [10,0]
            if (b.getX() == 0 && b.getY() == 10) return true;// [0,10]
            return b.getX() == 10 && b.getY() == 10;// [10,10]
        }
        return false;
    }

    //check whose turn is it, and the owner of the piece
    private boolean isItMyPieceAndTurn(Position a){
        if(getPieceAtPosition(a) == null) return false;
        boolean isPlayer1Piece = getPieceAtPosition(a).getOwner().isPlayerOne();//is it the first player piece?
        //case1: player 2 turn and player 1 piece = illegal move, case2: player 1 turn and player 2 piece = illegal move - every case like this will return true.
        boolean myPieceAndTurn = ((isSecondPlayerTurn() && !(isPlayer1Piece)) || (!(isSecondPlayerTurn()) && isPlayer1Piece));
        return myPieceAndTurn;
    }

    //check if the king is a specific position
    private boolean isThatTheKing(Position a){
        return boardPieces[a.getX()][a.getY()].getType().equals("♔"); //check if its types are equals
    }

    private void checkCapture(Position a, int stepX, int stepY){
        Position step = new Position(a, stepX, stepY);
        if(!(isNotValidMove(a,step))){ //get in, if the step is in a valid attack position
            // get in, if we have piece at this position and only if we have, check if the piece at the next step is the enemy piece and only then get in.
            if(!(getPieceAtPosition(step) == null)){
                    if(!(isItMyPieceAndTurn(step)) && !(isThatTheKing(step))) {
                        Position step2 = new Position(step, stepX, stepY);
                        if (isNotValidMove(a, step2)) {
                            capture(step); //capture if the piece got cornered
                        } else if (isItMyPieceAndTurn(step2) && !(isThatTheKing(step2))) {
                            capture(step); //capture the piece if we have 2 piece between the enemy piece
                        }
                    }
                }
            }
    }


    private void capture(Position pieceCaptured){
        this.boardPieces[pieceCaptured.getX()][pieceCaptured.getY()] = null;
    }

    //define if its valid move, updating the board(2D array), checking if the enemy has defeated according to the new position.
    @Override
    public boolean move(Position a, Position b){
        //check illegal moves, if illegalMove return true, then return false.(also illegal specific for pawns)
        if(isNotValidMove(a,b) || !(isItMyPieceAndTurn(a))){
            return false;
        }

        //calculate valid logic
        boolean isHorizontal = a.getY() != b.getY(); //check the direction move, the direction is horizontal if the columns are not equal
        int startPosition = isHorizontal ? a.getY() : a.getX(); //define the index, start position of the piece, depend on the direction.
        int endPosition = isHorizontal ? b.getY() : b.getX();   //define the index, end position of the piece, depend on the direction.
        int FixedPosition = isHorizontal ? a.getX() : a.getY(); //saving the fixed position according to the direction of the movement
        int step = startPosition < endPosition ? 1 : -1; //step = 1; //define if to move forward or backward.

        //loop, check if every position from the start to the end is clear from pieces, else return false - illegal move
        for (int checkPosition = startPosition + step; checkPosition != endPosition+step; checkPosition = checkPosition + step) {
            //depend on the direction, define row and column index's of the next square, for checking if there are pieces between 2 positions.
            int column = isHorizontal ? FixedPosition : checkPosition; //horizontal movement
            int row = isHorizontal ? checkPosition : FixedPosition; //vertical movement
            Position newPosition = new Position(row, column); //define the new position
            if (getPieceAtPosition(newPosition) != null) { //check if there are pieces at this position
                return false; //return false if there is a piece at the road to position b.
            }
        }

        //updating the board according to the position moves
        this.boardPieces[b.getX()][b.getY()] = this.boardPieces[a.getX()][a.getY()];
        this.boardPieces[a.getX()][a.getY()] = null;

        if(!(isThatTheKing(b))) {
            checkCapture(b, 1, 0);
            checkCapture(b, -1, 0);
            checkCapture(b, 0, 1);
            checkCapture(b, 0, -1);
        }

        //todo:save the moves that made
        //todo:save the defeated enemy's


        if(isGameFinished()){ //todo:consider the option to move the init game to the isGameFinished function
            initGame();
        }

        this.player2Turn = !(this.player2Turn); //change the player turns
        return true;
    }


    //return the piece that located at given position in the table(2D array)
    @Override
    public Piece getPieceAtPosition(Position position){
        return boardPieces[position.getX()][position.getY()];
    }

    //return player 1
    @Override
    public Player getFirstPlayer() {
        return this.player1;
    }

    //return player 2
    @Override
    public Player getSecondPlayer() {
        return this.player2;
    }

    //return true if its player2 turn, else false
    @Override
    public boolean isSecondPlayerTurn(){
        return this.player2Turn;
    }

    //check if the game is finished, according to the turn check win for player 1 or 2, and increase number of wins.
    @Override
    public boolean isGameFinished(){
        if(isSecondPlayerTurn()){
            return false; //todo: edit
        }else{
            if(isThatTheKing(new Position(0,0))){return true;}
            else if(isThatTheKing(new Position(10,0))){return true;}
            else if(isThatTheKing(new Position(0,10))){return true;}
            else if(isThatTheKing(new Position(10,10))){return true;}
            else return false;
        }
        //todo - check who's turn is it, so we wont have to check both side every turn
        //todo: increase number of wins to player 2 or 1(according to the player turn)
    }

    // reset the board pieces(Array 2D), and the information of the players.
    @Override
    public void reset() {
        this.player1 = new ConcretePlayer(true); //defender
        this.player2 = new ConcretePlayer(false); //attacker (first move in new game)
        initGame();
    }

    //return to the last move that has been made.
    @Override
    public void undoLastMove(){
    //todo: Idea, export the last object of move at the stack, consider another stack of enemy's to export, also change the player turn.
    }

    //return the size of the board.
    @Override
    public int getBoardSize(){
        return this.BOARD_SIZE;
    }
}
