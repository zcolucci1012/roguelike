package games4jeffpackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Enemy extends GameThing{

	private Handler handler;
	private Screen screen;
	private int hp = 20;

	public Enemy(float x, float y, String id, Handler handler, Screen screen) {
		super(x, y, id);
		this.handler = handler;
		this.screen = screen;

		width = 28;
		height = 28;
	}

	public void tick() {
		x+=velX;
		y+=velY;
		for(int i = 0; i < handler.stuff.size(); i++){
			GameThing thing = handler.stuff.get(i);
			if (thing.getId() == "Player"){
				float pX = thing.getX() + thing.getWidth()/2;
				float pY = thing.getY() + thing.getHeight()/2;
				float d = (float)Math.sqrt(Math.pow((x-(int)pX),2) + Math.pow((y-(int)pY),2));
				if (d != 0){
					velX = -(x - (int)pX)/d*2;
					velY = -(y - (int)pY)/d*2;
				}
			}
			if (thing.getId() == "Shot"){
				if (thing.getBounds().intersects(getBounds())){
					hp-=screen.getWeapon().getDamage();
					handler.removeObject(thing);
					if (hp <= 0){
						handler.removeObject(this);
					}
				}
			}
		}
	}

	public void render(Graphics g) {
		g.setColor(new Color(255,22,84));
		g.fillRect((int)x, (int)y, (int)width, (int)height);
		g.setColor(Color.BLACK);
		g.drawString(hp+"", (int)x, (int)y-10);
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}

}
