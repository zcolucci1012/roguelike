package games4jeffpackage;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Color;

public class TrackingShooter extends Enemy{

  private Screen screen;
  private float sx;
  private float sy;
  private int timer = 100;

  public TrackingShooter (float x, float y, String id, Handler handler, Screen screen){
    super(x, y, id, handler, 30);
    this.screen = screen;
    timer = (int)(Math.random()*151) + 1;

    width = 24;
    height = 24;
  }

  public void tick(){
    super.tick();
    if (timer != 0){
      timer--;
      if (timer == 0){
        fire();
        timer = 100;
      }
    }
  }

  public void render(Graphics g){
    super.render(g);
    g.setColor(new Color(4, 55, 7));
    g.fillRect((int)x, (int)y, (int)width, (int)height);
  }

  public Rectangle getBounds(){
    return new Rectangle((int)x, (int)y, (int)width, (int)height);
  }

  private void fire(){
    for(int i = 0; i < handler.stuff.size(); i++){
      GameThing thing = handler.stuff.get(i);
      if (thing.getId() == "Player"){
        sx = thing.getX() + thing.getWidth()/2;
        sy = thing.getY() + thing.getHeight()/2;
      }
    }
		float xx = x + width/2 ;
		float yy = y + height/2;
		float d = (float)Math.sqrt(Math.pow((sx-(int)xx),2) + Math.pow((sy-(int)yy),2));
		if (d != 0){
			float sVelX = ((sx - (int)xx)/d*5);
			float sVelY = ((sy - (int)yy)/d*5);
			float angle = (float)Math.atan(sVelY / sVelX);
			TrackingShot shot = new TrackingShot((int)xx-width/4, (int)yy-width/4, "TrackingShot", angle, 10, 100, 0.15f, handler);
			shot.setVelX(sVelX);
			shot.setVelY(sVelY);
			handler.addObject(shot);
    }
  }
}
