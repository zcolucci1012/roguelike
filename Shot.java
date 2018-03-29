package games4jeffpackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Shot extends GameThing{

	private int damage;
	
	public Shot(float x, float y, String id, int damage) {
		super(x, y, id);
		this.damage = damage;
		
		width = 16;
		height = 16;
	}

	public void tick() {
		x += velX;
		y += velY;
	}

	public void render(Graphics g) {
		g.setColor(new Color(243,255,189));
		g.fillOval((int)x, (int)y, (int)width, (int)height);
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, (int)width, (int)height); 
	}
	
	
	
}
