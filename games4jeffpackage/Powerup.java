package games4jeffpackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Powerup extends Pickup{

  private String name;
  private String itemType;
  private Handler handler;

  public Powerup(float x, float y, String id, Handler handler){
    super(x, y, id);
    this.handler = handler;
    name = this.id.substring(7);
    setItemTypes();
  }

  private void setItemTypes(){
    if (name.equals("health pack")){
      itemType = "single use"; //other types are "active" and "passive"
    }
  }
  public void action(){
    if (name.equals("health pack")){
      for(int i = 0; i < handler.stuff.size(); i++){
  			GameThing thing = handler.stuff.get(i);
        if (thing.getId().equals("Player")){
          ((Player)thing).setHp(((Player)thing).getHp()+20);
        }
      }
    }
  }

  public String getItemType(){
    return itemType;
  }

  public void setItemType(String itemType){
    this.itemType = itemType;
  }

  public void render(Graphics g){
    g.drawImage(tex.powerup[getType()], (int)x, (int)y, null);
  }
}
