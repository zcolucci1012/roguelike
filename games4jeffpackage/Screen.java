

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.AlphaComposite;
import java.awt.FontMetrics;
import java.awt.Rectangle;

public class Screen extends MouseAdapter{

    //class imports
    private Handler handler;
    private Main main;
    private Camera cam;

    //mouse and shot positions
    private int mx;
    private int my;
    private float sx;
    private float sy;

    //gun stats
    private Weapon weapon;
    private int fireDelay = 25;
    private int shotSpeed = 5;
    private float damage = 5;
    private int magazine = 0;
    private int bullets = 0;
    private int reloadTime = 100;
    private int inaccuracy = 50;
    private String fireType = "semiauto";
    private int shotsFired = 1;
    private int range = 50;
    private String weaponClass = "";

    //timers
    private int time = fireDelay;
    private int time2 = 0;
    private int pickupAlertTimer = 0;
    private int doorTimer = 0;
    private int introTimer = 0;
    private int powerupTimer = 0;

    //lists
    private ArrayList <Weapon> weapons = new ArrayList <Weapon> ();
    private ArrayList <Integer> ammo = new ArrayList <Integer> ();

    //flags
    private boolean firing = false;
    private boolean reloading = false;
    private boolean pickupAlertFlag = false;
    private boolean doorFlag = false;
    private boolean doorsUnlocked = false;
    private boolean powerupFlag = false;
    private boolean weaponGUIFlag = false;
    private boolean hpFlag = false;
    private boolean mapFlag = false;
    private boolean effectFlag = false;
    private boolean backgroundFlag = false;

    //textures and sounds
    private Texture tex = Main.getInstance();
    private Sound sound = Main.getSound();

    //mods
    private float damageMod = 1;
    private float speedMod = 1;
    private float fireDelayMod = 1;
    private float magazineMod = 1;
    private float reloadTimeMod = 1;
    private float accuracyMod = 1;
    private float defenseMod = 1;
    private float fireRateMod = 1;

    //etc.
    private String lastAddedWeapon = "";
    private BufferedImageLoader loader;
    private RoomPoint room = null;
    private RoomPoint tempRoom = null;
    private boolean movement = true;
    private int dx;
    private int dy;
    private ArrayList <RoomPoint> points = new ArrayList <RoomPoint> ();
    private ArrayList <Vector> vectors = new ArrayList <Vector> ();
    private ArrayList <RoomPoint> visible = new ArrayList <RoomPoint> ();
    private int level = 1;
    private String levelDescription = "Industrial Zone";
    private float angle;
    private int itemRoomIndex = 0;
    private String powerup = "None";
    private int mapSize;
    private int xd;
    private int yd;


    /*instantiate stuff, get some things from the main, add pistol to player's inventory*/
    public Screen (Handler handler, Main main, Camera cam){
        this.handler = handler;
        this.main = main;
        this.cam = cam;

        loader = new BufferedImageLoader();
        points = main.getPoints();
        vectors = main.getVectors();

        addWeapon(new Weapon(0, 0 , "pistol"));
    }

    /*ran whenever mouse is clicked, tries to fire weapon if not in menu*/
    public void mousePressed(MouseEvent e){
        mx = e.getX();
        my = e.getY();

        if (main.getState().equals("menu")){
            if (mouseOver(mx, my, 305, 340, 200, 100)){
                main.setState("1"); //will start the level if this area is clicked
            }
        }
        if (main.getState().equals("pause")){
          if (mouseOver(mx, my, 688, 45, 24, 24)){
            sound.toggleEffectMute();
            effectFlag = !effectFlag;
          }
          if (mouseOver(mx, my, 724, 45, 24, 24)){
            sound.toggleBackgroundMute();
            backgroundFlag = !backgroundFlag;
          }
        }
        else firing = true;
    }

    /*stops firing*/
    public void mouseReleased(MouseEvent e){
        mx = e.getX();
        my = e.getY();

        firing = false;
    }

    /*finds if mouse is over a certain area*/
    private boolean mouseOver(int x, int y, int x1, int y1, int w, int h){
        if(x > x1 && x < x1+w){
            if (y > y1 && y < y1+h){
                return true;
            }
            else return false;
        }
        else return false;
    }

