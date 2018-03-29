package games4jeffpackage;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

public class Main extends Canvas implements Runnable{

	private static final long serialVersionUID = 4934146021504066469L;

	private Thread thread;
	private boolean running = false;
	private Handler handler;
	private Screen screen;
	private String weapon = "";
	
	public Main(){
		handler = new Handler();
		
		new Window(800,800,"jeff",this);
		
		screen = new Screen(handler, this);
		this.addKeyListener(new KeyInput(handler, screen));
		this.addMouseListener(screen);
		
		addStuff();
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
		g.fillRect(0, 0, 800, 800);
		
		handler.render(g);
		
		g.dispose();
		bs.show();
	}
	
	public void addStuff(){
		handler.addObject(new Player(400,400,"Player", handler, this, screen));
		handler.addObject(new Pickup(250,250,"Pickup.pistol"));
		handler.addObject(new Pickup(250,600,"Pickup.smg"));
		handler.addObject(new Enemy(600,600,"Enemy", handler));
	}
	
	public void setWeapon(String weapon){
		this.weapon = weapon;
	}
	
	public String getWeapon(){
		return this.weapon;
	}
}
