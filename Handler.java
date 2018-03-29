package games4jeffpackage;

import java.awt.Graphics;
import java.util.LinkedList;

public class Handler {
	LinkedList <GameThing> stuff = new LinkedList <GameThing> ();
	
	public void tick(){
		for (int i = 0; i < stuff.size(); i++){
			GameThing thing = stuff.get(i);
			thing.tick();
		}
	}
	
	public void render(Graphics g){
		for (int i = 0; i < stuff.size(); i++){
			GameThing thing = stuff.get(i);
			thing.render(g);
		}
	}
	
	public void addObject(GameThing thing){
		stuff.add(thing);
	}
	
	public void removeObject(GameThing thing){
		stuff.remove(thing);
	}
}
