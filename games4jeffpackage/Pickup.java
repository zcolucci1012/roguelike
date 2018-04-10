package games4jeffpackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Pickup extends GameThing{

	protected Texture tex = Main.getInstance();
	private String name;

	public Pickup(float x, float y, String id) {
		super(x, y, "Pickup." + id);
		this.name = id;

		width = 24;
		height = 24;

	}

	public void tick() {

	}

	public abstract void render(Graphics g);

	public int getType(){
		if (name.equals("pistol")) return 0;
		if (name.equals("smg")) return 1;
		if (name.equals("sniper")) return 2;
		if (name.equals("assault rifle")) return 3;
		if (name.equals("DMR")) return 4;
		if (name.equals("slugshot")) return 5;
		if (name.equals("minigun")) return 6;
		if (name.equals("revolver")) return 7;
		if (name.equals("pump shotgun")) return 8;
		if (name.equals("tac shotgun")) return 9;
		if (name.equals("mauler")) return 10;

		if (name.equals("health pack")) return 0;
		if (name.equals("damage boost")) return 1;
		return 0;
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}
}
