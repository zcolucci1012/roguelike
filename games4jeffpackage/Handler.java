package games4jeffpackage;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.ArrayList;

public class Handler {
	ArrayList <GameThing> stuff = new ArrayList <GameThing> ();

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
	}

	public void addObject(GameThing thing){
		stuff.add(thing);
	}

	public void removeObject(GameThing thing){
		stuff.remove(thing);
	}

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