    public void tick(){
        if (main.getState().equals("menu")){ //do none of this if in the menu
            return;
        }

        itemRoomIndex = main.getItemRoomIndex(); //get location of item room

        //finds points adjacent to (and including) the starting point,
        //adds them to list of rooms visible on the map
        if (room != null && room.isPoint(new RoomPoint(0,0))){
            boolean found = false;
            for (RoomPoint point: visible){
                if (point.isPoint(room)){
                    found = true;
                }
            }
            if (!found) visible.add(room);
            for (Vector vector: vectors){
                if (vector.hasPoint(0, 0) == 1 || vector.hasPoint(0, 0) == 2){
                    found = false;
                    for (RoomPoint point: visible){
                        if (point.isPoint(vector.getOther(0, 0))){
                            found = true;
                        }
                    }
                    if (!found) visible.add(vector.getOther(0, 0));
                }
            }
        }

        //finds points adjacent to (and including) the current room
        //(if room has changed since last time it was checked),
        //adds them to list of rooms visible on the map
        for (RoomPoint point: points){
          if (point.isPoint(new RoomPoint ((int)(-cam.getX()/Main.WIDTH), (int)(cam.getY()/Main.HEIGHT)))){
            room = point;
            break;
          }
        }
        if (tempRoom != null && !room.isPoint(tempRoom)){
            doorsUnlocked = false;
            Vector pair = new Vector(tempRoom, room);
            boolean found = false;
            for (RoomPoint point: visible){
                if (point.isPoint(room)){
                    found = true;
                }
            }
            if (!found) visible.add(room);
            for (Vector vector: vectors){
                if (vector.hasPoint(room) == 1 || vector.hasPoint(room) == 2){
                    found = false;
                    for (RoomPoint point: visible){
                        if (point.isPoint(vector.getOther(room))){
                            found = true;
                        }
                    }
                    if (!found) visible.add(vector.getOther(room));
                }
            }

            //gets change in x and y between rooms to decide which way to push player when entering
            dx = pair.getDX();
            dy = pair.getDY();

            //finds if enemies are present, if so, push player into the room by starting the doorTimer
            for(int i = 0; i < handler.stuff.size(); i++){
                GameThing thing = handler.stuff.get(i);
                if (thing.getId().length() >= 6 && thing.getId().substring(0,6).equals("Enemy.")){
                    if (((Enemy)thing).getRoom().isPoint(room)){
                        doorTimer = 15;
                        break;
                    }
                }
            }
        }

        //locates enemies in the room, and returns whether any are found
        boolean found = false;
        for(int i = 0; i < handler.stuff.size(); i++){
            GameThing thing = handler.stuff.get(i);
            if (thing.getId().length() >= 6 && thing.getId().substring(0,6).equals("Enemy.")){
                if (((Enemy)thing).getRoom().isPoint(room)){
                    found = true;
                    break;
                }
            }
        }

        //if no enemies are found and the doors haven't been unlocked,
        //unlock the doors and set the rooms to completed
        //also possibly spawn a random core item
        if (!found  && !doorsUnlocked && !room.isComplete()){
            unlockDoors();
            for (RoomPoint point: visible){
                if (room.isPoint(point)){
                    point.complete();
                }
            }
            room.complete();
            //if room completed at the boss level, add a trapdoor to next level
            if (room.isPoint(points.get(points.size()-1))){
                handler.addObject(new Trapdoor(400 + room.getX()*Main.WIDTH, 400 - room.getY()*Main.WIDTH, "Trapdoor"));
            }
            doorsUnlocked = true;
            if(!room.isPoint(0, 0) && !room.isPoint(points.get(main.getItemRoomIndex()))) spawnItem();
        }

        //move the player until timer is over
        if (doorTimer != 0){
            movePlayer();
            doorTimer--;
            if (doorTimer == 0){
                movement = true; //allow the player to move again
                for(int i = 0; i < handler.stuff.size(); i++){
                    GameThing thing = handler.stuff.get(i);
                    if (thing.getId() == "Player"){ //stop the player's motion
                        thing.setVelX(0);
                        thing.setVelY(0);
                    }
                    if (thing.getId() == "Door"){ //close all doors in the room
                        if (((Door)thing).getRoom().isPoint(room)){
                            ((Door)thing).close();
                        }
                    }
                }
            }
        }

        //get info about the mouse
        Point a = MouseInfo.getPointerInfo().getLocation();
        Point b = main.getLocationOnScreen();
        mx = (int) a.getX() - (int)b.getX();
        my = (int) a.getY() - (int)b.getY();

        //turn point on the mouse to a point in the current room0
        sx = mx - cam.getX();
        sy = my - cam.getY();

        //set the texture of the player based on what direction the mouse is in
        for(int i = 0; i < handler.stuff.size(); i++){
          GameThing thing = handler.stuff.get(i);
          if (thing.getId() == "Player"){
            float x = thing.getX() + thing.getWidth()/2;
            float y = thing.getY() + thing.getHeight()/2;
            angle = (float)Math.atan((sy-y)/(sx-x));
            if ((sx-x) < 0){
                angle += Math.PI;
            }
            if (Math.abs(sy-y) < Math.abs(sx-x) && sx > thing.getX()) ((Player)thing).setType(0);
            if (Math.abs(sy-y) > Math.abs(sx-x) && sy > thing.getY()) ((Player)thing).setType(1);
            if (Math.abs(sy-y) < Math.abs(sx-x) && sx < thing.getX()) ((Player)thing).setType(2);
            if (Math.abs(sy-y) > Math.abs(sx-x) && sy < thing.getY()) ((Player)thing).setType(3);
            break;
          }
        }

        //make GUI transparent if something underneath it
        for(int i = 0; i < handler.stuff.size(); i++){
          GameThing thing = handler.stuff.get(i);
          if (!thing.getId().equals("Block")){
            weaponGUIFlag = false;
            if (thing.getBounds().intersects(new Rectangle(600+Main.WIDTH*room.getX(), 650-Main.HEIGHT*room.getY(), 180, 108))){
              weaponGUIFlag = true;
              break;
            }
          }
        }

        //make HP transparent if something underneath it
        for(int i = 0; i < handler.stuff.size(); i++){
          GameThing thing = handler.stuff.get(i);
          if (!thing.getId().equals("Block")){
            hpFlag = false;
            if (thing.getBounds().intersects(new Rectangle(24+Main.WIDTH*room.getX(), 24-Main.HEIGHT*room.getY(), 101, 26))){
              hpFlag = true;
              break;
            }
          }
        }

        //make map transparent if something underneath it
        for(int i = 0; i < handler.stuff.size(); i++){
          GameThing thing = handler.stuff.get(i);
          if (!thing.getId().equals("Block")){
            mapFlag = false;
            if (thing.getBounds().intersects(new Rectangle(730-xd+mapSize-5+Main.WIDTH*room.getX(), 45-5-Main.HEIGHT*room.getY(), xd+10, yd+10))){
              mapFlag = true;
              break;
            }
          }
        }

        //weapon firing
        if (weapon != null){ //if player has a weapon
            if (time2 == 0){ //if done reloading (time2 is the reload timer)
                if (time == fireDelay){ //if time between shots ends (time is fire delay timer)
                    if (firing && bullets > 0){ //if the user is attempting to fire the weapon
                        fire();
                        if (fireType.equals("semiauto")) firing = false; //reset weapon firing if gun not automatic
                        time = 0;
                    }
                }
                else {
                    time++; //increase until reaching fireDelay time
                }
            }
            else { // if reloading
                time2--;
                if (time2 == 0){
                    reloading = false;
                    bullets = magazine; //add bullets to mag in screen
                    weapon.setAmmo(magazine); //add bullets to mag in object
                    time = fireDelay; //allow weapon to shoot again
                }
            }
            if (bullets <= 0 && !reloading){ //reload if magazine is empty
                time2 = reloadTime;
                reloading = true;
            }
            if (pickupAlertTimer != 0){ //note when user picks up weapon
                pickupAlertTimer--;
                if (pickupAlertTimer == 0){
                    pickupAlertFlag = false;
                }
            }
            if (powerupTimer != 0){ //note when user picks up powerup
                powerupTimer--;
                if (powerupTimer == 0){
                    powerupFlag = false;
                }
            }
        }

        //control the intro message at the start of the level
        if (introTimer != 400){
            introTimer++;
        }

        tempRoom = room; //save old room for comparison
    }

