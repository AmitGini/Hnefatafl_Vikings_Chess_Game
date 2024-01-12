public class Position {

    //fields represent the rows and columns at the board game
    private final int x, y;

    //Constructor, getting row and col, coordinates on the map.
    public Position(int y, int x){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }
}
