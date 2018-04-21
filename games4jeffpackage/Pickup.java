

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Pickup extends GameThing{

	protected Texture tex = Main.getInstance();
	protected String name;
  protected boolean pickupable = false;
  private int timer = 0;

	public Pickup(float x, float y, String id) {
		super(x, y, "Pickup." + id); //adds "Pickup." to current ID
		this.name = id;

		width = 24;
		height = 24;

	}

	public void tick() {
    timer++;
    if (timer == 20){
      pickupable = true;
    }
	}

	public abstract void render(Graphics g);

	/*
		return the numerical ID of the weapon or powerup
		used to locate image in texture image list
	*/
	public int getType(){
		switch(name){
		    case "pistol": return 0;
		    case "smg": return 1;
		    case "sniper": return 2;
		    case "assault rifle": return 3;
		    case "DMR": return 4;
		    case "slugshot": return 5;
		    case "minigun": return 6;
		    case "revolver": return 7;
		    case "pump shotgun": return 8;
		    case "tac shotgun": return 9;
		    case "mauler": return 10;

		    case "health pack": return 0;
		    case "damage boost": return 1;
		    case "speed boost": return 2;
		    case "defense boost": return 3;
		    case "fire rate boost": return 4;
		    case "accuracy boost": return 5;

        case "heart": return 0;
        case "chest": return 1;

		    default: return 0;
		  }
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}

  public String getName(){
    return name;
  }

  public boolean isPickupable(){
    return pickupable;
  }
}
