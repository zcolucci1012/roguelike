package games4jeffpackage;

public class Camera {
  private float x, y;

  public Camera(float x, float y){
    this.x = x;
    this.y = y;
  }

  public void tick(GameThing player){
    int posX = (int)(player.getX() + player.getWidth()/2);
    int posY = (int)(player.getY() + player.getHeight());
    if (posX > 0) x = -(posX/Main.WIDTH)*Main.WIDTH;
    else x = -((posX-Main.WIDTH)/Main.WIDTH)*Main.WIDTH;
    if (posY > 0) y = -(posY/Main.HEIGHT)*Main.HEIGHT;
    else y = -((posY-Main.HEIGHT)/Main.HEIGHT)*Main.HEIGHT;
  }

  public void setX(float x){
    this.x = x;
  }

  public void setY(float y){
    this.y = y;
  }

  public float getX(){
    return x;
  }

  public float getY(){
    return y;
  }

}
