package games4jeffpackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class TrackingShot extends GameThing{

	private float angle;
	private int damage;
	private int range;
	private Handler handler;
	private int timer = 0;
	private float tempVelX;
	private float tempVelY;
	private float tempVel;
	private float intensity;

	public TrackingShot(float x, float y, String id, float angle, int damage, int range, float intensity, Handler handler) {
		super(x, y, id);
		this.angle = angle;
		this.damage = damage;
		this.range = range;
		this.intensity = intensity;
		this.handler = handler;
		timer = range;

		width = 16;
		height = 8;
	}

	public void tick() {
		if (timer == range) {
			tempVelX = velX;
			tempVelY = velY;
			tempVel = (float)Math.sqrt(Math.pow(tempVelX,2) + Math.pow(tempVelY,2));
		}
		angle = (float)Math.atan(velY / velX);
		x += velX;
		y += velY;
		for (int i=0; i<handler.stuff.size(); i++){
			GameThing thing = handler.stuff.get(i);
			if (getBounds().intersects(thing.getBounds()) && thing.getId().equals("Block")){
				handler.removeObject(this);
			}
			if (thing.getId() == "Player"){
        float pX = thing.getX();
        float pY = thing.getY();
        float d = (float)Math.sqrt(Math.pow((x-(int)pX),2) + Math.pow((y-(int)pY),2));
				if (d > 50) {
					pX = thing.getX() + thing.getWidth()/2;
	        pY = thing.getY() + thing.getHeight()/2;
					d = (float)Math.sqrt(Math.pow((x-(int)pX),2) + Math.pow((y-(int)pY),2));
				}
        if (d != 0){
          velX += (float)(-(x - (int)pX)/d*intensity);
          velY += (float)(-(y - (int)pY)/d*intensity);
					float vel = (float)Math.sqrt(Math.pow(velX,2) + Math.pow(velY,2));
					velX *= (tempVel/vel);
					velY *= (tempVel/vel);
        }
			}
		}
		timer--;
		if (timer == 0) handler.removeObject(this);
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.setColor(new Color(254, 65, 83));
		g2d.rotate(angle, x + width/2, y + height/2);
		g2d.fill(getBounds());
		g2d.dispose();
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}

	public int getDamage(){
		return damage;
	}

}
