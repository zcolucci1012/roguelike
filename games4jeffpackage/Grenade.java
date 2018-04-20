 

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;

public class Grenade extends GameThing{

  private Chicken chicken;
  private Handler handler;
  private Screen screen;
  private int timer = 0;

  public Grenade(float x, float y, String id, Handler handler, Screen screen){
    super(x, y, id);
    this.handler = handler;
    this.screen = screen;

    width = 8;
    height = 8;
  }

  public void tick(){
    x += velX;
    y += velY;
    timer++;
    if (timer == 150){ //explode after 150 ticks
      explode();
    }
  }

  /*adds an explosion at current location*/
  public void explode(){
    handler.addObject(new Explosion(x+width/2, y+height/2, "Explosion", handler, "Player", screen));
    handler.removeObject(this);
  }

  public void render(Graphics g){
    g.setColor(new Color(45, 249, 74));
    g.fillRect((int)x, (int)y, (int)width, (int)height);
  }

  public Rectangle getBounds(){
    return new Rectangle((int)x, (int)y, (int)width, (int)height);
  }

}