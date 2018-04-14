 

public class RoomPoint
{
    private int x;
    private int y;
    private boolean complete = false; //used by other classes

    public RoomPoint(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

    /*returns true if x and y values are the same as entered point*/
    public boolean isPoint(RoomPoint A){
        if (this.x == A.getX() && this.y == A.getY()){
          return true;
        }
        return false;
    }

    /*returns true if x and y values are the same as values entered*/
    public boolean isPoint(int x, int y){
      if (this.x == x && this.y == y){
        return true;
      }
      return false;
    }

    public void complete(){
      complete = true;
    }
    public boolean isComplete(){
      return complete;
    }

    public String toString(){
        return "[" + x + ", " + y + "]";
    }
}
