 

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.util.Random;

public class Chaser extends Enemy{

  private Screen screen;
	private int randTimer = 0;
	private int [] imperfections = new int [2];

  public Chaser(float x, float y, String id, Handler handler, Screen screen) {
    super(x, y, id, handler, 20);
    this.screen = screen;

    width = 28;
    height = 28;
  }

  public void tick() {
    super.tick();
		if (randTimer == 50){
      //makes AI movement imperfect by altering target every 50 ticks
			imperfections[0] = (int)(Math.random() * 51)-25;
			imperfections[1] = (int)(Math.random() * 51)-25;
			randTimer = 0;
		}
    for(int i = 0; i < handler.stuff.size(); i++){
      GameThing thing = handler.stuff.get(i);
      if (thing.getId() == "Player"){ //get distance between player and enemy
        float pX = thing.getX();
        float pY = thing.getY();
        float d = (float)Math.sqrt(Math.pow((x-(int)pX),2) + Math.pow((y-(int)pY),2));
				if (d > 50) { //if enemy is close enough, ignore imperfections in movement
					pX = thing.getX() + thing.getWidth()/2 + imperfections[0];
	        pY = thing.getY() + thing.getHeight()/2 + imperfections[1];
					d = (float)Math.sqrt(Math.pow((x-(int)pX),2) + Math.pow((y-(int)pY),2)); //get new distance
				}
        if (d != 0){ //set velocity, 2 represents overall speed
          velX = -(x - (int)pX)/d*2;
          velY = -(y - (int)pY)/d*2;
        }
      }

      //collision with blocks, doors, other enemies
      if (thing.getId().equals("Block") || thing.getId().equals("Door") || (thing.getId().length() >= 6 && thing.getId().substring(0,6).equals("Enemy.") && thing != this)){
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
		randTimer++;
  }

  /* draw different texture based off direction */
  public void render(Graphics g) {
		super.render(g);
		if (velX > 0) g.drawImage(tex.enemy[0], (int)x, (int)y, null);
		else g.drawImage(tex.enemy[1], (int)x, (int)y, null);
  }

  //collision bounds
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