    /*fire a bullet or a few*/
    private void fire(){
        //play sound based on weapon name
        if (weapon.getName().equals("slugshot") || weapon.getName().equals("pump shotgun")) sound.play("shotgun");
        if (weapon.getName().equals("tac shotgun") || weapon.getName().equals("mauler")) sound.play("shotgun2");
        if (weapon.getName().equals("sniper")) sound.play("sniper");
        if (weapon.getName().equals("smg")) sound.play("smg");
        if (weapon.getName().equals("DMR")) sound.play("dmr");
        if (weapon.getName().equals("assault rifle")) sound.play("assault rifle");
        if (weapon.getName().equals("pistol")) sound.play("pistol");
        if (weapon.getName().equals("revolver")) sound.play("revolver");
        if (weapon.getName().equals("minigun")) sound.play("minigun");

        //shoots a bullet from the player to the point where clicked
        for(int i = 0; i < handler.stuff.size(); i++){
            GameThing thing = handler.stuff.get(i);
            if (thing.getId() == "Player"){
                //get location of player
                float x = thing.getX() + thing.getWidth()/2;
                float y = thing.getY() + thing.getHeight()/2;

                float d = (float)Math.sqrt(Math.pow((sx-(int)x),2) + Math.pow((sy-(int)y),2)); //get distance

                int tempShots = shotsFired; //sets amount of bullets fired by one click

                while (tempShots != 0){ //until desired amount of bullets fired
                    //sets random changes in direction based off inaccuracy of weapon
                    int randX = (int)(d*((int)(Math.random()*(inaccuracy + 1))-(inaccuracy/2))/250.0);
                    int randY = (int)(d*((int)(Math.random()*(inaccuracy + 1))-(inaccuracy/2))/250.0);
                    d = (float)Math.sqrt(Math.pow((sx+randX-(int)x),2) + Math.pow((sy+randY-(int)y),2)); //get new distance
                    if (d != 0){
                        //set the velocity by normalizing player-->click vector (set magnitude to shotSpeed)
                        float sVelX = ((sx+randX - (int)x)/d*shotSpeed);
                        float sVelY = ((sy+randY - (int)y)/d*shotSpeed);

                        //set origin of where the point is shot (something about this may be wrong)
                        float dx = (20 * (sx+randX-(int)x))/d;
                        float dy = (20 * (sy+randY-(int)y))/d;

                        //add shot with given position, angle, damage, range, and velocity
                        if (weaponClass.equals("grenade launcher")){
                          Grenade grenade = new Grenade((int)x - thing.getWidth()/8 + dx, (int)y - thing.getHeight()/8 + dy+10, "Grenade", angle, (damage*damageMod), (10*range)/shotSpeed, handler);
                          grenade.setVelX(sVelX);
                          grenade.setVelY(sVelY);
                          handler.addObject(grenade);
                        }
                        else {
                          Shot shot = new Shot((int)x - thing.getWidth()/8 + dx, (int)y - thing.getHeight()/8 + dy+10, "Shot", angle, (damage*damageMod), (10*range)/shotSpeed, handler);
                          shot.setVelX(sVelX);
                          shot.setVelY(sVelY);
                          handler.addObject(shot);
                        }
                    }
                    tempShots--;
                }
                bullets--; //remove 1 from the bullets in the magazine
                weapon.setAmmo(bullets); //change in the object
                break;
            }
        }
    }

