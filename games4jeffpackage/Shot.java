package games4jeffpackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Shot extends GameThing{

	private int damage;
	private int timer = 0;
	private Handler handler;

	public Shot(float x, float y, String id, int damage, Handler handler) {
		super(x, y, id);
		this.damage = damage;
		this.handler = handler;

		width = 16;
		height = 16;
	}

	public void tick() {
		x += velX;
		y += velY;
		for (int i=0; i<handler.stuff.size(); i++){
			GameThing thing = handler.stuff.get(i);
			if (getBounds().intersects(thing.getBounds()) && thing.getId().equals("Block")){
				handler.removeObject(this);
			}
		}
	}

	public void render(Graphics g) {
		g.setColor(new Color(243,255,189));
		g.fillOval((int)x, (int)y, (int)width, (int)height);
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}

	public int getDamage(){
		return damage;
	}

}
