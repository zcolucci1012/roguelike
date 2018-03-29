package games4jeffpackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Pickup extends GameThing{

	public Pickup(float x, float y, String id) {
		super(x, y, "Pickup." + id);

		width = 24;
		height = 24;
	}

	public void tick() {

	}

	public void render(Graphics g) {
		g.setColor(new Color(112,193,179));
		g.fillRect((int)x, (int)y, (int)width, (int)height);
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}

}