    /*add a weapon to the list of weapons if not already there*/
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
        lastAddedWeapon = weapon.getName(); //change the name to display when picking up weapons
        pickupAlertTimer = 200; //start pickup alert timer
        pickupAlertFlag = true;
        if (weapons.size() == 1){
            setWeapon(weapon); //equip weapon if it's the first in the list
        }
    }

    /*get the properties from the desired weapon and set them to variables in the class*/
    public void setWeapon(Weapon weapon){
        this.weapon = weapon;
        fireDelay = (int)(weapon.getFireDelay()/fireRateMod);
        shotSpeed = weapon.getShotSpeed();
        damage = weapon.getDamage();
        magazine = weapon.getMagazine();
        reloadTime = weapon.getReloadTime();
        bullets = weapon.getAmmo();
        inaccuracy = (int)(weapon.getInaccuracy()/accuracyMod);
        fireType = weapon.getFireType();
        shotsFired = weapon.getShotsFired();
        range = weapon.getRange();
        weaponClass = weapon.getWeaponClass();
        time = fireDelay; //set the timer to the fireDelay so that shots can be fired right away
    }

    /*switches the current weapon to the next in the list*/
    public void changeWeapon(){
        if (weapons.size() > 1){ //only if more than one weapon
            if (reloading) { //cancels reload on switching
                reloading = false;
                time2 = 0;
            }
            for (int i=0; i<weapons.size(); i++){ //change weapon
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

    public Weapon getWeapon(){
        return weapon;
    }

    public void render(Graphics g){
        //render menu screen
        if (main.getState().equals("menu")){
            g.setColor(new Color(36,123,160));
            g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(loader.loadImage("assets/start_button.png"), 305, 340, null);
            return;
        }

        Graphics2D g2d = (Graphics2D) g.create(); //create temporary graphics variable to be made transparent
        float alpha = 0.5f; //transparency of GUI
        float alpha2 = (float)(-Math.pow((-introTimer/200.0 + 1), 2) + 1); //transparency of intro
        float alpha3 = 0.10f; //transparency of hovered GUI
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        AlphaComposite ac2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha2);
        AlphaComposite ac3 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha3);

        //display intro text
        g2d.setComposite(ac2);
        g2d.setColor(Color.WHITE);
        drawCenteredString(g2d, "Chapter " + level + ": " + levelDescription, new Rectangle(-1, 1, Main.WIDTH-1, Main.HEIGHT+1), new Font("Trebuchet MS", Font.BOLD, 36));
        g2d.setColor(Color.BLACK);
        drawCenteredString(g2d, "Chapter " + level + ": " + levelDescription, new Rectangle(0, 0, Main.WIDTH, Main.HEIGHT), new Font("Trebuchet MS", Font.BOLD, 36));

        //render GUI
        g2d.setComposite(ac);

        //render weapon GUI
        if (mouseOver(mx, my, 600, 650, 180, 108) || weaponGUIFlag) g2d.setComposite(ac3);
        g2d.drawImage(loader.loadImage("assets/weaponGUI.png"), 600, 650, 180, 108, null);
        if (weapon != null){
            g2d.drawImage(tex.weapon[weapon.getType()], 612, 662, 72, 72, null); //draw equipped weapon
        }
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Trebuchet MS", Font.BOLD, 14));
        if (weapon != null) { //if the player has a weapon
            g2d.drawString("Weapon:", 690, 682);
            g2d.setFont(new Font("Trebuchet MS", Font.BOLD, 10));
            g2d.drawString(weapon.getName(), 690, 696);
            g2d.setFont(new Font("Trebuchet MS", Font.BOLD, 14));
        }
        else {
            g2d.drawString("Weapon:", 690, 682);
            g2d.drawString("None", 690, 696);
        }
        g2d.drawString("Bullets:", 690, 710);
        g2d.drawString(bullets + "/" + magazine, 690, 724);

        //render alerts and reload bar
        g2d.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
        if (reloading) renderReload(g2d);
        g2d.setColor(Color.BLACK);
        //describes the heirarchy of which alerts precede which when displayed
        if (pickupAlertFlag && reloading) g2d.drawString("Picked up " + lastAddedWeapon + "!", 600, 625);
        else if (pickupAlertFlag) g2d.drawString("Picked up " + lastAddedWeapon + "!", 600, 640);
        if (pickupAlertFlag && reloading && powerupFlag) g2d.drawString("Picked up " + powerup + "!", 600, 610);
        else if ((reloading && powerupFlag) || (pickupAlertFlag && powerupFlag)) g2d.drawString("Picked up " + powerup + "!", 600, 625);
        else if (powerupFlag) g2d.drawString("Picked up " + powerup + "!", 600, 640);
        g2d.setComposite(ac);

        //render HP
        if (mouseOver(mx, my, 24, 24, 101, 26) || hpFlag) g2d.setComposite(ac3);
        renderHP(g2d);
        g2d.setComposite(ac);

        //render map
        mapSize = 20; //size of individual square side
        int maxY = 0;
        int maxX = 0;
        int minX = 0;
        int minY = 0;
        for (RoomPoint point: visible){ //get the maximum distance from main room
            if (point.getY() > maxY) maxY = point.getY();
            if (point.getX() > maxX) maxX = point.getX();
            if (point.getY() < minY) minY = point.getY();
            if (point.getX() < minX) minX = point.getX();
        }
        maxX *= mapSize;
        maxY *= mapSize;
        minX *= mapSize;
        minY *= mapSize;
        xd = maxX-minX+mapSize;
        yd = maxY-minY+mapSize;
        int i = 0;
        if (mouseOver(mx, my, 730-xd-5+mapSize, 45-5, xd+10, yd+10) || mapFlag) g2d.setComposite(ac3);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(730-xd-5+mapSize, 45-5, xd+10, yd+10);
        for (RoomPoint point: visible){
            if (point.isPoint(0,0)) g2d.setColor(Color.BLUE); //color of start point
            else if (point.isPoint(points.get(points.size()-1))) g2d.setColor(Color.RED); //color of boss room
            else if (point.isPoint(points.get(itemRoomIndex))) g2d.setColor(Color.YELLOW); //color of item room
            else if (point.isComplete()) g2d.setColor(new Color(150, 255, 150)); //color of completed basic rooms
            else g2d.setColor(new Color(100, 100, 100)); //color of incomplete basic rooms
            g2d.fillRect(mapSize*point.getX() + 730 - maxX, -mapSize*point.getY() + maxY + 45, mapSize, mapSize); //render room
            if (room != null && point.isPoint(room)){ //draw over current room with white box
                g2d.setColor(Color.WHITE);
                g2d.fillRect(mapSize*point.getX() + 730 - maxX, -mapSize*point.getY() + maxY + 45, mapSize, mapSize);
            }
            g2d.setColor(Color.BLACK);
            g2d.drawRect(mapSize*point.getX() + 730 - maxX, -mapSize*point.getY() + maxY + 45, mapSize, mapSize);
            i++;
        }
        g2d.setComposite(ac);

        //render cursor image
        g.drawImage(loader.loadImage("assets/cursor.png"), mx-8, my-8, null);

        if (main.getState().equals("pause")) {
          g.setColor(new Color(0, 0, 0, 150));
          g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);//transparent pause screen
          if (!effectFlag) g.drawImage(tex.sound_button[2], 688, 45, null);
          else g.drawImage(tex.sound_button[3], 688, 45, null);
          if (!backgroundFlag) g.drawImage(tex.sound_button[0], 724, 45, null);
          else g.drawImage(tex.sound_button[1], 724, 45, null);
        }

        g2d.dispose();
    }

    /* renders health bar */
    private void renderHP(Graphics2D g2d){
        float hp = 0;
        float totalHp = 0;
        for(int i = 0; i < handler.stuff.size(); i++){
            GameThing thing = handler.stuff.get(i);
            if (thing.getId() == "Player"){ //get hp from player
                Player player = (Player)thing;
                hp = player.getHp();
                totalHp = player.getTotalHp();
            }
        }

        //display bar
        g2d.setColor(Color.BLACK);
        g2d.drawRect(24, 24, 101, 26);
        g2d.setColor(Color.GRAY);
        g2d.fillRect(25, 25, 100, 25);
        if (hp >= 0){
            //change color dependent on current health and total health
            g2d.setColor(new Color((int)(255-hp*(255.0/totalHp)), (int)(hp*(255.0/totalHp)), 0));
            //fill health bar dependent on health and total health
            g2d.fillRect(25, 25, (int)(hp*(100.0/totalHp)), 25);
        }
    }

    /*renders reload bar*/
    public void renderReload(Graphics2D g2d){
        g2d.setColor(Color.BLACK);
        g2d.drawRect(600, 630, 180, 10);
        if (time2 > 0){
            //draw rectangle based on how much time left in reload
            g2d.setColor(Color.WHITE);
            g2d.fillRect(600, 630, (int)(time2*(180.0/reloadTime)), 10);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(600, 630, (int)(time2*(180.0/reloadTime)), 10);
        }
    }

    /*reloads current weapon*/
    public void reload(){
        if (bullets != magazine && !reloading){ //if magazine isn't full and not already reloading
            reloading = true;
            time2 = reloadTime; //reset reload timer
        }
    }

    /*moves player into the room if enemies present*/
    private void movePlayer(){
        movement = false; //player cannot move
        for (int i=0; i<handler.stuff.size(); i++){
            GameThing thing = handler.stuff.get(i);
            if (thing.getId() == "Player"){ //change players velocity based on which direction they must go in
                if (dx == 1) { thing.setVelX(5); thing.setVelY(0);}
                if (dx == -1) { thing.setVelX(-5); thing.setVelY(0);}
                if (dy == 1) { thing.setVelX(0); thing.setVelY(-5);}
                if (dy == -1) { thing.setVelX(0); thing.setVelY(5);}
            }
        }
    }

    /*unlock doors in current room*/
    private void unlockDoors(){
        for (int i=0; i<handler.stuff.size(); i++){
            GameThing thing = handler.stuff.get(i);
            if (thing.getId() == "Door"){
                if (((Door)thing).getRoom().isPoint(room)){ //unlock each door in the room
                    ((Door)thing).unlock();
                }
            }
        }
    }

    public boolean getMovement(){
        return movement;
    }

    public void toggleMovement(){
        this.movement = !movement;
    }

    /*called on new level, resets variables*/
    public void restart(){
        visible.clear(); //clear visible points
        level++; //increment level
        changeLevelDescription(); //change description based on level
        introTimer = 0; //reset intro timer
    }

    public int getLevel(){
        return level;
    }

    /*draws a centered string*/
    public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
      FontMetrics metrics = g.getFontMetrics(font);
      int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
      int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
      g.setFont(font);
      g.drawString(text, x, y);
    }

    public void changeLevelDescription(){
        if (level == 1) levelDescription = "Industrial Zone";
        if (level == 2) levelDescription = "Cavernous Confine";
        if (level == 3) levelDescription = "Jungle Junction";
        if (level == 4) levelDescription = "Freezing Fields";
    }

    private void spawnItem(){
      int chance = (int)(Math.random()*100) + 1;
      int x;
      int y;
      boolean intersecting = true;
      CoreItem item = null;
      while (intersecting){
        x = ((int)(Math.random()*21)+1)*32 + Main.WIDTH*room.getX();
        y = ((int)(Math.random()*21)+1)*32 - Main.HEIGHT*room.getY();
        if (chance < 25) item = new CoreItem((int)x+4, (int)y+4, "heart");
        else if (chance < 35) item = new CoreItem((int)x+4, (int)y+4, "chest");
        intersecting = false;
        if (item != null){
          for (int i=0; i<handler.stuff.size(); i++){
            GameThing thing = handler.stuff.get(i);
            if (item.getBounds().intersects(thing.getBounds())){
              intersecting = true;
            }
          }
        }
      }
      if (item != null) handler.addObject(item);
    }

    public float getAngle(){
        return angle;
    }

    public RoomPoint getRoom(){
        return room;
    }

    public float getDamage(){
		return damage;
	}

    public void setDamageMod(float damageMod){
        this.damageMod = damageMod;
    }

    public float getDamageMod(){
        return damageMod;
    }

    public void setSpeedMod(float speedMod){
        this.speedMod = speedMod;
    }

    public float getSpeedMod(){
        return speedMod;
    }

    public void setDefenseMod(float defenseMod){
        this.defenseMod = defenseMod;
    }

    public float getDefenseMod(){
        return defenseMod;
    }

    public void setFireRateMod(float fireRateMod){
        this.fireRateMod = fireRateMod;
        fireDelay = (int)(weapon.getFireDelay()/fireRateMod);
    }

    public float getFireRateMod(){
        return fireRateMod;
    }

    public void setAccuracyMod(float accuracyMod){
        inaccuracy = (int)(inaccuracy*this.accuracyMod/accuracyMod);
        this.accuracyMod = accuracyMod;
    }

    public float getAccuracyMod(){
        return accuracyMod;
    }

    /*sets timer when powerup obtained*/
    public void notifyPowerup(String powerup){
        this.powerup = powerup;
        powerupTimer = 200;
        powerupFlag = true;
    }

}
