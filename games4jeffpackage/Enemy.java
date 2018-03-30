package games4jeffpackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.util.Random;

public class Enemy extends GameThing{

  private Handler handler;
  private Screen screen;
  private int hp = 20;
	private int randTimer = 0;
	private int [] imperfections = new int [2];

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
      if (thing.getId() == "Block" || (thing.getId() == "Enemy" && thing != this)){
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
  }

  public void render(Graphics g) {
    g.setColor(new Color(255,22,84));
    g.fillRect((int)x, (int)y, (int)width, (int)height);
    g.setColor(Color.BLACK);
    g.drawString(hp+"", (int)x, (int)y-10);
    g.setColor(Color.BLUE);

    Graphics2D g2d = (Graphics2D)g;

    g2d.draw(getBoundsLeft());
    g2d.draw(getBoundsRight());
    g2d.draw(getBoundsTop());
    g2d.draw(getBoundsBottom());
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
