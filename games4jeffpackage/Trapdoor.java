package games4jeffpackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Graphics2D;

public class Trapdoor extends GameThing {

  private Texture tex = Main.getInstance();

  public Trapdoor (float x, float y, String id){
    super(x, y, id);

    width = 66;
    height = 64;
  }

  public void tick(){

  }

  //right now just renders a square in the ground
  public void render(Graphics g){
    g.setColor(new Color(178,219,191));
    g.fillRect((int)x, (int)y, (int)width, (int)height);
  }

  public Rectangle getBounds(){
    return new Rectangle((int)x, (int)y, (int)width, (int)height);
  }
}
