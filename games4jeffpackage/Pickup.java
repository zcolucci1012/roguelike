package games4jeffpackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Pickup extends GameThing{

	private Texture tex = Main.getInstance();
	private String name;

	public Pickup(float x, float y, String id) {
		super(x, y, "Pickup." + id);
		this.name = id;

		width = 24;
		height = 24;

		getTexture();
	}

	public void tick() {

	}

	public void render(Graphics g) {
		g.drawImage(tex.weapon[getType()], (int)x, (int)y, null);
	}

	public int getType(){
		if (name.equals("pistol")) return 0;
		if (name.equals("smg")) return 1;
		if (name.equals("sniper")) return 2;
		if (name.equals("assault rifle")) return 3;
		if (name.equals("DMR")) return 4;
		return -1;
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}

	private void getTexture(){

	}

}
