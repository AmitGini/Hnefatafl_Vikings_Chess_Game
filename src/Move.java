public class Move {

    private final Position CURRENT_POSITION;
    private final Position PREVIOUS_POSITION;
    private final boolean IS_PLAYER_TWO_TURN;
    private final Player ENEMY_PLAYER;

    private Position[] captured;


    public Move(Position current, Position previous, boolean player2Turn){
        this.CURRENT_POSITION = new Position(current); //position after the move
        this.PREVIOUS_POSITION = new Position(previous); //position after the change
        this.IS_PLAYER_TWO_TURN = player2Turn; //check player turn.
        this.ENEMY_PLAYER = new ConcretePlayer(!(IS_PLAYER_TWO_TURN));
        this.captured = new Position[4];
    }

    public Position getCurrentPosition(){ return this.CURRENT_POSITION;}

    public Position getPreviousPosition(){ return this.PREVIOUS_POSITION;}

    public boolean getPlayerTurn(){ return this.IS_PLAYER_TWO_TURN;}

    public Position[] getCaptured() {
        return this.captured;
    }

    public Player getEnemyPlayer(){
        return this.ENEMY_PLAYER;
    }

    //cases(clockwise)
    // i = 0: captured at step y+1,
    // i = 1 captured at step x+1
    // i = 2 captured at step y-1
    // i = 3 captured at step x-1
    public void setCapture(Position pos,int x, int y){
        for (int i = 0; i < 4; i++){
            if(this.captured[i] == null){
                this.captured[i] = new Position(pos, x, y);
                break;
            }
        }
    }


}
