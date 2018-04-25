 

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.util.Random;

public abstract class Enemy extends GameThing{

	protected Texture tex = Main.getInstance();
	protected float hp;
	private float totalHp;
	protected Handler handler;

  public Enemy(float x, float y, String id, Handler handler, float hp) {
    super(x, y, "Enemy." + id); //adds "Enemy." to current ID
		this.hp = hp;
		this.handler = handler;
		totalHp = hp;
  }

	/*returns the current room the enemy is in*/
	public RoomPoint getRoom(){
		int roomX;
		int roomY;
		if (x < 0) roomX = ((int)x-Main.WIDTH)/Main.WIDTH;
		else roomX = (int)x/Main.WIDTH;
		if (y < 0) roomY = ((int)y-Main.HEIGHT)/Main.HEIGHT;
		else roomY = (int)y/Main.HEIGHT;
		return new RoomPoint (roomX, -roomY);
	}

	/*sets enemy's movement and allows death*/
  public void tick(){
		x += velX;
		y += velY;
		if (hp <= 0){
			handler.removeObject(this); //kill if no hp
		}
	}

	/*renders the enemy's health bar*/
  public void render(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.BLACK);
		g2d.fillRect((int)x, (int)y-10, (int)width, 5);
		if (hp >= 0){ //sets color based on health, changes length based on health
			g2d.setColor(new Color((int)(255-hp*(255.0/totalHp)), (int)(hp*(255.0/totalHp)), 0));
			g2d.fillRect((int)x, (int)y-10, (int)(hp*(width/totalHp)), 5);
		}
		g2d.setColor(Color.BLACK);
		g2d.drawRect((int)x, (int)y-10, (int)width, 5);
	}
  public abstract Rectangle getBounds();

	public void setHp(float hp){
		this.hp = hp;
	}
	public float getHp(){
		return hp;
	}
}
