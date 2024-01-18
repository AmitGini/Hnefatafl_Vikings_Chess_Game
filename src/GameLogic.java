import java.util.Stack;

public class GameLogic implements PlayableLogic {

    private final int BOARD_SIZE = 11;

    private Piece[][] boardPieces = new ConcretePiece[BOARD_SIZE][BOARD_SIZE];

    private Stack<Move> moveStack;

    private Player player1; //defender
    private Player player2; //attacker (first move in new game)
    private Position kingPosition;
    private boolean player2Turn;

    //constructor, initialize game logic
    public GameLogic(){
        this.player1 = new ConcretePlayer(false); //defender
        this.player2 = new ConcretePlayer(true); //attacker (first move in new game)
        this.player2Turn = true; //set attacker - player 2 first turn.
        initGame();
        this.kingPosition = new Position(5,5);
    }

    //initial the board to a new game.
    private void initGame(){
        resetBoard(); //reset the board to null;
        moveStack = new Stack<Move>(); //init the stacks of moves
        createPlayer1Pieces(); //create defender - player 1 pawns and king
        createPlayer2Pieces();  //create attacker - player 2 pawns
        this.player2Turn = true; //set attacker - player 2 first turn.
        this.kingPosition = new Position(5,5);
    }

    //reset the board pieces, all the cells defined as null.
    private void resetBoard(){
        for (int x = 0; x < BOARD_SIZE; x++){
            for (int y = 0; y < BOARD_SIZE; y++){
                boardPieces[x][y] = null;
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
        for(int xPosition = 3; xPosition < 8; xPosition++) {
            for(int yPosition = 3; yPosition < 8; yPosition++) {
                //[5,5] KING
                if(xPosition == BOARD_SIZE/2 && yPosition == BOARD_SIZE/2) {  //set King at [5,5]
                    boardPieces[xPosition][yPosition] = new King(getFirstPlayer());
                }
                //Pawns(around the King) - [4,4],[4,5],[4,6],[5,4],[5,6],[6,4],[6,5],[6,6]
                else if(xPosition > 3 && yPosition > 3 && xPosition < 7 && yPosition < 7) {
                    boardPieces[xPosition][yPosition] = new Pawn(getFirstPlayer());
                }
                //Pawns - [3,5], [5,3], [5,7], [7,5]
                else if((xPosition + 2 == yPosition || yPosition + 2 == xPosition)){
                    boardPieces[xPosition][yPosition] = new Pawn(getFirstPlayer());
                }
            }
        }
    }

    //  creating the attacker - Player 2
    private void createPlayer2Pieces(){
        for(int player2Pieces = 1; player2Pieces < BOARD_SIZE-1; player2Pieces++){
            if(player2Pieces > 2 && player2Pieces < 8) {
                boardPieces[0][player2Pieces] = new Pawn(getSecondPlayer());
                boardPieces[BOARD_SIZE-1][player2Pieces] = new Pawn(getSecondPlayer());
                boardPieces[player2Pieces][0] = new Pawn(getSecondPlayer());
                boardPieces[player2Pieces][BOARD_SIZE-1] = new Pawn(getSecondPlayer());
            }
            if(player2Pieces == 9 || player2Pieces == 1){
                boardPieces[player2Pieces][5] = new Pawn(getSecondPlayer());
                boardPieces[5][player2Pieces] = new Pawn(getSecondPlayer());
            }
        }
    }


    //check if the move is valid, return true if not.
    private boolean isInvalidMove(Position a, Position b){
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
        return ((isSecondPlayerTurn() && !(isPlayer1Piece)) || (!(isSecondPlayerTurn()) && isPlayer1Piece));
    }

    //check if the king is a specific position
    private boolean isThatTheKing(Position a){
        //check if the position given similar to the king position.
        return (a.getX() == kingPosition.getX() && a.getY() == kingPosition.getY());
    }

    private boolean checkCapture(Position a, int stepX, int stepY){
        Position step = new Position(a, stepX, stepY);
        if (!(isInvalidMove(a,step))){ //get in, if the step is in a valid attack position

            //if the position at step is null, it can occur because the piece at the boarders of the board game
            // in the other hand if it's the king that god cornered we need to check for player 2 win opportunity
            if (!(getPieceAtPosition(step) == null)){
                if (!(isItMyPieceAndTurn(step)) && !(isThatTheKing(step))) {
                    //if a is the king and next step is not player1 piece then king is blocked.
                    //at move function we made sure that after player2 move, the turn will change
                    //so the king will always be at the same turn and check for enemy's surround him
                    if(isThatTheKing(a)) return true;
                    Position step2 = new Position(step, stepX, stepY);

                    if (isInvalidMove(a, step2)) {
                        capture(step); //capture if the piece got cornered
                        return true;

                    } else if (isItMyPieceAndTurn(step2) && !(isThatTheKing(step2))) {
                        capture(step); //capture the piece if we have 2 piece between the enemy piece
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
        //if the step checked is invalid move, and it's the king, return true since he is being blocked
        //and it's a winning option
        return isThatTheKing(a);
    }

    private void capture(Position pieceCaptured){
        //todo:save the defeated enemy's
        this.boardPieces[pieceCaptured.getX()][pieceCaptured.getY()] = null;
    }

    //define if its valid move, updating the board(2D array), checking if the enemy has defeated according to the new position.
    @Override
    public boolean move(Position a, Position b){
        //check illegal moves, if illegalMove return true, then return false.(also illegal specific for pawns)
        if(isInvalidMove(a,b) || !(isItMyPieceAndTurn(a))){
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
        //todo:save the moves that made
        this.boardPieces[b.getX()][b.getY()] = this.boardPieces[a.getX()][a.getY()];
        this.boardPieces[a.getX()][a.getY()] = null;
        moveStack.push(new Move(b, a, this.player2Turn));

        //since the king cant capture, check if it's not the king, only then calling checkCapture function
        //to check if there are pieces of the other player that have been captured.
        if(!(isThatTheKing(b))) {
            //todo:if checkCapture return true, add number of kills
            if(checkCapture(b, 1, 0)) moveStack.peek().setCapture(b, 1, 0);
            if(checkCapture(b, -1, 0)) moveStack.peek().setCapture(b, -1, 0);
            if(checkCapture(b, 0, 1)) moveStack.peek().setCapture(b, 0, 1);
            if(checkCapture(b, 0, -1)) moveStack.peek().setCapture(b, 0, -1);
        }else this.kingPosition = b; //if it's the king we need to update the field representing its position

        this.player2Turn = !(this.player2Turn); //change the player turns

        if(isGameFinished()) { //todo:consider the option to move the init game to the isGameFinished function
            initGame();
        }

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
        //changing the turn before isGameFinished function, that why the boolean is set at NOT
        if(!(isSecondPlayerTurn())){
            if (checkCapture(kingPosition,1,0) & checkCapture(kingPosition,-1,0)
                & checkCapture(kingPosition,0,1) & checkCapture(kingPosition,0,-1)) {
                this.player2 = new ConcretePlayer(true, this.player2.getWins()); //increase number of wins
                return true;
            }
            return false;
        }else{
            //checking when its the first player turn if the condition for winning occur.
            if (getPieceAtPosition(new Position(0,0)) != null
                    || getPieceAtPosition(new Position(10,0)) != null
                    || getPieceAtPosition(new Position(0,10)) != null
                    || getPieceAtPosition(new Position(10,10)) != null ){
                this.player1 = new ConcretePlayer(false, this.player1.getWins()); //increase number of wins
                return true;
            }
            else return false;
        }
    }

    // reset the board pieces(Array 2D), and the information of the players.
    @Override
    public void reset() {
        this.player1 = new ConcretePlayer(false); //defender
        this.player2 = new ConcretePlayer(true); //attacker (first move in new game)
        initGame();
    }

    //return to the last move that has been made.
    @Override
    public void undoLastMove(){
    //todo: Idea, export the last object of move at the stack, consider another stack of enemy's to export, also change the player turn.
        if(!(moveStack.isEmpty())) {
            Move lastMove = moveStack.pop();
            this.player2Turn = lastMove.getPlayerTurn();
            move(lastMove.getCurrentPosition(), lastMove.getPreviousPosition());

            //if it's not the king we need to check if their any captured enemy we need to return.
            if(!(isThatTheKing(lastMove.getPreviousPosition()))){
                Position[] capturedData = lastMove.getCaptured();
                for(int i = 0; i < 4; i++){
                    if(capturedData[i] == null){
                        break;
                    }else{
                        boardPieces[capturedData[i].getX()][capturedData[i].getY()] = new Pawn(lastMove.getEnemyPlayer());
                    }
                }
            }

            // since the adding moves to the stack is placed at the move function and also changing the player turn
            // we got to make sure the previous move won't be added again to the stack after being popped,
            // and also make sure this is the turn of the previous player, by changing once
            //for valid move, piece and turn the same, and also return again after the move has been made.
            moveStack.pop();
            this.player2Turn = lastMove.getPlayerTurn();
        }
    }

    //return the size of the board.
    @Override
    public int getBoardSize(){
        return this.BOARD_SIZE;
    }
}
