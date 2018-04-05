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

	public Main(){
		tex = new Texture();

		handler = new Handler();

		cam = new Camera(0, 0);

		new Window(WIDTH,HEIGHT,"jeff",this);

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
	}

	public void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
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
		int w = image.getWidth();
		int h = image.getHeight();

		int enemyChoice = (int)(Math.random()*3);
		int currentLevel;
		try {
			currentLevel = Integer.parseInt(state);
		} catch (Exception e){
			currentLevel = 1;
		}


		for (int xx = 0; xx < w; xx++){
			for (int yy = 0; yy < h; yy++){
				if (xx==11 && yy==0 && doors[0] != 0){
					handler.addObject(new Door(xx*33 + 800*dx, yy*32 + 800*dy, "Door", 1, 2, doors[0]));
				}
				else if (xx==12 && yy==0 && doors[0] != 0){
					handler.addObject(new Door(xx*33 + 800*dx, yy*32 + 800*dy, "Door", 1, 4, doors[0]));
				}
				else if (xx==23 && yy==11 && doors[1] != 0){
					handler.addObject(new Door(xx*33 + 800*dx, yy*32 + 800*dy, "Door", 2, 3, doors[1]));
				}
				else if (xx==23 && yy==12 && doors[1] != 0){
					handler.addObject(new Door(xx*33 + 800*dx, yy*32 + 800*dy, "Door", 2, 1, doors[1]));
				}
				else if (xx==11 && yy==23 && doors[2] != 0){
					handler.addObject(new Door(xx*33 + 800*dx, yy*32 + 800*dy, "Door", 3, 2, doors[2]));
				}
				else if (xx==12 && yy==23 && doors[2] != 0){
					handler.addObject(new Door(xx*33 + 800*dx, yy*32 + 800*dy, "Door", 3, 4, doors[2]));
				}
				else if (xx==0 && yy==11 && doors[3] != 0){
					handler.addObject(new Door(xx*33 + 800*dx, yy*32 + 800*dy, "Door", 4, 3, doors[3]));
				}
				else if (xx==0 && yy==12 && doors[3] != 0){
					handler.addObject(new Door(xx*33 + 800*dx, yy*32 + 800*dy, "Door", 4, 1, doors[3]));
				}
			}
		}

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
						handler.addObject(new Block(xx*33 + 800*dx, yy*32 + 800*dy, "Block", type, currentLevel));
					}
					if (red == 0 && green == 0 && blue == 255){
						handler.addObject(new Player(xx*33 + 800*dx, yy*32 + 800*dy, "Player", 0, handler, this, screen));
					}
					if (red == 255 && green == 0 && blue == 0){
						if (enemyChoice == 1) handler.addObject(new Chaser(xx*33 + 800*dx, yy*32 + 800*dy, "Chaser", handler, screen));
						else if (enemyChoice == 2) handler.addObject(new Pouncer(xx*33 + 800*dx, yy*32 + 800*dy, "Pouncer", handler, screen));
						else handler.addObject(new Shooter(xx*33 + 800*dx, yy*32 + 800*dy, "Shooter", handler, screen));
					}
					if (red == 255 && green == 1 && blue == 0){
						handler.addObject(new Chaser(xx*33 + 800*dx, yy*32 + 800*dy, "Chaser", handler, screen));
					}
					if (red == 255 && green == 2 && blue == 0){
						handler.addObject(new Shooter(xx*33 + 800*dx, yy*32 + 800*dy, "Shooter", handler, screen));
					}
					if (red == 255 && green == 255 && blue == 0){
						handler.addObject(new Trapdoor(xx*33 + 800*dx, yy*32 + 800*dy, "Trapdoor"));
					}
					if (red == 0 && green == 255 && blue == 0){
						int choice = (int)(Math.random()*11);
						String weapon = "";
						if (choice == 0) weapon = "smg";
						else if (choice == 1) weapon = "sniper";
						else if (choice == 2) weapon = "assault rifle";
						else if (choice == 3) weapon = "DMR";
						else if (choice == 4) weapon = "slugshot";
						else if (choice == 5) weapon = "minigun";
						else if (choice == 6) weapon = "revolver";
						else if (choice == 7) weapon = "pump shotgun";
						else if (choice == 8) weapon = "tac shotgun";
						else if (choice == 9) weapon = "mauler";
						else weapon = "pistol";

						handler.addObject(new Weapon(xx*33 + 800*dx, yy*32 + 800*dy, weapon));
					}
				}
			}
		}

		handler.addObject(new Block(10*33 + 800*dx, -1*32 + 800*dy, "Block", 0, currentLevel));
		handler.addObject(new Block(13*33 + 800*dx, -1*32 + 800*dy, "Block", 0, currentLevel));
	}

	private void generateMap(){
		points.clear();
		vectors.clear();
		RoomPoint startRoom = new RoomPoint(0, 0);
		points.add(startRoom);
		while (points.size() < 8){
			for (int i=0; i<points.size(); i++){
				randVectors(points.get(i));
				if (points.size() > 10){
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
	}

	private void randVectors(RoomPoint A){
		if (points.size() < 10) randVector(1, 0, A);
    if (points.size() < 10) randVector(-1, 0, A);
    if (points.size() < 10) randVector(0, 1, A);
    if (points.size() < 10) randVector(0, -1, A);
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

	public static void main(String [] args){
		new Main();
	}

}
