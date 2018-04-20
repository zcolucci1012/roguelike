

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Color;

public class Bumbler extends Enemy{

  private int timer = 0;
  private float tempVelX;
  private float tempVelY;
  private float tempVel;
  private float angle;
  private Screen screen;

  public Bumbler(float x, float y, String id, Handler handler, Screen screen){
    super(x, y, id, handler, 25);
    this.screen = screen;

    //get initial velocity and random angle and set speed
    angle = (float)(((Math.random()*2)-1)*2*Math.PI);
    tempVelX = (float)(Math.cos((double)angle));
    tempVelY = (float)(Math.sin((double)angle));
    tempVel = (float)Math.sqrt(Math.pow(tempVelX, 2) + Math.pow(tempVelY, 2));
    velX = tempVelX; //set velocity
    velY = tempVelY;

    width = 28;
    height = 28;

    timer += (int)(Math.random()*50); //random start point of timer
  }

  public void tick(){
    super.tick();
    timer++;
    if (timer % 20 == 0){
      angle = (float)(((Math.random()*2)-1)*2*Math.PI); //random angle
    }
    if (timer == 100){
      int shot = (int)(Math.random()*2); //randomly chooses between cardinal or diagonal shots
      if (shot == 0){
        EnemyShot shot1 = new EnemyShot((int)x + width/2, (int)y, "EnemyShot", (float)(Math.PI/2), 10, 20, handler);
  			shot1.setVelY(-5);
        EnemyShot shot2 = new EnemyShot((int)x + width, (int)y + height/2, "EnemyShot", 0, 10, 20, handler);
  			shot2.setVelX(5);
        EnemyShot shot3 = new EnemyShot((int)x + width/2, (int)y + height, "EnemyShot", (float)(3*Math.PI/2), 10, 20, handler);
  			shot3.setVelY(5);
        EnemyShot shot4 = new EnemyShot((int)x, (int)y + height/2, "EnemyShot", (float)(Math.PI), 10, 20, handler);
  			shot4.setVelX(-5);
        handler.addObject(shot1);
        handler.addObject(shot2);
        handler.addObject(shot3);
        handler.addObject(shot4);
      }
      else {
        EnemyShot shot5 = new EnemyShot((int)x+width, (int)y, "EnemyShot", -(float)(Math.PI/4), 10, 20, handler);
  			shot5.setVelX((float)(5 * Math.cos(Math.PI/4)));
        shot5.setVelY((float)(-5 * Math.sin(Math.PI/4)));
        EnemyShot shot6 = new EnemyShot((int)x + width, (int)y + height, "EnemyShot", -(float)(7*Math.PI/4), 10, 20, handler);
        shot6.setVelX((float)(5 * Math.cos(7*Math.PI/4)));
        shot6.setVelY((float)(-5 * Math.sin(7*Math.PI/4)));
        EnemyShot shot7 = new EnemyShot((int)x, (int)y + height, "EnemyShot", -(float)(5*Math.PI/4), 10, 20, handler);
        shot7.setVelX((float)(5 * Math.cos(5*Math.PI/4)));
        shot7.setVelY((float)(-5 * Math.sin(5*Math.PI/4)));
        EnemyShot shot8 = new EnemyShot((int)x, (int)y, "EnemyShot", -(float)(3*Math.PI/4), 10, 20, handler);
        shot8.setVelX((float)(5 * Math.cos(3*Math.PI/4)));
        shot8.setVelY((float)(-5 * Math.sin(3*Math.PI/4)));
        handler.addObject(shot5);
        handler.addObject(shot6);
        handler.addObject(shot7);
        handler.addObject(shot8);
      }
      timer = 0;
    }
    //add to velocity
    velX += (float)(5*Math.cos((double)angle));
    velY += (float)(5*Math.sin((double)angle));
    float vel = (float)Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2));
    //normalize vector
    velX *= (tempVel/vel);
    velY *= (tempVel/vel);

    //collision
    for(int i = 0; i < handler.stuff.size(); i++){
      GameThing thing = handler.stuff.get(i);
      if (thing.getId().equals("Block") || thing.getId().equals("Door") || (thing.getId().length() >= 6 && thing.getId().substring(0,6).equals("Enemy.") && thing != this)){
        if (thing.getBounds().intersects(getBoundsRight())){
          x = thing.getX() - width;
        }
        if (thing.getBounds().intersects(getBoundsLeft())){
          x = thing.getX() + thing.getBounds().width;
        }
        if (thing.getBounds().intersects(getBoundsTop())){
          y = thing.getY() + thing.getBounds().height;
        }
        if (thing.getBounds().intersects(getBoundsBottom())){
          y = thing.getY() - height;
        }
      }
    }
  }

  public void render(Graphics g){
    super.render(g);
    g.setColor(new Color(51, 190, 255));
    g.fillRect((int)x, (int)y, (int)width, (int)height);
  }

  public Rectangle getBounds(){
    return new Rectangle((int)x, (int)y, (int)width, (int)height);
  }

  public Rectangle getBoundsLeft(){
    return new Rectangle((int)x, (int)y+5, (int)5, (int)height-10);
  }

  public Rectangle getBoundsRight(){
    return new Rectangle((int)x+(int)width-5, (int)y+5, (int)5, (int)height-10);
  }

  public Rectangle getBoundsTop(){
    return new Rectangle((int)x+(int)width/4, (int)y, (int)width/2, (int)height/2);
  }

  public Rectangle getBoundsBottom(){
    return new Rectangle((int)x+(int)width/4, (int)y + (int)height/2, (int)width/2, (int)height/2);
  }

}
