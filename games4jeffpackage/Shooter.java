package games4jeffpackage;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Color;

public class Shooter extends Enemy{

  private Handler handler;
  private Screen screen;
  private int hp = 40;
  private float sx;
  private float sy;
  private int timer = 150;

  public Shooter (float x, float y, String id, Handler handler, Screen screen){
    super(x, y, id);
    this.handler = handler;
    this.screen = screen;

    width = 24;
    height = 24;
  }

  public void tick(){
    for(int i = 0; i < handler.stuff.size(); i++){
      GameThing thing = handler.stuff.get(i);
      if (thing.getId() == "Shot"){
        if (thing.getBounds().intersects(getBounds())){
          hp-=screen.getWeapon().getDamage();
          handler.removeObject(thing);
          if (hp <= 0){
            handler.removeObject(this);
          }
        }
      }
    }
    if (timer != 0){
      timer--;
      if (timer == 0){
        fire();
        timer = 150;
      }
    }
  }

  public void render(Graphics g){
    g.setColor(Color.BLACK);
		g.drawString(hp+"", (int)x, (int)y-10);
    g.setColor(Color.CYAN);
    g.fillRect((int)x, (int)y, (int)width, (int)height);
  }

  public Rectangle getBounds(){
    return new Rectangle((int)x, (int)y, (int)width, (int)height);
  }

  private void fire(){
    for(int i = 0; i < handler.stuff.size(); i++){
      GameThing thing = handler.stuff.get(i);
      if (thing.getId() == "Player"){
        sx = thing.getX();
        sy = thing.getY();
      }
    }
		float xx = x + width/2 ;
		float yy = y + height/2;
		float d = (float)Math.sqrt(Math.pow((sx-(int)xx),2) + Math.pow((sy-(int)yy),2));
		if (d != 0){
			float sVelX = ((sx - (int)xx)/d*5);
			float sVelY = ((sy + ((int)(Math.random()*51)-25) - (int)yy)/d*5);
			float angle = (float)Math.atan(sVelY / sVelX);
			EnemyShot shot = new EnemyShot((int)xx-width/4, (int)yy-width/4, "EnemyShot", angle, 10, 100, handler);
			shot.setVelX(sVelX);
			shot.setVelY(sVelY);
			handler.addObject(shot);
    }
  }
}
