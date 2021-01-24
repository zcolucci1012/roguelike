 

public class Vector
{
    private RoomPoint A;
    private RoomPoint B;

    public Vector(RoomPoint A, RoomPoint B){
        this.A = A;
        this.B = B;
    }
    public RoomPoint getA(){
      return A;
    }
    public RoomPoint getB(){
      return B;
    }

    /*gets the change in x or y between points*/
    public int getDX(){
      return B.getX() - A.getX();
    }
    public int getDY(){
      return B.getY() - A.getY();
    }

    /*returns 1 if point is first in the vector, 2 if second*/
    public int hasPoint(RoomPoint C){
      if (A.isPoint(C)) return 1;
      if (B.isPoint(C)) return 2;
      return -1;
    }
    public int hasPoint(int x, int y){
      if (A.getX() == x && A.getY() == y) return 1;
      if (B.getX() == x && B.getY() == y) return 2;
      return -1;
    }

    /*returns true if vectors have the same start points and end points*/
    public boolean isVector(Vector V){
      if (V.getA().isPoint(A) && V.getB().isPoint(B)){
        return true;
      }
      return false;
    }

    /*get other point in vector*/
    public RoomPoint getOther(RoomPoint C){
      if (A.isPoint(C)) return B;
      if (B.isPoint(C)) return A;
      return new RoomPoint(0, 0);
    }
    public RoomPoint getOther(int x, int y){
      if (A.getX() == x && A.getY() == y) return B;
      if (B.getX() == x && B.getY() == y) return A;
      return new RoomPoint(0, 0);
    }
    
    public String toString(){
        return "(" + A + ", " + B + ")";
    }
}
