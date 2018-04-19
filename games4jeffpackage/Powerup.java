 

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Powerup extends Pickup{

  private String name;
  private String itemType;
  private Handler handler;
  private Screen screen;

  public Powerup(float x, float y, String id, Handler handler, Screen screen){
    super(x, y, id);
    this.handler = handler;
    this.screen = screen;
    name = this.id.substring(7); //gets rid of "Pickup." like in Weapon
    setItemTypes();
  }

  /*defines the type of powerup by name*/
  private void setItemTypes(){
    if (name.equals("health pack")) itemType = "single use"; //other types are "active" and "passive"
    if (name.equals("damage boost")) itemType = "single use";
    if (name.equals("speed boost")) itemType = "single use";
    if (name.equals("defense boost")) itemType = "single use";
    if (name.equals("fire rate boost")) itemType = "single use";
    if (name.equals("accuracy boost")) itemType = "single use";
  }

  /*
    the action that will be performed with the powerup
    dependent on the item type, this method will either be called
    single use: one time when picked up
    passive: every tick after picked up
    active: when user activates item
    *note* passive and active items have not been implemented yet
  */
  public void action(){
    if (name.equals("health pack")){ //adds 20 to current hp
      for(int i = 0; i < handler.stuff.size(); i++){
  			GameThing thing = handler.stuff.get(i);
        if (thing.getId().equals("Player")){
          ((Player)thing).setHp(((Player)thing).getHp()+20);
        }
      }
    }
    if (name.equals("damage boost")){ //boosts damage by 20%
      screen.setDamageMod(screen.getDamageMod()*1.2f);
    }
    if (name.equals("speed boost")){ //boosts speed by 10%
      screen.setSpeedMod(screen.getSpeedMod()*1.1f);
    }
    if (name.equals("defense boost")){ //boosts defense by 20%
      screen.setDefenseMod(screen.getDefenseMod()*1.2f);
    }
    if (name.equals("fire rate boost")){ //boosts fire rate by 20%
      screen.setFireRateMod(screen.getFireRateMod()*1.2f);
    }
    if (name.equals("accuracy boost")){ //boosts accuracy by 20%
      screen.setAccuracyMod(screen.getAccuracyMod()*1000.2f);
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

  public String getName(){
    return name;
  }
}
