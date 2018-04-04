package games4jeffpackage;

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
    public int getDX(){
      return B.getX() - A.getX();
    }
    public int getDY(){
      return B.getY() - A.getY();
    }
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
    public boolean isVector(Vector V){
      if (V.getA().isPoint(A) && V.getB().isPoint(B)){
        return true;
      }
      return false;
    }
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
