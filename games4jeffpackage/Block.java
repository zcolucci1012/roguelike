

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;

public class Block extends GameThing {

  private Texture tex = Main.getInstance();
  private int type;
  private int level;

  public Block (float x, float y, String id, int type, int level){
    super(x, y, id);
    this.type = type;
    this.level = level;

    width = 33;
    height = 32;
  }

  public void tick(){

  }

  /*draw block given its randomly generated type and current level*/
  public void render(Graphics g){
    /*
    g.setColor(new Color(178,219,191));
    g.fillRect((int)x, (int)y, (int)width, (int)height);
    g.setColor(new Color(0,0,0));
    g.drawRect((int)x, (int)y, (int)width, (int)height);
    */
    Graphics2D g2d = (Graphics2D)g.create();
    if (level == 1) g.drawImage(tex.block[type], (int)x, (int)y, null);
    else if (level == 2) g.drawImage(tex.block[type+4], (int)x, (int)y, null);
    else if (level == 3) g.drawImage(tex.block[type+8], (int)x, (int)y, null);
    else if (level == 4) {
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
      g2d.drawImage(tex.block[type+12], (int)x, (int)y, null);
    }
    g2d.dispose();
  }

  public Rectangle getBounds(){
    return new Rectangle((int)x, (int)y, (int)width, (int)height);
  }
}
