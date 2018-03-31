package games4jeffpackage;

public class Vector
{
    private Point A;
    private Point B;

    public Vector(Point A, Point B){
        this.A = A;
        this.B = B;
    }
    public int getDX(){
      return B.getX() - A.getX();
    }
    public int getDY(){
      return B.getY() - A.getY();
    }
    public int hasPoint(Point C){
      if (A.getX() == C.getX() && A.getY() == C.getY()) return 1;
      if (B.getX() == C.getX() && B.getY() == C.getY()) return 2;
      return -1;
    }
    public String toString(){
        return "(" + A + ", " + B + ")";
    }
}