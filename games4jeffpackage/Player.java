package games4jeffpackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Player extends GameThing{
	private Handler handler;
	private Main main;
	private Screen screen;
	private int hp = 100;
	private int iFrames = 50;
	private int iTimer = 0;
	private boolean invincible = false;

	public Player(float x, float y, String id, Handler handler, Main main, Screen screen) {
		super(x, y, id);
		this.handler = handler;
		this.main = main;
		this.screen = screen;

		width = 32;
		height = 32;
	}

	public void tick() {
		x += velX;
		y += velY;

		for(int i = 0; i < handler.stuff.size(); i++){
			GameThing thing = handler.stuff.get(i);
			if (thing.getId().length() >= 6 && thing.getId().substring(0,6).equals("Pickup")){
				if (getBounds().intersects(thing.getBounds())){
					screen.addWeapon((Weapon)thing);
					handler.removeObject(thing);
				}
			}
			if (thing.getId().equals("Enemy")){
				if (getBounds().intersects(thing.getBounds())){
					if (iTimer == 0) {
						hp-=10;
						System.out.println(hp);
						iTimer = iFrames;
					}
					if (hp == 0){
						//game over
					}

				}
			}
		}
		if (iTimer != 0){
			iTimer--;
			invincible = true;
		}
		else {
			invincible = false;
		}

	}

	public void render(Graphics g) {
		g.setColor(new Color(178,219,191));
		if (invincible){
			g.setColor(new Color(208,249,221,75));
		}
		g.fillRect((int)x, (int)y, (int)width, (int)height);
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}

}
