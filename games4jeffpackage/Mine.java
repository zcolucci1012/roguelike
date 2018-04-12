package games4jeffpackage;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;

public class Mine extends GameThing{

  private Chicken chicken;
  private Handler handler;
  private int timer = 0;

  public Mine(float x, float y, String id, Handler handler, Chicken chicken){
    super(x, y, id);
    this.chicken = chicken;
    this.handler = handler;

    width = 12;
    height = 12;
  }

  public void tick(){
    timer++;
    if (timer == 250){
      explode();
    }
  }

  public void explode(){
    handler.addObject(new Explosion(x+width/2, y+height/2, "Explosion", handler));
    handler.removeObject(this);
  }

  public void render(Graphics g){
    g.setColor(new Color(67, 119, 139));
    g.fillRect((int)x, (int)y, (int)width, (int)height);
    if (handler.stuff.contains(chicken)){
      chicken.render(g);
    }
  }

  public Rectangle getBounds(){
    return new Rectangle((int)x, (int)y, (int)width, (int)height);
  }

}
