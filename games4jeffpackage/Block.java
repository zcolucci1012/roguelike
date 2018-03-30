package games4jeffpackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Graphics2D;

public class Block extends GameThing {
  public Block (float x, float y, String id){
    super(x, y, id);

    width = 32;
    height = 32;
  }

  public void tick(){

  }

  public void render(Graphics g){
    g.setColor(new Color(178,219,191));
    g.fillRect((int)x, (int)y, (int)width, (int)height);
    g.setColor(new Color(0,0,0));
    g.drawRect((int)x, (int)y, (int)width, (int)height);
  }

  public Rectangle getBounds(){
    return new Rectangle((int)x, (int)y, (int)width, (int)height);
  }
}
