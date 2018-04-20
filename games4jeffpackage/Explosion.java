 

import java.awt.Graphics;
import java.awt.Rectangle;

public class Explosion extends GameThing{

  /*adds enemy shots going in eight directions*/
  public Explosion(float x, float y, String id, Handler handler, Screen screen, String type){
    super(x, y, id);
    if (type.equals("Enemy")){
        EnemyShot shot1 = new EnemyShot((int)x, (int)y, "EnemyShot", (float)(Math.PI/2), 10, 10, handler);
        shot1.setVelY(-5);
        EnemyShot shot2 = new EnemyShot((int)x, (int)y, "EnemyShot", 0, 10, 10, handler);
        shot2.setVelX(5);
        EnemyShot shot3 = new EnemyShot((int)x, (int)y, "EnemyShot", (float)(3*Math.PI/2), 10, 10, handler);
        shot3.setVelY(5);
        EnemyShot shot4 = new EnemyShot((int)x, (int)y, "EnemyShot", (float)(Math.PI), 10, 10, handler);
        shot4.setVelX(-5);
        handler.addObject(shot1);
        handler.addObject(shot2);
        handler.addObject(shot3);
        handler.addObject(shot4);
        EnemyShot shot5 = new EnemyShot((int)x, (int)y, "EnemyShot", -(float)(Math.PI/4), 10, 10, handler);
        shot5.setVelX((float)(5 * Math.cos(Math.PI/4)));
        shot5.setVelY((float)(-5 * Math.sin(Math.PI/4)));
        EnemyShot shot6 = new EnemyShot((int)x, (int)y, "EnemyShot", -(float)(7*Math.PI/4), 10, 10, handler);
        shot6.setVelX((float)(5 * Math.cos(7*Math.PI/4)));
        shot6.setVelY((float)(-5 * Math.sin(7*Math.PI/4)));
        EnemyShot shot7 = new EnemyShot((int)x, (int)y, "EnemyShot", -(float)(5*Math.PI/4), 10, 10, handler);
        shot7.setVelX((float)(5 * Math.cos(5*Math.PI/4)));
        shot7.setVelY((float)(-5 * Math.sin(5*Math.PI/4)));
        EnemyShot shot8 = new EnemyShot((int)x, (int)y, "EnemyShot", -(float)(3*Math.PI/4), 10, 10, handler);
        shot8.setVelX((float)(5 * Math.cos(3*Math.PI/4)));
        shot8.setVelY((float)(-5 * Math.sin(3*Math.PI/4)));
        handler.addObject(shot5);
        handler.addObject(shot6);
        handler.addObject(shot7);
        handler.addObject(shot8);
        handler.removeObject(this);
    }
    if (type.equals("Player")){
        Shot shot1 = new Shot((int)x, (int)y, "Shot", (float)(Math.PI/2), screen.getDamage(), 10, handler);
        shot1.setVelY(-5);
        Shot shot2 = new Shot((int)x, (int)y, "Shot", 0, screen.getDamage(), 10, handler);
        shot2.setVelX(5);
        Shot shot3 = new Shot((int)x, (int)y, "Shot", (float)(3*Math.PI/2), screen.getDamage(), 10, handler);
        shot3.setVelY(5);
        Shot shot4 = new Shot((int)x, (int)y, "Shot", (float)(Math.PI), screen.getDamage(), 10, handler);
        shot4.setVelX(-5);
        handler.addObject(shot1);
        handler.addObject(shot2);
        handler.addObject(shot3);
        handler.addObject(shot4);
        Shot shot5 = new Shot((int)x, (int)y, "Shot", -(float)(Math.PI/4), screen.getDamage(), 10, handler);
        shot5.setVelX((float)(5 * Math.cos(Math.PI/4)));
        shot5.setVelY((float)(-5 * Math.sin(Math.PI/4)));
        Shot shot6 = new Shot((int)x, (int)y, "Shot", -(float)(7*Math.PI/4), screen.getDamage(), 10, handler);
        shot6.setVelX((float)(5 * Math.cos(7*Math.PI/4)));
        shot6.setVelY((float)(-5 * Math.sin(7*Math.PI/4)));
        Shot shot7 = new Shot((int)x, (int)y, "Shot", -(float)(5*Math.PI/4), screen.getDamage(), 10, handler);
        shot7.setVelX((float)(5 * Math.cos(5*Math.PI/4)));
        shot7.setVelY((float)(-5 * Math.sin(5*Math.PI/4)));
        Shot shot8 = new Shot((int)x, (int)y, "Shot", -(float)(3*Math.PI/4), screen.getDamage(), 10, handler);
        shot8.setVelX((float)(5 * Math.cos(3*Math.PI/4)));
        shot8.setVelY((float)(-5 * Math.sin(3*Math.PI/4)));
        handler.addObject(shot5);
        handler.addObject(shot6);
        handler.addObject(shot7);
        handler.addObject(shot8);
        handler.removeObject(this);
    }
  }

  public void tick(){

  }

  public void render(Graphics g){

  }

  public Rectangle getBounds(){
    return new Rectangle(0, 0, 0, 0);
  }

}
