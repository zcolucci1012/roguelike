package games4jeffpackage;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameThing {
	protected float x, y, velX, velY, width, height; //the position, velocity, and size of the object held here

	protected String id; //each object type has their own id (ex. "Player" or "Enemy")

	public GameThing(float x, float y, String id){
		this.x = x;
		this.y = y;
		this.id = id;

		//for more info on how to create gamethings, check out Player class and others that extend GameThing
	}

	//tick method controls parts of the object not involved with rendering (i.e. movement, firing)
	public abstract void tick();
	//render method will draw each object
	public abstract void render(Graphics g);
	//returns a rectangle describing the bounds of the object
	//(function could possibly be altered to handle other shapes)
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
