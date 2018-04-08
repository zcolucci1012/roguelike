package games4jeffpackage;

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
	private int numWeapons = 10;
	private int numPowerups = 1;
	private boolean levelCleared = false;
	private int w;
	private int h;
	private int currentLevel;
	private int itemRoomIndex = 0;
	private Window window;

	public Main(){
		tex = new Texture();

		handler = new Handler();

		cam = new Camera(0, 0);

		window = new Window(WIDTH,HEIGHT,"jeff",this);

		screen = new Screen(handler, this, cam);
		this.addKeyListener(new KeyInput(handler, screen));
		this.addMouseListener(screen);

		player = new Player(400,400,"Player", 0, handler, this, screen);
		handler.addObject(player);

		Sound s = new Sound();
		Sound.loop("background", 0.1);

		generateMap();
	}

	public synchronized void start(){
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	public synchronized void stop(){
		try{
			thread.join();
			running = false;
		}catch(Exception e){
			e.printStackTrace();
		}
	}

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

	public void tick(){
		if (!state.equals("menu")) {
			if (cam != null){
				handler.tick(cam);
			}
			for (int i=0; i<handler.stuff.size(); i++){
				GameThing thing = handler.stuff.get(i);
				if (thing.getId().equals("Player")){
					cam.tick(thing);
				}
			}
		}
		screen.tick();
		window.tick();
	}

	public void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(2);
			return;
		}
		g = bs.getDrawGraphics();
		g2d = (Graphics2D)g;

		if (state.equals("1")) g.setColor(new Color(239, 172, 117));
		if (state.equals("2")) g.setColor(new Color(61, 61, 61));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		if (!state.equals("menu")) {
			g2d.translate(cam.getX(), cam.getY()); //begin of cam

			handler.render(g, cam);

			g2d.translate(-cam.getX(), -cam.getY()); //end of cam
		}

		screen.render(g);

		g.dispose();

		bs.show();

		/*
		g.dispose();
		bs.show();
		*/
	}

	private void LoadImageLevel(BufferedImage image, int dx, int dy, int [] doors){
		w = image.getWidth();
		h = image.getHeight();

		int enemyChoice = (int)(Math.random()*4);
		try {
			currentLevel = Integer.parseInt(state);
		} catch (Exception e){
			currentLevel = 1;
		}

		addDoors(dx, dy, doors);

		for (int xx = 0; xx < w; xx++){
			for (int yy = 0; yy < h; yy++){
				int pixel = image.getRGB(xx,yy);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				if (!((xx==11 || xx==12) && yy==0 && doors[0] != 0) &&
						!(xx==23 && (yy==11 || yy==12) && doors[1] != 0) &&
						!((xx==11 || xx==12) && yy==23 && doors[2] != 0) &&
						!(xx==0 && (yy==11 || yy==12) && doors[3] != 0)){
					if (red == 0 && green == 0 && blue == 0){
						int type = (int)(Math.random()*4);
						handler.addObject(new Block(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, "Block", type, currentLevel));
					}
					if (red == 0 && green == 0 && blue == 255){
						handler.addObject(new Player(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, "Player", 0, handler, this, screen));
					}
					if (red == 255 && green == 0 && blue == 0){
						if (enemyChoice == 1) handler.addObject(new Chaser(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, "Chaser", handler, screen));
						else if (enemyChoice == 2) handler.addObject(new Pouncer(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, "Pouncer", handler, screen));
						else if (enemyChoice == 3) handler.addObject(new TrackingShooter(xx*33 + WIDTH*dx + 5, yy*32 + HEIGHT*dy + 5, "TrackingShooter", handler, screen));
						else handler.addObject(new Shooter(xx*33 + WIDTH*dx + 5, yy*32 + HEIGHT*dy + 5, "Shooter", handler, screen));
					}
					if (red == 255 && green == 1 && blue == 0){
						handler.addObject(new Chaser(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, "Chaser", handler, screen));
					}
					if (red == 255 && green == 2 && blue == 0){
						handler.addObject(new Shooter(xx*33 + WIDTH*dx + 5, yy*32 + HEIGHT*dy + 5, "Shooter", handler, screen));
					}
					if (red == 255 && green == 255 && blue == 0){
						handler.addObject(new Trapdoor(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, "Trapdoor"));
					}
					if (red == 0 && green == 255 && blue == 0){
						String weapon = chooseWeapon();
						String powerup = choosePowerup();
						int choice = (int)(Math.random()*(numWeapons + numPowerups));
						if (choice < numWeapons) handler.addObject(new Weapon(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, weapon));
						else handler.addObject(new Powerup(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, powerup, handler));
					}
					if (red == 0 && green == 255 && blue == 1){
						System.out.println("YES!");
						String weapon = chooseWeapon();
						handler.addObject(new Weapon(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, weapon));
					}
					if (red == 0 && green == 255 && blue == 2){
						String powerup = choosePowerup();
						handler.addObject(new Powerup(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, powerup, handler));
					}
					if (red == 0 && green == 255 && blue == 3){
						handler.addObject(new Weapon(xx*33 + WIDTH*dx, yy*32 + HEIGHT*dy, "pistol"));
					}
				}
			}
		}

		handler.addObject(new Block(10*33 + WIDTH*dx, -1*32 + HEIGHT*dy, "Block", 0, currentLevel));
		handler.addObject(new Block(13*33 + WIDTH*dx, -1*32 + HEIGHT*dy, "Block", 0, currentLevel));
	}

	private void addDoors(int dx, int dy, int [] doors){
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


	private String chooseWeapon(){
		int weaponChoice = (int)(Math.random()*numWeapons);
		System.out.println(weaponChoice);
		String weapon = "";
		if (weaponChoice == 0) weapon = "smg";
		else if (weaponChoice == 1) weapon = "sniper";
		else if (weaponChoice == 2) weapon = "assault rifle";
		else if (weaponChoice == 3) weapon = "DMR";
		else if (weaponChoice == 4) weapon = "slugshot";
		else if (weaponChoice == 5) weapon = "minigun";
		else if (weaponChoice == 6) weapon = "revolver";
		else if (weaponChoice == 7) weapon = "pump shotgun";
		else if (weaponChoice == 8) weapon = "tac shotgun";
		else if (weaponChoice == 9) weapon = "mauler";
		return weapon;
	}
	private String choosePowerup(){
		int powerupChoice = (int)(Math.random()*numPowerups);
		String powerup = "";
		if (powerupChoice == 0) powerup = "health pack";
		return powerup;
	}

	private void generateMap(){
		points.clear();
		vectors.clear();
		RoomPoint startRoom = new RoomPoint(0, 0);
		points.add(startRoom);
		while (points.size() < 8){
			for (int i=0; i<points.size(); i++){
				randVectors(points.get(i));
				if (points.size() > 12){
					break;
				}
			}
		}

		int i=0;
		for (RoomPoint point: points){
			i++;
			int [] doors = getDoors(point);
			if (i == 1){
				level = loader.loadImage("room0.png");
				System.out.println("Starting Room created");
			}
			else if (i == points.size()){
				level = loader.loadImage("roomwin.png");
				System.out.println("Winning Room created");
			}
			else if (i == points.size()-1){
				int roomNum = (int)(Math.random()*35) + 1;
				level = loader.loadImage("room" + roomNum + ".png");
				System.out.println("room" + roomNum + " created (next to winning room)");
			}
			else {
				int roomNum = (int)(Math.random()*35) + 1;
				level = loader.loadImage("room" + roomNum + ".png");
				System.out.println("room" + roomNum + " created");
			}
			LoadImageLevel(level, point.getX(), -point.getY(), doors);
		}

		int k=0;
		for (RoomPoint point: points){
			int j = 0;
			int numDoors = 0;
			int index = 0;
			int [] doors = getDoors(point);
			for (int num: doors){
				if (num != 0) {
					numDoors++;
					index = j;
				}
				j++;
			}
			if (numDoors == 1 && !levelCleared && !point.isPoint(0,0) && !point.isPoint(points.get(points.size()-1))){
				clearLevel(point.getX(), point.getY());
				levelCleared = true;
				level = loader.loadImage("roomItem" + (index+1) + ".png");
				doors[index] = 3;
				RoomPoint temp;
				for (Vector vector: vectors){
					if (vector.hasPoint(point) == 1 || vector.hasPoint(point) == 2){
						temp = vector.getOther(point);
						int [] tempDoors = getDoors(temp);
						int tempIndex = index + 2;
						if (tempIndex > 3) tempIndex -= 4;
						tempDoors[tempIndex] = 3;
						addDoors(temp.getX(), -temp.getY(), tempDoors);
					}
				}
				LoadImageLevel(level, point.getX(), -point.getY(), doors);
				itemRoomIndex = k;
			}
			k++;
		}
	}

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
		System.out.println("Level cleared at " + dx + ", " + dy);
	}

	private void randVectors(RoomPoint A){
		if (points.size() < 12) randVector(1, 0, A);
    if (points.size() < 12) randVector(-1, 0, A);
    if (points.size() < 12) randVector(0, 1, A);
    if (points.size() < 12) randVector(0, -1, A);
	}

	private void randVector(int dx, int dy, RoomPoint A){
		if ((int)(Math.random()*2) == 1){
      RoomPoint point = new RoomPoint(A.getX()+dx, A.getY()+dy);
      boolean found = false;
      for (RoomPoint point2: points){
        if (point2.getX() == point.getX() && point2.getY() == point.getY()){
          found = true;
        }
      }
      if (!found){
        points.add(point);
        vectors.add(new Vector(A, point));
      }
    }
	}

	private int [] getDoors(RoomPoint A){

		int [] doors = {0, 0, 0, 0};
		for (Vector vector: vectors){
			int doorType = 1;
			if (vector.isVector(new Vector(A, points.get(points.size()-1)))){
				doorType = 2;
			}
			int dx = 0;
			int dy = 0;
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

	public void restart(){
		handler.clear();
		for (int i=0; i<handler.stuff.size(); i++){
			GameThing thing = handler.stuff.get(i);
			if (thing.getId().equals("Player")){
				thing.setX(400);
				thing.setY(400);
			}
		}
		changeMusic(screen.getLevel());
		state = screen.getLevel() + "";
		levelCleared = false;
		itemRoomIndex = 0;
		generateMap();
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

	public void changeMusic(int level){
		if (level == 2){
			Sound.loop("background2", 0.1);
		}
		else Sound.loop("background", 0.1);
	}

	public int getItemRoomIndex(){
		return itemRoomIndex;
	}

	public static void main(String [] args){
		new Main();
	}

}
