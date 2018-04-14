 

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Graphics2D;

public class Door extends GameThing{

  private int type;
  private int openType;
  private int doorType;
  private boolean open = false;
  private boolean unlocked = true;

  public Door(float x, float y, String id, int type, int openType, int doorType){
    super(x, y, id);
    this.type = type;
    this.openType = openType;
    this.doorType = doorType;

    //set orientation of the door based on door type (1-top, 2-right, 3-bottom, 4-left)
    if (type == 1){
      this.width = 33;
      this.height = 12;
      this.y = this.y + 20;
    }
    if (type == 2){
      this.width = 12;
      this.height = 32;
    }
    if (type == 3){
      this.width = 33;
      this.height = 12;
    }
    if (type == 4){
      this.width = 12;
      this.height = 32;
      this.x = this.x + 20;
    }
  }

  public void tick(){

  }

  public void render(Graphics g){
    //changes color of door based on type
    if (doorType == 1) g.setColor(Color.ORANGE);
    if (doorType == 2) g.setColor(Color.RED);
    if (doorType == 3) g.setColor(Color.GREEN);
    g.fillRect((int)x, (int)y, (int)width, (int)height);
  }

  public Rectangle getBounds(){
    return new Rectangle((int)x, (int)y, (int)width, (int)height);
  }

  //opens door a certain direction depending on direction specified (1-down, 2-left, 3-up, 4-right)
  public void open(){
    if (!open && unlocked){
      if (openType == 1){
        this.y += 27;
      }
      if (openType == 2){
        this.x -= 28;
      }
      if (openType == 3){
        this.y -= 27;
      }
      if (openType == 4){
        this.x += 28;
      }
      open = true;
    }
  }

  //closes the doors opposite of how they're opened (if already open)
  public void close(){
    if (open){
      if (openType == 1){
        this.y -= 27;
      }
      if (openType == 2){
        this.x += 28;
      }
      if (openType == 3){
        this.y += 27;
      }
      if (openType == 4){
        this.x -= 28;
      }
    }
    open = false;
    unlocked = false; //lock the doors when closed by default
  }

  public void unlock(){
    unlocked = true;
  }

  //get current room of the door
  public RoomPoint getRoom(){
    int roomX;
		int roomY;
		if (x < 0) roomX = ((int)x-Main.WIDTH)/Main.WIDTH;
		else roomX = (int)x/Main.WIDTH;
		if (y < 0) roomY = ((int)y-Main.HEIGHT)/Main.HEIGHT;
		else roomY = (int)y/Main.HEIGHT;
		return new RoomPoint (roomX, -roomY);
  }

  public boolean getUnlocked(){
    return unlocked;
  }
}
