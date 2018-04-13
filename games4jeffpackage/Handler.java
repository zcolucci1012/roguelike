 

import java.awt.Graphics;
import java.util.LinkedList; // <--- never use this they stink
import java.util.ArrayList;

public class Handler {
	ArrayList <GameThing> stuff = new ArrayList <GameThing> ();

	/*
		runs through every object added to the list
		call the tick method on each object unless
		it's an enemy outside of the player's room
	*/
	public void tick(Camera cam){
		float camX = -cam.getX();
		float camY = -cam.getY();
		for (int i = 0; i < stuff.size(); i++){
			GameThing thing = stuff.get(i);
			if (!(thing.getId().length() >= 6 && thing.getId().substring(0, 6).equals("Enemy."))){
				thing.tick();
			}
			else if (thing.getX() >= camX && thing.getX() <= camX+Main.WIDTH){
				if (thing.getY() >= camY && thing.getY() <= camY+Main.HEIGHT){
					thing.tick();
				}
			}
		}
	}

	/*
		runs through every object added to the list
		call the render method on each object unless
		the object is outside of the player's current room
		finally renders the player over all other objects
	*/
	public void render(Graphics g, Camera cam){
		float camX = -cam.getX();
		float camY = -cam.getY();
		for (int i = 0; i < stuff.size(); i++){
			GameThing thing = stuff.get(i);
			if (thing.getId().equals("Player")){
				thing.render(g);
			}
			else if (thing.getX() >= camX && thing.getX() <= camX+Main.WIDTH){
				if (thing.getY() >= camY && thing.getY() <= camY+Main.HEIGHT){
					thing.render(g);
				}
			}
		}
		for (int i = 0; i < stuff.size(); i++){
			GameThing thing = stuff.get(i);
			if (thing.getId().equals("Player")){
				thing.render(g);
				break;
			}
		}
	}

	/*adds an object to the stuff list*/
	public void addObject(GameThing thing){
		stuff.add(thing);
	}
	/*removes an object to the stuff list*/
	public void removeObject(GameThing thing){
		stuff.remove(thing);
	}

	/*clears objects bar the player*/
	public void clear(){
		for (int i = 0; i < stuff.size(); i++){
			GameThing thing = stuff.get(i);

			if (!thing.getId().equals("Player")){
				removeObject(thing);
				i--;
			}
		}
	}
}
