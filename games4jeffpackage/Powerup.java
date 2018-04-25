

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Powerup extends Pickup{

  private String itemType;
  private Handler handler;
  private Screen screen;

  public Powerup(float x, float y, String id, Handler handler, Screen screen){
    super(x, y, id);
    this.handler = handler;
    this.screen = screen;
    setItemTypes();
  }

  /*defines the type of powerup by name*/
  private void setItemTypes(){
      switch(name){
            case "health pack": itemType = "single use"; //other types are "active" and "passive"
            case "damage boost": itemType = "single use";
            case "speed boost": itemType = "single use";
            case "defense boost": itemType = "single use";
            case "fire rate boost": itemType = "single use";
            case "accuracy boost": itemType = "single use";
      }
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
    switch(name){
        case "health pack": //adds 20 to current hp
            for(int i = 0; i < handler.stuff.size(); i++){
                GameThing thing = handler.stuff.get(i);
                if (thing.getId().equals("Player")){
                    ((Player)thing).setHp(((Player)thing).getHp()+20);
                }
            }
        case "damage boost": screen.setDamageMod(screen.getDamageMod()*1.2f); //boosts damage by 20%
        case "speed boost": screen.setSpeedMod(screen.getSpeedMod()*1.1f); //boosts speed by 10%
        case "defense boost": screen.setDefenseMod(screen.getDefenseMod()*1.2f); //boosts defense by 20%
        case "fire rate boost": screen.setFireRateMod(screen.getFireRateMod()*1.2f);//boosts fire rate by 20%
        case "accuracy boost": screen.setAccuracyMod(screen.getAccuracyMod()*1.2f); //boosts accuracy by 20%
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
