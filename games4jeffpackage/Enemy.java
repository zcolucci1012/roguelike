package games4jeffpackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.util.Random;

public abstract class Enemy extends GameThing{

	private Texture tex = Main.getInstance();
	protected float hp;
	private float totalHp;
	protected Handler handler;

  public Enemy(float x, float y, String id, Handler handler, float hp) {
    super(x, y, "Enemy." + id);
		this.hp = hp;
		this.handler = handler;
		totalHp = hp;
  }

	public RoomPoint getRoom(){
		int roomX;
		int roomY;
		if (x < 0) roomX = ((int)x-Main.WIDTH)/Main.WIDTH;
		else roomX = (int)x/Main.WIDTH;
		if (y < 0) roomY = ((int)y-Main.HEIGHT)/Main.HEIGHT;
		else roomY = (int)y/Main.HEIGHT;
		return new RoomPoint (roomX, -roomY);
	}

  public void tick(){
		x += velX;
		y += velY;
		if (hp <= 0){
			handler.removeObject(this);
		}
	}
  public void render(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.BLACK);
		g2d.fillRect((int)x, (int)y-10, (int)width, 5);
		if (hp >= 0){
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
