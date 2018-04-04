package games4jeffpackage;

public class RoomPoint
{
    private int x;
    private int y;
    private boolean complete = false;

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
    public boolean isPoint(RoomPoint A){
        if (this.x == A.getX() && this.y == A.getY()){
          return true;
        }
        return false;
    }
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
