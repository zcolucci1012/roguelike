package games4jeffpackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.util.Random;

public abstract class Enemy extends GameThing{

	private Texture tex = Main.getInstance();

  public Enemy(float x, float y, String id) {
    super(x, y, "Enemy." + id);
  }

	public RoomPoint getRoom(){
		int roomX;
		int roomY;
		if (x < 0) roomX = ((int)x-800)/800;
		else roomX = (int)x/800;
		if (y < 0) roomY = ((int)y-800)/800;
		else roomY = (int)y/800;
		return new RoomPoint (roomX, -roomY);
	}

  public abstract void tick();
  public abstract void render(Graphics g);
  public abstract Rectangle getBounds();
}
