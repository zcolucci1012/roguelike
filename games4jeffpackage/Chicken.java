package games4jeffpackage;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;

public class Chicken extends Enemy{

  private int timer = 0;
  private float tempVelX;
  private float tempVelY;
  private float tempVel;
  private float angle;

  public Chicken(float x, float y, String id, Handler handler){
    super(x, y, id, handler, 20);

    angle = (float)(((Math.random()*2)-1)*2*Math.PI);
    tempVelX = 3*(float)(Math.cos((double)angle));
    tempVelY = 3*(float)(Math.sin((double)angle));
    tempVel = (float)Math.sqrt(Math.pow(tempVelX, 2) + Math.pow(tempVelY, 2));
    velX = tempVelX;
    velY = tempVelY;

    width = 24;
    height = 24;

    timer += (int)(Math.random()*100);
  }

  public void tick(){
    super.tick();
    timer++;
    if (timer % 50 == 0){
      angle = (float)(((Math.random()*2)-1)*2*Math.PI);
    }
    if (timer == 150){
      poo();
      timer = 0;
    }

    velX += (float)(Math.cos((double)angle));
    velY += (float)(Math.sin((double)angle));
    float vel = (float)Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2));
    velX *= (tempVel/vel);
    velY *= (tempVel/vel);

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

  public void poo(){
    handler.addObject(new Mine(x+6, y+6, "Mine", handler, this));
  }

  public void render(Graphics g){
    super.render(g);
    if (velX < 0) g.drawImage(tex.enemy[6], (int)x, (int)y, null);
		else g.drawImage(tex.enemy[7], (int)x, (int)y, null);
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
