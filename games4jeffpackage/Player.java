package games4jeffpackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Player extends GameThing{
	private int type;
	private Handler handler;
	private Main main;
	private Screen screen;
	private float hp = 100;
	private float totalHp = hp;
	private int iFrames = 100;
	private int iTimer = 0;
	private boolean invincible = false;
	private boolean restarted = false;

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
		//if not already specified, have these statements in each moving object's class
		x += velX;
		y += velY;

		//iterating through all objects in the handler is often used
		for(int i = 0; i < handler.stuff.size(); i++){
			GameThing thing = handler.stuff.get(i);
			//get the ID of the the thing in the list, check if it's equal to your desired id
			//the following checks for items starting with "Pickup", (ex. "Pickup.pistol")
			//note all pickups (weapons/powerups) are initialized with an ID "Pickup.name"
			//similarly, enemies have an ID of "Enemy.name"
			if (thing.getId().length() >= 6 && thing.getId().substring(0,6).equals("Pickup")){
				if (getBounds().intersects(thing.getBounds())){
					try {
						screen.addWeapon((Weapon)thing); //add weapon to inventory if it's a weapon
						handler.removeObject(thing);
					} catch (Exception e) { //complete powerup action if it's a powerup
						try {
							if (((Powerup)thing).getItemType().equals("single use")){
								((Powerup)thing).action();
							}
							if (((Powerup)thing).getItemType().equals("passive")){
								//to be implemented
							}
							if (((Powerup)thing).getItemType().equals("active")){
								//to be implemented
							}
							screen.notifyPowerup(((Powerup)thing).getName()); //send powerup that is picked up to screen
							handler.removeObject(thing);
						} catch (Exception e2){
							System.out.println("oops something happened");
							e2.printStackTrace();
						}
					}
				}
			}

			//take damage if you hit any of these enemies
			if (thing.getId().equals("Enemy.Chaser") ||
					thing.getId().equals("Enemy.Pouncer") ||
					thing.getId().equals("Enemy.Boss") ||
					thing.getId().equals("Enemy.Bumbler")){
				if (getBounds().intersects(thing.getBounds())){
					if (iTimer == 0) {
						hp-=10;
						iTimer = iFrames; //set the player to an invincible state for a short while
					}
				}
			}

			//controls taking damage on enemy shot hitting player
			if (thing.getId().equals("EnemyShot")){
				if (getBounds().intersects(thing.getBounds())){
					if (iTimer == 0) {
						hp -= ((EnemyShot)thing).getDamage();
						iTimer = iFrames; //set the player to an invincible state for a short while
					}
					handler.removeObject(thing);
				}
			}

			//controls taking damage on enemy tracking shot hitting player
			if (thing.getId().equals("TrackingShot")){
				if (getBounds().intersects(thing.getBounds())){
					if (iTimer == 0) {
						hp -= ((TrackingShot)thing).getDamage();
						iTimer = iFrames; //set the player to an invincible state for a short while
					}
					handler.removeObject(thing);
				}
			}

			//not implemented
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

			//controls block and player collision
			if (thing.getId().equals("Block")){
				/*
					the code below appears to work for most collsions
					however there have been issues in the past, so if not functional, experiment with values
				*/
				if (thing.getBounds().intersects(getBoundsRight())){ //check if the bounds of the object intersects the rightmost bounds of the player
					x = thing.getX() - width; //pushes player back by its own width
        }
				if (thing.getBounds().intersects(getBoundsLeft())){ //check left bounds
					x = thing.getX() + thing.getBounds().width; //pushes player back by width of the object
        }
				if (thing.getBounds().intersects(getBoundsTop())){ //check top bounds
					y = thing.getY() + thing.getBounds().height; //pushes player down by height of the object
				}
				if (thing.getBounds().intersects(getBoundsBottom())){ //check bottom bounds
					y = thing.getY() - height; //pushes player up by its own height
				}
			}

			//controls door and player collision (closed door similar to block)
			if (thing.getId().equals("Door")){
				if (thing.getBounds().intersects(getBounds())){
					if (((Door)thing).getUnlocked()){
						((Door)thing).open(); //open the door if unlocked
					}
					else {
						if (thing.getBounds().intersects(getBoundsRight())){
							x = thing.getX() - width;
		        }
						if (thing.getBounds().intersects(getBoundsLeft())){
							x = thing.getX() + thing.getBounds().width;
		        }
						if (thing.getBounds().intersects(getBoundsTop())){
							y = thing.getY() + thing.getBounds().height;
						}
						if (thing.getBounds().intersects(getBoundsBottom())){
							y = thing.getY() - height;
						}
					}
				}
			}

			//moves to next level when intersecting trapdoor
			if (thing.getId().equals("Trapdoor")){
				if (thing.getBounds().intersects(getBounds()) && !restarted){
					screen.restart(); //restarts screen and main
					main.restart();
					restarted = true;
				}
				else {
					restarted = false;
				}
			}

		}

		//remove player when hp is less than or equal to zero
		if (hp <= 0){
			handler.removeObject(this);
		}

		//controls iFrames
		if (iTimer != 0){
			iTimer--;
			invincible = true;
		}
		else {
			invincible = false;
		}
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g.drawImage(tex.player[type], (int)x, (int)y, null); //draws player with current direction facing
		if (screen.getWeapon() != null) {
			BufferedImage weaponImage = tex.blank_weapon[screen.getWeapon().getType()]; //get image of current weapon
			float angle = screen.getAngle();
			g2d.rotate(angle, x + width/2, y + height/2 + 10); //rotate the gun
			if (angle > Math.PI/2 && angle < 3*Math.PI/2 //flip image if passes the vertical line
				g2d.drawImage(weaponImage, (int)x+10, (int)y+ weaponImage.getHeight()+20, weaponImage.getWidth(), -weaponImage.getHeight(), null);
			}
			else {
				g2d.drawImage(weaponImage, (int)x+10, (int)y+10, null); //draw weapon
			}
			if (type == 3){
				g.drawImage(tex.player[type], (int)x, (int)y, null); //draw the player over the weapon if facing backward
			}
		}
	}

	/*
		the following are the methods for the collision of the Player
		there is a general bounding box for the height and width and for all four cardinal directions
		copying this code into new objects that must have collision will often be sufficient
		if not, mess with the constants until glitching stops
	*/
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

	public float getHp(){
		return hp;
	}

	public void setHp(float hp){
		this.hp = hp;
		if (hp > totalHp) this.hp = totalHp; //doesn't let hp go above max
	}

	public float getTotalHp(){
		return totalHp;
	}

	public int getType(){
		return type;
	}

	public void setType(int type){
		this.type = type;
	}
}
