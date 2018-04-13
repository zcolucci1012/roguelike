 

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class EnemyShot extends GameThing{

	private float angle;
	private float damage;
	private Handler handler;
	private int timer = 0;

	public EnemyShot(float x, float y, String id, float angle, float damage, int range, Handler handler) {
		super(x, y, id);
		this.angle = angle;
		this.damage = damage;
		this.handler = handler;
		timer = range;

		width = 16;
		height = 8;
	}

	public void tick() {
		x += velX;
		y += velY;
		for (int i=0; i<handler.stuff.size(); i++){
			GameThing thing = handler.stuff.get(i);
			if (getBounds().intersects(thing.getBounds()) && thing.getId().equals("Block")){
				handler.removeObject(this); //remove if block is hit
			}
		}
		timer--;
		if (timer == 0) handler.removeObject(this); //remove after range timer expended
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.setColor(Color.RED);
		g2d.rotate(angle, x + width/2, y + height/2); //set angle of render
		g2d.fill(getBounds());
		g2d.dispose();
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}

	public float getDamage(){
		return damage;
	}

}
