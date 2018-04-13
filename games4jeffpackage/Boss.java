package games4jeffpackage;

import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Boss extends Enemy{

  private Screen screen;
  private int timer = 0;
  private float initX;
  private float initY;
  private int action = -1;
  private int movement;
  private boolean dead = false;
  private int knockbackTimer = 0;
  private boolean leftWall = false;
  private boolean rightWall = false;
  private boolean topWall = false;
  private boolean bottomWall = false;
  private float sx;
  private float sy;
  private float tempVelX;
  private float tempVelY;
  private float tempVel;
  private float d;

  public Boss(float x, float y, String id, Handler handler, Screen screen){
    super(x, y, id, handler, 300);
    this.screen = screen;
    initX = x;
    initY = y;

    movement = (int)(Math.random()*4); //randomizes initial movment

    width = 64;
    height = 64;
  }

  public void tick(){
    super.tick();

    //flags that alert whether a wall is touched
    leftWall = false;
    rightWall = false;
    topWall = false;
    bottomWall = false;

    for(int i = 0; i < handler.stuff.size(); i++){
      GameThing thing = handler.stuff.get(i);
      if (thing.getId() == "Shot"){
        if (thing.getBounds().intersects(getBounds())){
          if (action != 1) {
            //adjusts for knockback (currently dependent on shot speed)
            velX = thing.getVelX()/25;
            velY = thing.getVelY()/25;
            knockbackTimer = 5;
          }
        }
      }

      //get location of player
      if (thing.getId() == "Player"){
        sx = thing.getX() + thing.getWidth()/2;
        sy = thing.getY() + thing.getHeight()/2;
      }

      //collision
      if (thing.getId().equals("Block") || thing.getId().equals("Door") || (thing.getId().length() >= 6 && thing.getId().substring(0,6).equals("Enemy.") && thing != this)){
        if (thing.getBounds().intersects(getBoundsRight())){
					x = thing.getX() - width;
          rightWall = true; //activates flag
        }
				if (thing.getBounds().intersects(getBoundsLeft())){
					x = thing.getX() + thing.getBounds().width;
          leftWall = true;
        }
				if (thing.getBounds().intersects(getBoundsTop())){
					y = thing.getY() + thing.getBounds().height;
          topWall = true;
				}
				if (thing.getBounds().intersects(getBoundsBottom())){
					y = thing.getY() - height;
          bottomWall = true;
				}
      }
    }

    timer++;
    if (timer==400){
      action = (int)(Math.random()*3); //chooses a random action every 400 ticks
      timer = 0;
    }
    if (timer % 100 == 0){
      movement = (int)(Math.random()*4); //chooses a random direction to move every 100 ticks
    }

    //the following functions ran continuously until timer runs out
    if (action == 0) action1();
    if (action == 1) action2();
    if (action == 2) action3();

    if (knockbackTimer == 0 && action != 1){
      //adjusts movement if touching wall
      if (leftWall) movement = 0;
      if (rightWall) movement = 1;
      if (topWall) movement = 2;
      if (bottomWall) movement = 3;
      //sets velocity
      if (movement == 0) {velX = 2f; velY = 0;}
      if (movement == 1) {velX = -2f; velY = 0;}
      if (movement == 2) {velY = 2f; velX = 0;}
      if (movement == 3) {velY = -2f; velX = 0;}
    }

    if (knockbackTimer != 0){
      knockbackTimer--;
    }
  }

  public void render(Graphics g){
    super.render(g);
    g.setColor(new Color(173, 237, 150));
    g.fillRect((int)x, (int)y, (int)width, (int)height);
    g.setColor(Color.BLACK);
    g.drawString((action+1) + "", (int)(x+width/2), (int)(y+height/2));
  }

  /*//shoots tracking bullets in cardinal directions, then diagonal*/
  public void action1(){
    if (timer == 100){
      TrackingShot shot1 = new TrackingShot((int)x + width/2, (int)y, "TrackingShot", (float)(Math.PI/2), 10, 100, 0.25f, handler);
			shot1.setVelY(-5);
      TrackingShot shot2 = new TrackingShot((int)x + width, (int)y + height/2, "TrackingShot", 0, 10, 100, 0.25f, handler);
			shot2.setVelX(5);
      TrackingShot shot3 = new TrackingShot((int)x + width/2, (int)y + height, "TrackingShot", (float)(3*Math.PI/2), 10, 100, 0.25f, handler);
			shot3.setVelY(5);
      TrackingShot shot4 = new TrackingShot((int)x, (int)y + height/2, "TrackingShot", (float)(Math.PI), 10, 100, 0.25f, handler);
			shot4.setVelX(-5);
			handler.addObject(shot1);
      handler.addObject(shot2);
      handler.addObject(shot3);
      handler.addObject(shot4);
    }

    if (timer == 200){
      TrackingShot shot1 = new TrackingShot((int)x+width, (int)y, "TrackingShot", (float)(Math.PI/4), 10, 100, 0.25f, handler);
			shot1.setVelX((float)(5 * Math.sin(Math.PI/4)));
      shot1.setVelY((float)(5 * Math.cos(Math.PI/4)));
      TrackingShot shot2 = new TrackingShot((int)x + width, (int)y + height, "TrackingShot", (float)(7*Math.PI/4), 10, 100, 0.25f, handler);
      shot2.setVelX((float)(5 * Math.sin(7*Math.PI/4)));
      shot2.setVelY((float)(5 * Math.cos(7*Math.PI/4)));
      TrackingShot shot3 = new TrackingShot((int)x, (int)y + height, "TrackingShot", (float)(5*Math.PI/4), 10, 100, 0.25f, handler);
      shot3.setVelX((float)(5 * Math.sin(5*Math.PI/4)));
      shot3.setVelY((float)(5 * Math.cos(5*Math.PI/4)));
      TrackingShot shot4 = new TrackingShot((int)x, (int)y, "TrackingShot", (float)(3*Math.PI/4), 10, 100, 0.25f, handler);
      shot4.setVelX((float)(5 * Math.sin(3*Math.PI/4)));
      shot4.setVelY((float)(5 * Math.cos(3*Math.PI/4)));
      handler.addObject(shot1);
      handler.addObject(shot2);
      handler.addObject(shot3);
      handler.addObject(shot4);
    }
  }

  /*treats self like a tracking bullet and launches at player*/
  public void action2(){
    float xx = x + width/2;
    float yy = y + height/2;
    if (timer == 50){
      d = (float)Math.sqrt(Math.pow((sx-(int)xx),2) + Math.pow((sy-(int)yy),2));
  		if (d != 0){
        velX = ((sx - (int)xx)/d*7);
  			velY = ((sy - (int)yy)/d*7);
        tempVelX = velX;
        tempVelY = velY;
        tempVel = (float)Math.sqrt(Math.pow(tempVelX,2) + Math.pow(tempVelY,2));
      }
    }
    if (timer < 350 && timer > 50){ //boss should be stopped before 50 ticks and after 350 ticks
      if (d != 0){
        velX += (float)(((int)sx - xx)/d*0.5);
        velY += (float)(((int)sy - yy)/d*0.5);
        float vel = (float)Math.sqrt(Math.pow(velX,2) + Math.pow(velY,2));
        velX *= (tempVel/vel);
        velY *= (tempVel/vel);
      }
    }
    else {
      velX = 0;
      velY = 0;
    }
  }

  /*spawn a trackingshooter if location not occupied*/
  public void action3(){
    if (timer == 0){
      TrackingShooter trackingshooter = null;
      boolean intersecting = true;
      int dx = screen.getRoom().getX()*Main.WIDTH;
      int dy = -screen.getRoom().getY()*Main.HEIGHT;
      int [] places = {-1, -1, -1, -1};
      for (int i=0; i<4; i++){ //generates list of random corners to attempt
        int place = -1;
        boolean found = true;
        while (found){
          place = (int)(Math.random()*4);
          found = false;
          for (int j=0; j<4; j++){
            if (places[j] == place){
              found = true;
            }
          }
        }
        places[i] = place;
      }

      int k = 0;
      while (k < 4){ //try placing enemy at all four random corners
        if (places[k] == 0) trackingshooter = new TrackingShooter(dx + 100, dy + 100, "TrackingShooter", handler, screen);
        if (places[k] == 1) trackingshooter = new TrackingShooter(dx + 700, dy + 100, "TrackingShooter", handler, screen);
        if (places[k] == 2) trackingshooter = new TrackingShooter(dx + 700, dy + 650, "TrackingShooter", handler, screen);
        if (places[k] == 3) trackingshooter = new TrackingShooter(dx + 100, dy + 650, "TrackingShooter", handler, screen);
        intersecting = false;
        for (int i=0; i<handler.stuff.size(); i++){
          GameThing thing = handler.stuff.get(i);
          if (trackingshooter.getBounds().intersects(thing.getBounds())){
            intersecting = true;
          }
        }
        if (intersecting) k++;
        else break;
      }
      handler.addObject(trackingshooter); //finally add the enemy
    }
  }

  public Rectangle getBounds(){
    return new Rectangle((int)x, (int)y, (int)width, (int)height);
  }

  public Rectangle getBoundsLeft(){
    return new Rectangle((int)x, (int)y+10, (int)5, (int)height-20);
  }

  public Rectangle getBoundsRight(){
    return new Rectangle((int)x+(int)width-5, (int)y+10, (int)5, (int)height-20);
  }

  public Rectangle getBoundsTop(){
    return new Rectangle((int)x+(int)width/4, (int)y, (int)width/2, (int)height/2);
  }

  public Rectangle getBoundsBottom(){
    return new Rectangle((int)x+(int)width/4, (int)y + (int)height/2, (int)width/2, (int)height/2);
  }
}
