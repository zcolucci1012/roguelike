import java.awt.Graphics;

public class CoreItem extends Pickup {

  public CoreItem(float x, float y, String id){
    super(x, y, id);
  }

  public void render(Graphics g){
    g.drawImage(tex.core_item[getType()], (int)x, (int)y, null);
  }
}
