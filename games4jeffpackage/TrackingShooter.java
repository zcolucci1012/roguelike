 

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

public class TrackingShooter extends Enemy{

  private Screen screen;
  private float sx;
  private float sy;
  private int timer = 100;
  private float xx;
  private float yy;
  private float angle;

  public TrackingShooter (float x, float y, String id, Handler handler, Screen screen){
    super(x, y, id, handler, 30);
    this.screen = screen;
    timer = (int)(Math.random()*51); //random timer start point

    width = 24;
    height = 24;
  }

  public void tick(){
    super.tick();
    if (timer != 0){
      timer--;
      if (timer == 0){
        fire(); //fire every 100 ticks
        timer = 100;
      }
    }

    //get location of player and set angle
    xx = x + width/2;
		yy = y + height/2;
    for(int i = 0; i < handler.stuff.size(); i++){
      GameThing thing = handler.stuff.get(i);
      if (thing.getId() == "Player"){
        sx = thing.getX() + thing.getWidth()/2;
        sy = thing.getY() + thing.getHeight()/2;
      }
    }
    angle = (float)Math.atan((sy - yy)/(sx - xx));
    if ((sx-xx) < 0){
      angle += Math.PI;
    }
  }

  public void render(Graphics g){
    super.render(g);
    g.drawImage(tex.enemy[5], (int)x, (int)y, null); //draw base
    Graphics2D g2d = (Graphics2D)g.create();
    g2d.rotate(angle, x+width/2+2, y+height/2+2); //rotate image of turret based on angle to player
    g2d.drawImage(tex.enemy[4], (int)x, (int)y, null);
    g2d.dispose();
  }

  public Rectangle getBounds(){
    return new Rectangle((int)x, (int)y, (int)width, (int)height);
  }

  /* spawn a tracking shot toward player */
  private void fire(){
		float d = (float)Math.sqrt(Math.pow((sx-(int)xx),2) + Math.pow((sy-(int)yy),2));
		if (d != 0){
			float sVelX = ((sx - (int)xx)/d*5);
			float sVelY = ((sy - (int)yy)/d*5);
			angle = (float)Math.atan(sVelY / sVelX);
      float dx = (float)(20*Math.cos((double)angle));
      float dy = (float)(20*Math.sin((double)angle));
			TrackingShot shot = new TrackingShot((int)xx-width/4+dx, (int)yy-width/4+dy, "TrackingShot", angle, 10, 100, 0.15f, handler);
			shot.setVelX(sVelX);
			shot.setVelY(sVelY);
			handler.addObject(shot);
    }
  }
}
