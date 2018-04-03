package games4jeffpackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Graphics2D;

public class Player extends GameThing{
	private int type;
	private Handler handler;
	private Main main;
	private Screen screen;
	private int hp = 50;
	private int iFrames = 100;
	private int iTimer = 0;
	private boolean invincible = false;

	private Texture tex = Main.getInstance();

	public Player(float x, float y, String id, int type, Handler handler, Main main, Screen screen) {
		super(x, y, id);
		this.type = type;
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
			if (thing.getId().equals("Enemy.Chaser")){
				if (getBounds().intersects(thing.getBounds())){
					if (iTimer == 0) {
						hp-=10;
						iTimer = iFrames;
					}
				}
			}
			if (thing.getId().equals("EnemyShot")){
				if (getBounds().intersects(thing.getBounds())){
					hp -= ((EnemyShot)thing).getDamage();
					handler.removeObject(thing);
				}
			}
			if (thing.getId().equals("MoveBlock")){
				if (thing.getBounds().intersects(getBoundsRight())){
					thing.setX(x+width);
        }
				if (thing.getBounds().intersects(getBoundsLeft())){
					thing.setX(x-width);
        }
				if (thing.getBounds().intersects(getBoundsTop())){
					thing.setY(y-height);
				}
				if (thing.getBounds().intersects(getBoundsBottom())){
					thing.setY(y+height);
				}
			}
			if (thing.getId().equals("Block")){
				if (thing.getBounds().intersects(getBoundsRight())){
					x = thing.getX() - width;
				}
				if (thing.getBounds().intersects(getBoundsLeft())){
					x = thing.getX() + width;
				}
				if (thing.getBounds().intersects(getBoundsTop())){
					y = thing.getY() + height;
				}
				if (thing.getBounds().intersects(getBoundsBottom())){
					y = thing.getY() - height;
				}
			}
			if (thing.getId().equals("Door")){
				if (thing.getBounds().intersects(getBounds())){
					if (((Door)thing).getUnlocked()){
						((Door)thing).open();
					}
					else {
						if (thing.getBounds().intersects(getBoundsRight())){
							x = thing.getX() - width;
						}
						if (thing.getBounds().intersects(getBoundsLeft())){
							x = thing.getX() + width - 20;
						}
						if (thing.getBounds().intersects(getBoundsTop())){
							y = thing.getY() + height - 20;
						}
						if (thing.getBounds().intersects(getBoundsBottom())){
							y = thing.getY() - height;
						}
					}
				}
			}
			if (hp <= 0){
				handler.removeObject(this);
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
		/*
		g.setColor(new Color(178,219,191));
		if (invincible){
			g.setColor(new Color(208,249,221,75));
		}
		g.fillRect((int)x, (int)y, (int)width, (int)height);
		g.setColor(Color.BLUE);

    Graphics2D g2d = (Graphics2D)g;

    g2d.draw(getBoundsLeft());
    g2d.draw(getBoundsRight());
    g2d.draw(getBoundsTop());
    g2d.draw(getBoundsBottom());
		*/
		g.drawImage(tex.player[type], (int)x, (int)y, null);
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}

	public Rectangle getBoundsLeft(){
    return new Rectangle((int)x, (int)y+5, (int)5, (int)height-10);
  }

  public Rectangle getBoundsRight(){
    return new Rectangle((int)x+(int)width-5, (int)y+5, (int)5, (int)height-10);
  }

  public Rectangle getBoundsTop(){
    return new Rectangle((int)x+(int)width/4, (int)y, (int)width/2, (int)height/2);
  }

  public Rectangle getBoundsBottom(){
    return new Rectangle((int)x+(int)width/4, (int)y + (int)height/2, (int)width/2, (int)height/2);
  }

	public int getHp(){
		return hp;
	}

	public int getType(){
		return type;
	}

	public void setType(int type){
		this.type = type;
	}
}
