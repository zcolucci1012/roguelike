

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;

public class Grenade extends GameThing{

  private Chicken chicken;
  private Handler handler;
  private int timer = 0;
  private int range;
  private float damage;
  private float angle;
  private float tempVelX;
  private float tempVelY;

  public Grenade(float x, float y, String id, float angle, float damage, int range, Handler handler){
    super(x, y, id);
    this.handler = handler;
    this.range = range;
    this.damage = damage;
    this.angle = angle;

    width = 12;
    height = 12;
  }

  public void tick(){
    x += velX;
    y += velY;

    if (timer == 0){
      tempVelX = velX;
      tempVelY = velY;
    }
    timer++;
    if (timer == 100){ //explode after 100 ticks
      explode();
    }
    if (velX > 0) {
      velX-=(float)tempVelX/(2f*range);
      if (velX <= 0) velX = 0;
    }
    if (velX < 0) {
      velX-=(float)tempVelX/(2f*range);
      if (velX >= 0) velX = 0;
    }
    if (velY > 0) {
      velY-=(float)tempVelY/(2f*range);
      if (velY <= 0) velY = 0;
    }
    if (velY < 0) {
      velY-=(float)tempVelY/(2f*range);
      if (velY >= 0) velY = 0;
    }

    int f = 4;
    for(int i = 0; i < handler.stuff.size(); i++){
      GameThing thing = handler.stuff.get(i);
      if (thing.getId().equals("Block") || thing.getId().equals("Door") || (thing.getId().length() >= 6 && thing.getId().substring(0,6).equals("Enemy."))){
        if (thing.getBounds().intersects(getBoundsRight())){
          x = thing.getX() - width;
          velX = -velX/f;
          velY = velY/f;
          tempVelX = -tempVelX/f;
          tempVelY = velY/f;
        }
        if (thing.getBounds().intersects(getBoundsLeft())){
          x = thing.getX() + thing.getBounds().width;
          velX = -velX/f;
          velY = velY/f;
          tempVelX = -tempVelX/f;
          tempVelY = velY/f;
        }
        if (thing.getBounds().intersects(getBoundsTop())){
          y = thing.getY() + thing.getBounds().height;
          velY = -velY/f;
          velX = velX/f;
          tempVelY = -tempVelY/f;
          tempVelX = tempVelX/f;
        }
        if (thing.getBounds().intersects(getBoundsBottom())){
          y = thing.getY() - height;
          velY = -velY/f;
          velX = velX/f;
          tempVelY = -tempVelY/f;
          tempVelX = tempVelX/f;
        }
      }
      if (thing.getId().equals("Shot")){
        if(thing.getBounds().intersects(getBounds())){
          explode();
          handler.removeObject(thing);
          break;
        }
      }
      if (thing.getId().equals("Grenade") && thing != this){
        if(thing.getBounds().intersects(getBounds())){
          explode();
          ((Grenade)thing).explode();
          break;
        }
      }
    }

  }

  /*adds an explosion at current location*/
  public void explode(){
    handler.removeObject(this);
    handler.addObject(new Explosion(x+width/2, y+height/2, "Explosion", handler, damage, "Player"));
  }

  public void render(Graphics g){
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.rotate(angle, x + width/2, y + height/2);
    g2d.setColor(new Color(45, 249, 74));
    g2d.fillRect((int)x, (int)y, (int)width, (int)height);
    g2d.dispose();
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
