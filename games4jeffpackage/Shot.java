package games4jeffpackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Shot extends GameThing{

	private float angle;
	private float damage;
	private Handler handler;
	private int timer = 0;

	public Shot(float x, float y, String id, float angle, float damage, int range, Handler handler) {
		super(x, y, id);
		this.angle = angle;
		this.damage = damage;
		this.handler = handler;
		timer = range;

		width = 8;
		height = 4;
	}

	public void tick() {
		x += velX;
		y += velY;
		for (int i=0; i<handler.stuff.size(); i++){
			GameThing thing = handler.stuff.get(i);
			if (getBounds().intersects(thing.getBounds()) && thing.getId().equals("Block")){
				handler.removeObject(this);
			}
			if (thing.getId().length() >= 6 && thing.getId().substring(0,6).equals("Enemy.")){
				if (thing.getBounds().intersects(getBounds())){
					((Enemy)thing).setHp(((Enemy)thing).getHp()-damage);
					handler.removeObject(this);
				}
			}
		}
		timer--;
		if (timer == 0) handler.removeObject(this);
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.rotate(angle, x + width/2, y + height/2);
		g2d.setColor(Color.BLACK);
		g2d.fill(getBounds());
		g2d.setColor(Color.WHITE);
		g2d.draw(getBounds());
		g2d.dispose();
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}

	public float getDamage(){
		return damage;
	}

}
