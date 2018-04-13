package games4jeffpackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Graphics2D;

public class MoveBlock extends GameThing {
  private Handler handler;

  /*
    this class isn't properly implemented
    it is currently deprecated
  */

  public MoveBlock (float x, float y, String id, Handler handler){
    super(x, y, id);
    this.handler = handler;

    width = 32;
    height = 32;
  }

  public void tick(){
    for(int i = 0; i < handler.stuff.size(); i++){
			GameThing thing = handler.stuff.get(i);
      if (thing.getId().equals("Block") || (thing.getId().equals("MoveBlock") && thing != this)){
				if (thing.getBounds().intersects(getBoundsRight())){
					x = thing.getX() - width;
				}
				if (thing.getBounds().intersects(getBoundsLeft())){
					x = thing.getX() + width;
				}
				if (thing.getBounds().intersects(getBoundsTop())){
					y = thing.getY() + height;
				}
				if (thing.getBounds().intersects(getBoundsBottom())){
					y = thing.getY() - height;
				}
			}
    }
  }

  public void render(Graphics g){
    g.setColor(new Color(243,255,189));
    g.fillRect((int)x, (int)y, (int)width, (int)height);
    g.setColor(new Color(0,0,0));
    g.drawRect((int)x, (int)y, (int)width, (int)height);
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
