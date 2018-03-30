package games4jeffpackage;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Main extends Canvas implements Runnable{

	private static final long serialVersionUID = 4934146021504066469L;

	private Thread thread;
	private boolean running = false;
	private Handler handler;
	private Screen screen;
	public static int WIDTH = 800;
	public static int HEIGHT = 800;
	private BufferedImage level = null;

	public Main(){
		BufferedImageLoader loader = new BufferedImageLoader();
		level = loader.loadImage("room1.png"); //load level

		handler = new Handler();

		new Window(WIDTH,HEIGHT,"jeff",this);

		screen = new Screen(handler, this);
		this.addKeyListener(new KeyInput(handler, screen));
		this.addMouseListener(screen);

		LoadImageLevel(level);
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
		handler.tick();
		if (screen.hasWeapon()){
			screen.tick();
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

		handler.render(g);
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

	private void LoadImageLevel(BufferedImage image){
		int w = image.getWidth();
		int h = image.getHeight();

		System.out.println("width, height: " + w + " " + h);

		for (int xx = 0; xx < w; xx++){
			for (int yy = 0; yy < h; yy++){
				int pixel = image.getRGB(xx,yy);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				if (red == 0 && green == 0 && blue == 0){
					handler.addObject(new Block(xx*32, yy*32, "Block"));
				}
				if (red == 0 && green == 0 && blue == 255){
					handler.addObject(new Player(xx*32, yy*32, "Player", handler, this, screen));
				}
				if (red == 255 && green == 0 && blue == 0){
					handler.addObject(new Enemy(xx*32, yy*32, "Enemy", handler, screen));
				}
				if (red == 0 && green == 255 && blue == 0){
					int choice = (int)(Math.random()*2);
					String weapon = "";
					if (choice == 0) weapon = "smg";
					else weapon = "pistol";
					System.out.println(weapon);
					handler.addObject(new Weapon(xx*32, yy*32, weapon));
				}
			}
		}
	}
}
