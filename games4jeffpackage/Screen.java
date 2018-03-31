package games4jeffpackage;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

public class Screen extends MouseAdapter{

	private Handler handler;
	private Main main;
	private Camera cam;
	private int mx;
	private int my;
	private int fireDelay = 25;
	private int shotSpeed = 5;
	private int damage = 5;
	private int magazine = 8;
	private int bullets = 8;
	private int reloadTime = 100;
	private int time2 = 0;
	private Weapon weapon;
	private ArrayList <Weapon> weapons = new ArrayList <Weapon> ();
	private ArrayList <Integer> ammo = new ArrayList <Integer> ();
	private boolean firing = false;
	private boolean reloading = false;
	private boolean auto = false;
	private int time = fireDelay;
	private int pickupAlertTimer = 0;
	private boolean pickupAlertFlag = false;
	private String lastAddedWeapon = "";

	public Screen (Handler handler, Main main, Camera cam){
		this.handler = handler;
		this.main = main;
		this.cam = cam;
	}

	public void mousePressed(MouseEvent e){
		mx = e.getX();
		my = e.getY();

		firing = true;
	}

	public void mouseReleased(MouseEvent e){
		mx = e.getX();
		my = e.getY();

		firing = false;
	}


	private boolean mouseOver(int mx, int my, int x1, int y1, int x2, int y2){
		if(mx > x1 && mx < x2){
			if (my > y1 && my < y2){
				return true;
			}
			else return false;
		}
		else return false;
	}

	public void tick(){
		Point a = MouseInfo.getPointerInfo().getLocation();
		Point b = main.getLocationOnScreen();
		mx = (int) a.getX() - (int)b.getX();
		my = (int) a.getY() - (int)b.getY();
		if (time2 == 0){
			if (time == fireDelay){
				if (firing){
					fire();
					time = 0;
				}
			}
			else {
				time++;
			}
		}
		else {
			time2--;
			if (time2 == 0){
				reloading = false;
				bullets = magazine;
				weapon.setAmmo(magazine);
			}
		}
		if (bullets <= 0 && !reloading){
			time2 = reloadTime;
			reloading = true;
		}
		if (pickupAlertTimer != 0){
			pickupAlertTimer--;
			if (pickupAlertTimer == 0){
				pickupAlertFlag = false;
			}
		}
	}

	private void fire(){
		for(int i = 0; i < handler.stuff.size(); i++){
			GameThing thing = handler.stuff.get(i);
			if (thing.getId() == "Player"){
				int randX = (int)(Math.random()*51)-25;
				int randY = (int)(Math.random()*51)-25;
				float x = thing.getX() + thing.getWidth()/2 ;
				float y = thing.getY() + thing.getHeight()/2;
				float sx = mx - cam.getX();
				float sy = my - cam.getY();
				float d = (float)Math.sqrt(Math.pow((sx+randX-(int)x),2) + Math.pow((sy+randY-(int)y),2));
				if (d != 0){
					float sVelX = ((sx+randX - (int)x)/d*shotSpeed);
					float sVelY = ((sy+randY + ((int)(Math.random()*51)-25) - (int)y)/d*shotSpeed);
					Shot shot = new Shot((int)x-thing.getWidth()/4, (int)y-thing.getHeight()/4, "Shot", damage, handler);
					shot.setVelX(sVelX);
					shot.setVelY(sVelY);
					handler.addObject(shot);
					bullets--;
					weapon.setAmmo(bullets);
				}
			}
		}
	}

	public void addWeapon(Weapon weapon){
		boolean owned = false;
		for (Weapon ownedWeapon: weapons){
			if (ownedWeapon.getId().equals(weapon.getId())){
				owned = true;
			}
		}
		if (!owned) {
			weapons.add(weapon);
		}
		lastAddedWeapon = weapon.getName();
		pickupAlertTimer = 200;
		pickupAlertFlag = true;
		if (weapons.size() == 1){
			setWeapon(weapon);
		}
	}

	public void setWeapon(Weapon weapon){
		this.weapon = weapon;
		fireDelay = weapon.getFireDelay();
		shotSpeed = weapon.getShotSpeed();
		damage = weapon.getDamage();
		magazine = weapon.getMagazine();
		reloadTime = weapon.getReloadTime();
		bullets = weapon.getAmmo();
		time = weapon.getFireDelay();
	}

	public void changeWeapon(){
		if (weapons.size() > 1){
			if (reloading) {
				reloading = false;
				time2 = 0;
			}
			for (int i=0; i<weapons.size(); i++){
				if (weapon.getId().equals(weapons.get(i).getId())){
					if (i < weapons.size()-1){
						setWeapon(weapons.get(i+1));
						break;
					}
					else {
						setWeapon(weapons.get(0));
						break;
					}
				}
			}
		}
	}

	public boolean hasWeapon(){
		if (weapon == null){
			return false;
		}
		return true;
	}

	public Weapon getWeapon(){
		return weapon;
	}

	public void render(Graphics g){
		g.setColor(new Color(112, 193, 179));
		g.setFont(new Font("Trebuchet MS", Font.BOLD, 24));
		if (weapon != null) g.drawString("Weapon: " + weapon.getName(), 600, 700);
		else g.drawString("Weapon: None", 600, 700);
		g.drawString("Bullets: " + bullets, 600, 725);
		g.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		if (reloading) g.drawString("Reloading " + weapon.getName() + "...", 25, 75);
		if (pickupAlertFlag) g.drawString("Picked up " + lastAddedWeapon + "!", 650, 50);
		renderHP(g);
	}

	private void renderHP(Graphics g){
		int hp = 0;
		for(int i = 0; i < handler.stuff.size(); i++){
			GameThing thing = handler.stuff.get(i);
			if (thing.getId() == "Player"){
				Player player = (Player)thing;
				hp = player.getHp();
			}
		}
		g.setColor(Color.BLACK);
		g.drawRect(24, 24, 101, 26);
		g.setColor(Color.GRAY);
		g.fillRect(25, 25, 100, 25);
		if (hp >= 0){
			g.setColor(new Color((int)(255-hp*(255.0/100.0)), (int)(hp*(255.0/100.0)), 0));
			g.fillRect(25, 25, hp, 25);
		}
	}

	public void reload(){
		if (bullets != magazine && !reloading){
			reloading = true;
			time2 = reloadTime;
		}
	}
}
