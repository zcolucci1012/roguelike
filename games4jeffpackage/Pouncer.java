package games4jeffpackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.util.Random;

public class Pouncer extends Enemy{

  private Handler handler;
  private Screen screen;
	private int randTimer = 0;
	private int [] imperfections = new int [2];
  private int timer = 0;

	private Texture tex = Main.getInstance();

  public Pouncer(float x, float y, String id, Handler handler, Screen screen) {
    super(x, y, id, 15);
    this.handler = handler;
    this.screen = screen;
    timer = (int)(Math.random()*11) + 1;

    width = 28;
    height = 28;
  }

  public void tick() {
    x+=velX;
    y+=velY;
    double max = 7.0;
    float speed = (float)Math.pow((-(Math.pow(max, 1.0/10.0)/2.0*Math.cos(Math.PI*timer/25.0)) + Math.pow(max, 1.0/10.0)/2.0), 10.0) + 1;
		if (randTimer == 50){
			imperfections[0] = (int)(Math.random() * 51)-25;
			imperfections[1] = (int)(Math.random() * 51)-25;
			randTimer = 0;
		}
    for(int i = 0; i < handler.stuff.size(); i++){
      GameThing thing = handler.stuff.get(i);
      if (thing.getId() == "Player"){
        float pX = thing.getX();
        float pY = thing.getY();
        float d = (float)Math.sqrt(Math.pow((x-(int)pX),2) + Math.pow((y-(int)pY),2));
				if (d > 50) {
					pX = thing.getX() + thing.getWidth()/2 + imperfections[0];
	        pY = thing.getY() + thing.getHeight()/2 + imperfections[1];
					d = (float)Math.sqrt(Math.pow((x-(int)pX),2) + Math.pow((y-(int)pY),2));
				}
        if (d != 0){
          velX = -(x - (int)pX)/d*speed;
          velY = -(y - (int)pY)/d*speed;
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
      if (thing.getId() == "Block" || (thing.getId().length() >= 6 && thing.getId().substring(0,6).equals("Enemy.") && thing != this)){
        if (thing.getBounds().intersects(getBoundsRight())){
					x = thing.getX() - thing.getWidth();
        }
				if (thing.getBounds().intersects(getBoundsLeft())){
					x = thing.getX() + thing.getWidth();
        }
				if (thing.getBounds().intersects(getBoundsTop())){
					y = thing.getY() + thing.getHeight();
				}
				if (thing.getBounds().intersects(getBoundsBottom())){
					y = thing.getY() - thing.getHeight();
				}
      }
    }
		randTimer++;
    timer++;
  }

  public void render(Graphics g) {
		super.render(g);
    g.setColor(Color.GRAY);
    g.fillRect((int)x, (int)y, (int)width, (int)height);
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
}
