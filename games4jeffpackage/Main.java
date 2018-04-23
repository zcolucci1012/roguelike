

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Main extends Canvas implements Runnable{

	private static final long serialVersionUID = 4934146021504066469L;

	private Thread thread;
	private boolean running = false;
	private Handler handler;
	private Screen screen;
	public static int WIDTH = 800;
	public static int HEIGHT = 800;
	private BufferedImage level = null;
	private BufferedImageLoader loader = new BufferedImageLoader();
	private ArrayList <RoomPoint> points = new ArrayList <RoomPoint> ();
    private ArrayList <Vector> vectors = new ArrayList <Vector> ();
	private Camera cam;
	private Player player;
	public static Texture tex;
	private Graphics g;
	private Graphics2D g2d;
	private String state = "menu";
	private int numWeapons = 11;
	private int numPowerups = 6;
	private boolean levelCleared = false;
	private int w;
	private int h;
	private int currentLevel;
	private int itemRoomIndex = 0;
	private Window window;
	private int numRooms = 59;
	public static Sound sound;

	/*instantiate all variables and stuff and run all level creation stuff*/
	public Main(){
		tex = new Texture(); //get static variable for textures
		sound = new Sound();

		handler = new Handler(); //create handler which contains a list that manages most game items

		cam = new Camera(0, 0); //create camera

		window = new Window(WIDTH,HEIGHT,"jeff",this); //create a new window called "jeff"

		screen = new Screen(handler, this, cam); //create a new screen variable (handles mouse input, gui, and weapon handling)
		this.addKeyListener(new KeyInput(handler, screen, this)); //adds a listener of key input
		this.addMouseListener(screen); //add listener of mouse input

		player = new Player(400,400,"Player", 0, handler, this, screen); //create player and spawn in first room
		handler.addObject(player); //add player to the list of objects

		sound.loop("background"); //start the background music

		generateMap(); //start map generation
	}

	/*called at start, starts game*/
	public synchronized void start(){
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	/*stops the game*/
	public synchronized void stop(){
		try{
			thread.join();
			running = false;
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/*
		called at start of game, runs a game loop
		calls functions called "tick" and "render" which are used in most classes of the game
		these functions will be called every tenth of a second about
		tick will handle actions such as position, movement, firing
		render will draw these elements using the Graphics variable passed to each functions
	*/
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while (running){
			long now = System.nanoTime();
			delta += (now - lastTime)/ns;
			lastTime = now;
			while (delta >= 1){
				tick();
				delta--;
			}
			if (running)
				render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}

	/*calls tick method on different parts of the game*/
	public void tick(){
		if (!state.equals("menu") && !state.equals("pause")) { //if not in the menu screen
			if (cam != null){
				handler.tick(cam); //runs the tick method on objects in the game
			}
			for (int i=0; i<handler.stuff.size(); i++){ //iterates through the object handler
				GameThing thing = handler.stuff.get(i);
				if (thing.getId().equals("Player")){
					cam.tick(thing); //tick the camera after passing the player object into the camera class
				}
			}
			screen.tick(); //tick the screen
		}

		window.tick(); //tick the window (doesn't do much outside of displaying the cursor)
	}

	/*calls render method on different parts of the game*/
	public void render(){
		BufferStrategy bs = this.getBufferStrategy(); //something with buffers idk
		if(bs == null){
			this.createBufferStrategy(2);
			return;
		}
		g = bs.getDrawGraphics(); //instantiate the graphics variable
		g2d = (Graphics2D)g;

		//set background color based on level
		if (state.equals("1") || currentLevel == 1) g.setColor(new Color(239, 172, 117));
		if (state.equals("2") || currentLevel == 2) g.setColor(new Color(61, 61, 61));
		if (state.equals("3") || currentLevel == 3) g.setColor(new Color(104, 81, 53));
		if (state.equals("4") || currentLevel == 4) g.setColor(new Color(73, 142, 163));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		if (!state.equals("menu")) {
			g2d.translate(cam.getX(), cam.getY()); //begin of cam

			handler.render(g, cam); //render the items in the handler after passing camera

			g2d.translate(-cam.getX(), -cam.getY()); //end of cam
		}

		screen.render(g); //render screen

		g.dispose(); //dispose graphics

		bs.show(); //show the screen
	}

	/*
		converts an image into a level
		takes in the image of the level,
		its distance on a grid away from the origin (0,0) (ex. just above would be dx=0, dy=1)
		and whether a door is present on one of the four given sides of the room
		(the door array consists of four values, zero being no door, 1 being a basic door,
		and further values being special doors. the order of the array is (top, right, bottom, left))
	*/
	private void LoadImageLevel(BufferedImage image, int dx, int dy, int [] doors){
		//save width and height of the image
		w = image.getWidth();
		h = image.getHeight();

		int enemyChoice = (int)(Math.random()*6); //randomly choose enemy to add (random by room not by enemy)
		//get current level
		try {
			currentLevel = Integer.parseInt(state);
		} catch (Exception e){
			currentLevel = 1;
		}

		addDoors(dx, dy, doors); //adds doors dependent on values entered

		//iterate through pixels of the image
		for (int xx = 0; xx < w; xx++){
			for (int yy = 0; yy < h; yy++){
				//get rgb value of pixel
				int pixel = image.getRGB(xx,yy);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				//conditions added to prevent placing blocks over doors
				if (!((xx==11 || xx==12) && yy==0 && doors[0] != 0) &&
						!(xx==23 && (yy==11 || yy==12) && doors[1] != 0) &&
						!((xx==11 || xx==12) && yy==23 && doors[2] != 0) &&
						!(xx==0 && (yy==11 || yy==12) && doors[3] != 0)){
					if (red == 0 && green == 0 && blue == 0){ //select random block from block types and place at point
						int type = (int)(Math.random()*4);
						handler.addObject(new Block(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, "Block", type, currentLevel));
						/*
							values in the object above can be explained as such:
							the position is the current location of the pixel (xx/yy),
							multiplied by the in-game distance of the pixels (33/32),
							and added to the change in x or y of the room (dx/dy),
							multiplied by the size of each room (WIDTH/HEIGHT).
							then the objects ID ("Block", usually name of the class),
							and in this case the type of block and the currentLevel.
							many objects require certain variables outside of the position and ID,
							(often the main class, handler, or screen) because they will be
							referenced within the object
						*/
					}
					if (red == 0 && green == 0 && blue == 255){ //most likely shouldn't be used, adds player
						handler.addObject(new Player(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, "Player", 0, handler, this, screen));
					}
					if (red == 255 && green == 0 && blue == 0){ //adds a random enemy
						if (enemyChoice == 1) handler.addObject(new Chaser(xx*33 + WIDTH*dx + 5, yy*32 + HEIGHT*dy + 5, "Chaser", handler, screen));
						else if (enemyChoice == 2) handler.addObject(new Pouncer(xx*33 + WIDTH*dx + 5, yy*32 + HEIGHT*dy + 5, "Pouncer", handler, screen));
						else if (enemyChoice == 3) handler.addObject(new TrackingShooter(xx*33 + WIDTH*dx + 5, yy*32 + HEIGHT*dy + 5, "TrackingShooter", handler, screen));
						else if (enemyChoice == 4) handler.addObject(new Shooter(xx*33 + WIDTH*dx + 5, yy*32 + HEIGHT*dy + 5, "Shooter", handler, screen));
						else if (enemyChoice == 5) handler.addObject(new Bumbler(xx*33 + WIDTH*dx + 5, yy*32 + HEIGHT*dy + 5, "Bumbler", handler, screen));
						else handler.addObject(new Chicken(xx*33 + WIDTH*dx + 5, yy*32 + HEIGHT*dy + 5, "Chicken", handler, screen));
					}
					/*
						adds an enemy based on the green value to represent somewhat
						of a numerical ID. enemies will have a red value 255 and a
						unique green value as new enemies are added
					*/
					if (red == 255 && green == 1 && blue == 0){ //note a +5 is added simply to center the object
						handler.addObject(new Chaser(xx*33 + WIDTH*dx + 5, yy*32 + HEIGHT*dy + 5, "Chaser", handler, screen));
					}
					if (red == 255 && green == 2 && blue == 0){
						handler.addObject(new Shooter(xx*33 + WIDTH*dx + 5, yy*32 + HEIGHT*dy + 5, "Shooter", handler, screen));
					}
					if (red == 255 && green == 3 && blue == 0){
						handler.addObject(new Pouncer(xx*33 + WIDTH*dx + 5, yy*32 + HEIGHT*dy + 5, "Pouncer", handler, screen));
					}
					if (red == 255 && green == 4 && blue == 0){
						handler.addObject(new TrackingShooter(xx*33 + WIDTH*dx + 5, yy*32 + HEIGHT*dy + 5, "TrackingShooter", handler, screen));
					}
					if (red == 255 && green == 5 && blue == 0){
						handler.addObject(new Bumbler(xx*33 + WIDTH*dx + 5, yy*32 + HEIGHT*dy + 5, "Bumbler", handler, screen));
					}
					if (red == 255 && green == 6 && blue == 0){
						handler.addObject(new Chicken(xx*33 + WIDTH*dx + 5, yy*32 + HEIGHT*dy + 5, "Chicken", handler, screen));
					}
					if (red == 255 && green == 255 && blue == 0){
						handler.addObject(new Boss(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, "Boss", handler, screen));
					}
					if (red == 0 && green == 255 && blue == 0){ //add a random weapon or powerup
						String weapon = chooseWeapon(); //chooses from a list of weapons
						String powerup = choosePowerup(); //chooses from a list of powerups
						int choice = (int)(Math.random()*(numWeapons + numPowerups)); //numWeapons and numPowerups must be changed when new one added
						//note that currently all weapons/powerups have equal chance of spawning
						if (choice < numWeapons) handler.addObject(new Weapon(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, weapon));
						else handler.addObject(new Powerup(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, powerup, handler, screen));
					}
					if (red == 0 && green == 255 && blue == 1){ //adds random weapon
						String weapon = chooseWeapon();
						handler.addObject(new Weapon(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, weapon));
					}
					if (red == 0 && green == 255 && blue == 2){ //adds random powerup
						String powerup = choosePowerup();
						handler.addObject(new Powerup(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, powerup, handler, screen));
					}
					if (red == 0 && green == 255 && blue == 3){ //adds pistol (never used)
						handler.addObject(new Weapon(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, "pistol"));
					}
				}
			}
		}

		//blocks added between rooms because player used to be able to escape between opening between rooms
		handler.addObject(new Block(10*33 + WIDTH*dx, -1*32 + HEIGHT*dy, "Block", 0, currentLevel));
		handler.addObject(new Block(13*33 + WIDTH*dx, -1*32 + HEIGHT*dy, "Block", 0, currentLevel));
	}

	/* takes in the level's position and the door values and adds doors*/
	private void addDoors(int dx, int dy, int [] doors){
		//iterate through image, look at door conditions, add door
		for (int xx = 0; xx < w; xx++){
			for (int yy = 0; yy < h; yy++){
				if (xx==11 && yy==0 && doors[0] != 0){
					handler.addObject(new Door(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, "Door", 1, 2, doors[0]));
				}
				else if (xx==12 && yy==0 && doors[0] != 0){
					handler.addObject(new Door(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, "Door", 1, 4, doors[0]));
				}
				else if (xx==23 && yy==11 && doors[1] != 0){
					handler.addObject(new Door(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, "Door", 2, 3, doors[1]));
				}
				else if (xx==23 && yy==12 && doors[1] != 0){
					handler.addObject(new Door(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, "Door", 2, 1, doors[1]));
				}
				else if (xx==11 && yy==23 && doors[2] != 0){
					handler.addObject(new Door(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, "Door", 3, 2, doors[2]));
				}
				else if (xx==12 && yy==23 && doors[2] != 0){
					handler.addObject(new Door(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, "Door", 3, 4, doors[2]));
				}
				else if (xx==0 && yy==11 && doors[3] != 0){
					handler.addObject(new Door(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, "Door", 4, 3, doors[3]));
				}
				else if (xx==0 && yy==12 && doors[3] != 0){
					handler.addObject(new Door(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, "Door", 4, 1, doors[3]));
				}
			}
		}

		//add blocks over the doors so that the doors are under blocks when opened
		for (int xx = 0; xx < w; xx++){
			for (int yy = 0; yy < h; yy++){
				if (((xx==10 || xx==13) && (yy==0 || yy==23)) ||
						((xx==0 || xx==23) && (yy==10 || yy==13))){
					int type = (int)(Math.random()*4);
					handler.addObject(new Block(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, "Block", type, currentLevel));
				}
			}
		}
	}

	/*returns a random weapon name*/
	public String chooseWeapon(){
		int weaponChoice = (int)(Math.random()*numWeapons);
		switch(weaponChoice){
    		case 0: return "smg";
    		case 1: return "sniper";
    		case 2: return "assault rifle";
    		case 3: return "DMR";
    		case 4: return "slugshot";
    		case 5: return "minigun";
    		case 6: return "revolver";
    		case 7: return "pump shotgun";
    		case 8: return "tac shotgun";
    		case 9: return "mauler";
            case 10: return "grenade launcher";
            default: return "";
        }
	}

	/*returns a random powerup name*/
	public String choosePowerup(){
		int powerupChoice = (int)(Math.random()*numPowerups);
		switch(powerupChoice){
       		case 0: return "health pack";
       		case 1: return "damage boost";
       		case 2: return "speed boost";
       		case 3: return "defense boost";
        	case 4: return "fire rate boost";
        	case 5: return "accuracy boost";
        	default: return "";
        }
	}

	/*
		starts map generation
		called from main constructor
		generates a series of random vectors from a starting point
		then generates random vectors off of those points (randVectors())
		until size of the room reaches at least 8, at most 12
	*/
	private void generateMap(){
		points.clear();
		vectors.clear();
		RoomPoint startRoom = new RoomPoint(0, 0);
		points.add(startRoom); //adds starting point to list of points
		while (points.size() < 8){
			for (int i=0; i<points.size(); i++){
				randVectors(points.get(i)); //adds random vectors based off point given
				if (points.size() > 12){
					break;
				}
			}
		}

		//loads rooms at each point in the list of points
		int i=0;
		for (RoomPoint point: points){
			i++;
			int [] doors = getDoors(point); //returns the value of the doors present at each side
			if (i == 1){ //create starting room
				level = loader.loadImage("assets/room0.png"); //loads the image with the current file name
			}
			else if (i == points.size()){ //create boss room at final point
				level = loader.loadImage("assets/roomwin.png");
			}
			else { //create random room from list of rooms
				int roomNum = (int)(Math.random()*numRooms) + 1;
				level = loader.loadImage("assets/room" + roomNum + ".png");
			}
			LoadImageLevel(level, point.getX(), -point.getY(), doors); //load level with information gathered
		}

		//finds a room with only one door, turns it into an item room
		int k=0;
		for (RoomPoint point: points){
			int j = 0;
			int numDoors = 0;
			int index = 0;
			int [] doors = getDoors(point);
			for (int num: doors){ //finds how many doors (if 1, save location of door)
				if (num != 0) {
					numDoors++;
					index = j;
				}
				j++;
			}

			//if the room has one door, no level has been cleared yet, and isn't the start nor end points
			if (numDoors == 1 && !levelCleared && !point.isPoint(0,0) && !point.isPoint(points.get(points.size()-1))){
				clearLevel(point.getX(), point.getY()); //clears whatever is at the current point
				levelCleared = true;
				level = loader.loadImage("assets/roomItem" + (index+1) + ".png"); //generates item room based off door location
				doors[index] = 3; //sets the inside door to a green door
				RoomPoint temp;
				for (Vector vector: vectors){
					if (vector.hasPoint(point) == 1 || vector.hasPoint(point) == 2){
						temp = vector.getOther(point);
						int [] tempDoors = getDoors(temp);
						int tempIndex = index + 2;
						if (tempIndex > 3) tempIndex -= 4;
						tempDoors[tempIndex] = 3; //sets the outside door to a green door
						addDoors(temp.getX(), -temp.getY(), tempDoors);
					}
				}
				LoadImageLevel(level, point.getX(), -point.getY(), doors); //regenerates level with new doors
				itemRoomIndex = k; //get location of item room
			}
			k++;
		}
	}

	/*clears room at a given point*/
	private void clearLevel(int dx, int dy){
		for(int i = 0; i < handler.stuff.size(); i++){
			GameThing thing = handler.stuff.get(i);
			if (thing.getX() >= dx * WIDTH && thing.getX() < dx * WIDTH + WIDTH){
				if (thing.getY() < -dy * HEIGHT + HEIGHT - 32 && thing.getY() > -dy * HEIGHT){
					handler.removeObject(thing);
					i--;
				}
			}
		}
	}

	/*possibly generates four random vectors*/
	private void randVectors(RoomPoint A){
		if (points.size() < 12) randVector(1, 0, A);
    if (points.size() < 12) randVector(-1, 0, A);
    if (points.size() < 12) randVector(0, 1, A);
    if (points.size() < 12) randVector(0, -1, A);
	}

	/*adds a new vector if random chance successful*/
	private void randVector(int dx, int dy, RoomPoint A){
		if ((int)(Math.random()*2) == 1){
      RoomPoint point = new RoomPoint(A.getX()+dx, A.getY()+dy);
      boolean found = false;
      for (RoomPoint point2: points){ //finds if point already present
        if (point2.getX() == point.getX() && point2.getY() == point.getY()){
          found = true;
        }
      }
      if (!found){ //add if not already found
        points.add(point);
        vectors.add(new Vector(A, point)); //add a vector between starting point and random point
      }
    }
	}

	private int [] getDoors(RoomPoint A){
		int [] doors = {0, 0, 0, 0};
		for (Vector vector: vectors){ //iterate through current list of vector
			int doorType = 1;
			if (vector.isVector(new Vector(A, points.get(points.size()-1)))){ //change to red door if boss room (endpoint)
				doorType = 2;
			}
			int dx = 0;
			int dy = 0;
			//add doors based on the change in x or change in y of the vector (changes value of door from 0)
			if (vector.hasPoint(A) == 1){
				dx = vector.getDX();
				dy = vector.getDY();
				if (dy == 1) doors[0] = doorType;
				if (dx == 1) doors[1] = doorType;
				if (dy == -1) doors[2] = doorType;
				if (dx == -1) doors[3] = doorType;
			}
			else if (vector.hasPoint(A) == 2){
				dx = -vector.getDX();
				dy = -vector.getDY();
				if (dy == 1) doors[0] = doorType;
				if (dx == 1) doors[1] = doorType;
				if (dy == -1) doors[2] = doorType;
				if (dx == -1) doors[3] = doorType;
			}
		}
		return doors;
	}

	/*called at end of level, clears level and changes state*/
	public void restart(){
		handler.clear(); //clears all objects (except player)
		for (int i=0; i<handler.stuff.size(); i++){
			GameThing thing = handler.stuff.get(i);
			if (thing.getId().equals("Player")){ //set location of player
				thing.setX(400);
				thing.setY(400);
			}
		}
		changeMusic(screen.getLevel()); //change background music
		state = screen.getLevel() + ""; //change game's current state
		levelCleared = false; //this means a new item room can be generated
		itemRoomIndex = 0;
		generateMap(); //generate a new map
	}

	public ArrayList <RoomPoint> getPoints(){
		return points;
	}

	public ArrayList <Vector> getVectors(){
		return vectors;
	}

	public String getState(){
		return state;
	}

	public void setState(String state){
		this.state = state;
	}

	public static Texture getInstance(){
		return tex;
	}

	/*takes in numerical value, plays according sound*/
	public void changeMusic(int level){
		if (level == 2){
			sound.loop("background2");
		}
		else if (level == 3){
			sound.loop("background3");
		}
		else if (level == 4){
			sound.loop("background4");
		}
		else sound.loop("background");
	}

	public int getItemRoomIndex(){
		return itemRoomIndex;
	}

	public static void main(String [] args){
		new Main();
		//nothing else should go here
	}

	public int getNumWeapons(){
		return numWeapons;
	}

	public int getNumPowerups(){
		return numPowerups;
	}

	public static Sound getSound(){
		return sound;
	}

	public void togglePause(){
		if (!state.equals("menu")){
			if (state.equals("pause")) state = currentLevel+"";
			else state = "pause";
		}
	}

}
