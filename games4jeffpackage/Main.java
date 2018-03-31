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
	private ArrayList <Point> points = new ArrayList <Point> ();
  private ArrayList <Vector> vectors = new ArrayList <Vector> ();
	private Camera cam;
	private Player player;

	public Main(){
		BufferedImageLoader loader = new BufferedImageLoader();


		handler = new Handler();

		cam = new Camera(0, 0);

		new Window(WIDTH,HEIGHT,"jeff",this);

		screen = new Screen(handler, this, cam);
		this.addKeyListener(new KeyInput(handler, screen));
		this.addMouseListener(screen);

		level = loader.loadImage("room1.png"); //load level
		player = new Player(400,400,"Player",handler, this, screen);
		handler.addObject(player);
		generateMap();


		//LoadImageLevel(level);
		//addStuff();
	}

	public static void main(String [] args){
		new Main();
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
				//System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}

	public void tick(){
		if (cam != null){
			handler.tick(cam);
		}
		if (screen.hasWeapon()){
			screen.tick();
		}
		for (int i=0; i<handler.stuff.size(); i++){
			GameThing thing = handler.stuff.get(i);
			if (thing.getId().equals("Player")){
				cam.tick(thing);
			}
		}

	}

	public void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		Graphics2D g2d = (Graphics2D)g;

		g.setColor(new Color(36,123,160));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g2d.translate(cam.getX(), cam.getY()); //begin of cam

		handler.render(g, cam);

		g2d.translate(-cam.getX(), -cam.getY()); //end of cam

		screen.render(g);

		g.dispose();
		bs.show();
	}

	public void addStuff(){
		handler.addObject(new Player(400,400,"Player", handler, this, screen));
		handler.addObject(new Weapon(250,250,"pistol"));
		handler.addObject(new Weapon(250,600,"smg"));
		handler.addObject(new Enemy(500,600,"Enemy", handler, screen));
		//handler.addObject(new Enemy(400,600,"Enemy", handler, screen));
		//handler.addObject(new Enemy(300,600,"Enemy", handler, screen));
		for (int i=0; i<800; i+=32){
			handler.addObject(new Block(0, i, "Block"));
		}
	}

	private void LoadImageLevel(BufferedImage image, int dx, int dy, boolean [] doors){
		int w = image.getWidth();
		int h = image.getHeight();

		for (int xx = 0; xx < w; xx++){
			for (int yy = 0; yy < h; yy++){
				int pixel = image.getRGB(xx,yy);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				if (!((xx==11 || xx==12) && yy==0 && doors[0]) &&
						!(xx==23 && (yy==11 || yy==12) && doors[1]) &&
						!((xx==11 || xx==12) && yy==23 && doors[2]) &&
						!(xx==0 && (yy==11 || yy==12) && doors[3])){
					if (red == 0 && green == 0 && blue == 0){
						handler.addObject(new Block(xx*33 + 800*dx, yy*32 + 800*dy, "Block"));
					}
					if (red == 0 && green == 0 && blue == 255){
						handler.addObject(new Player(xx*33 + 800*dx, yy*32 + 800*dy, "Player", handler, this, screen));
					}
					if (red == 255 && green == 0 && blue == 0){
						handler.addObject(new Enemy(xx*33 + 800*dx, yy*32 + 800*dy, "Enemy", handler, screen));
					}
					if (red == 0 && green == 255 && blue == 0){
						int choice = (int)(Math.random()*2);
						String weapon = "";
						if (choice == 0) weapon = "smg";
						else weapon = "pistol";
						handler.addObject(new Weapon(xx*33 + 800*dx, yy*32 + 800*dy, weapon));
					}
				}
			}
		}
		/*
		if (doors[0]){
			for (int i=0; i<handler.stuff.size(); i++){
				GameThing thing = handler.stuff.get(i);
				if (thing.getX() == 12 * 32 + 800*dx && thing.getY() == 0 * 32 + 800*dy){
					handler.removeObject(thing);
				}
				if (thing.getX() == 13 * 32 + 800*dx && thing.getY() == 0 * 32 + 800*dy){
					handler.removeObject(thing);
				}
			}
		}
		if (doors[1]){
			for (int i=0; i<handler.stuff.size(); i++){
				GameThing thing = handler.stuff.get(i);
				if (thing.getX() == 24 * 32 + 800*dx && thing.getY() == 11 * 32 + 800*dy){
					handler.removeObject(thing);
				}
				if (thing.getX() == 24 * 32 + 800*dx && thing.getY() == 12 * 32 + 800*dy){
					handler.removeObject(thing);
				}
			}
		}
		if (doors[2]){
			for (int i=0; i<handler.stuff.size(); i++){
				GameThing thing = handler.stuff.get(i);
				if (thing.getX() == 12 * 32 + 800*dx && thing.getY() == 23 * 32 + 800*dy){
					handler.removeObject(thing);
				}
				if (thing.getX() == 13 * 32 + 800*dx && thing.getY() == 23 * 32 + 800*dy){
					handler.removeObject(thing);
				}
			}
		}
		if (doors[3]){
			for (int i=0; i<handler.stuff.size(); i++){
				GameThing thing = handler.stuff.get(i);
				if (thing.getX() == 0 * 32 + 800*dx && thing.getY() == 11 * 32 + 800*dy){
					handler.removeObject(thing);
				}
				if (thing.getX() == 0 * 32 + 800*dx && thing.getY() == 12 * 32 + 800*dy){
					handler.removeObject(thing);
				}
			}
		}
		*/
	}

	private void generateMap(){
		Point startRoom = new Point(0, 0);
		points.add(startRoom);
		while (points.size() < 8){
			for (int i=0; i<points.size(); i++){
				randVectors(points.get(i));
				if (points.size() > 10){
					break;
				}
			}
		}
		for (Point point: points){
			boolean [] doors = getDoors(point);
			LoadImageLevel(level, point.getX(), -point.getY(), doors);
		}
	}

	private void randVectors(Point A){
		randVector(1, 0, A);
    randVector(-1, 0, A);
    randVector(0, 1, A);
    randVector(0, -1, A);
	}

	private void randVector(int dx, int dy, Point A){
		if ((int)(Math.random()*2) == 1){
      Point point = new Point(A.getX()+dx, A.getY()+dy);
      boolean found = false;
      for (Point point2: points){
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

	private boolean [] getDoors(Point A){
		boolean [] doors = {false, false, false, false};
		for (Vector vector: vectors){
			int dx = 0;
			int dy = 0;
			if (vector.hasPoint(A) == 1){
				dx = vector.getDX();
				dy = vector.getDY();
				if (dy == 1) doors[0] = true;
				if (dx == 1) doors[1] = true;
				if (dy == -1) doors[2] = true;
				if (dx == -1) doors[3] = true;
			}
			else if (vector.hasPoint(A) == 2){
				dx = -vector.getDX();
				dy = -vector.getDY();
				if (dy == 1) doors[0] = true;
				if (dx == 1) doors[1] = true;
				if (dy == -1) doors[2] = true;
				if (dx == -1) doors[3] = true;
			}
		}
		return doors;
	}
}
