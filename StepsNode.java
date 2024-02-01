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
