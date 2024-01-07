public class Position {
    private int row, col;
    //Constructor, getting row and col, coordinates on the map.
    public Position(int col, int row){
        this.row = row;
        this.col = col;
    }

    public int getRow(){
        return this.row;
    }

    public int getCol(){
        return this.col;
    }
}
