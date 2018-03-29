package games4jeffpackage;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameThing {
	protected float x, y, velX, velY, width, height, aX, aY;

	protected String id;

	public GameThing(float x, float y, String id){
		this.x = x;
		this.y = y;
		this.id = id;
	}
	public abstract void tick();
	public abstract void render(Graphics g);
	public abstract Rectangle getBounds();

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getVelX() {
		return velX;
	}

	public void setVelX(float velX) {
		this.velX = velX;
	}

	public float getVelY() {
		return velY;
	}

	public void setVelY(float velY) {
		this.velY = velY;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
}
