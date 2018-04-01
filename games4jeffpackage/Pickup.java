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
		if (name.equals("pistol")) g.drawImage(tex.weapon[0], (int)x, (int)y, null);
		if (name.equals("smg")) g.drawImage(tex.weapon[1], (int)x, (int)y, null);
		if (name.equals("sniper")) g.drawImage(tex.weapon[2], (int)x, (int)y, null);
		if (name.equals("assault rifle")) g.drawImage(tex.weapon[3], (int)x, (int)y, null);
		if (name.equals("designated marksman rifle")) g.drawImage(tex.weapon[4], (int)x, (int)y, null);
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}

	private void getTexture(){

	}

}
