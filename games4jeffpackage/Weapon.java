package games4jeffpackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Weapon extends Pickup {

  private int fireDelay;
  private int shotSpeed;
  private int damage;
  private int magazine;
  private int reloadTime;
  private int ammo;
  private int inaccuracy;
  private String fireType;
  private int range;
  private int shotsFired = 1;
  private String name;

  public Weapon (float x, float y, String id){
    super(x, y, id);
    name = this.id.substring(7);
    makeWeapons();
  }

  public int getFireDelay(){
    return fireDelay;
  }

  public void setFireDelay(int fireDelay){
    this.fireDelay = fireDelay;
  }

  public int getShotSpeed(){
    return shotSpeed;
  }

  public void setShotSpeed(int shotSpeed){
    this.shotSpeed = shotSpeed;
  }

  public int getDamage(){
    return damage;
  }

  public void setDamage(int damage){
    this.damage = damage;
  }

  public int getMagazine(){
    return magazine;
  }

  public void setMagazine(int magazine){
    this.magazine = magazine;
  }

  public int getReloadTime(){
    return reloadTime;
  }

  public void setReloadTime(int reloadTime){
    this.reloadTime = reloadTime;
  }

  public int getAmmo(){
    return ammo;
  }

  public void setAmmo(int ammo){
    this.ammo = ammo;
  }

  public int getInaccuracy(){
    return inaccuracy;
  }

  public void setInaccuracy(int inaccuracy){
    this.inaccuracy = inaccuracy;
  }

  public String getFireType(){
    return fireType;
  }

  public void setFireType(String fireType){
    this.fireType = fireType;
  }

  public int getShotsFired(){
    return shotsFired;
  }

  public void setShotsFired(int shotsFired){
    this.shotsFired = shotsFired;
  }

  public int getRange(){
    return range;
  }

  public void setRange(int range){
    this.range = range;
  }

  private void makeWeapons(){
    if (name.equals("pistol")){
      fireDelay = 25;
      shotSpeed = 5;
      damage = 5;
      magazine = 8;
      reloadTime = 100;
      inaccuracy = 50;
      fireType = "semiauto";
      range = 50;
    }
    if (name.equals("smg")){
      fireDelay = 7;
      shotSpeed = 5;
      damage = 3;
      magazine = 30;
      reloadTime = 200;
      inaccuracy = 100;
      fireType = "auto";
      range = 30;
    }
    if (name.equals("sniper")){
      fireDelay = 100;
      shotSpeed = 15;
      damage = 20;
      magazine = 5;
      reloadTime = 250;
      inaccuracy = 5;
      fireType = "semiauto";
      range = 100;
    }
    if (name.equals("assault rifle")){
      fireDelay = 12;
      shotSpeed = 8;
      damage = 4;
      magazine = 24;
      reloadTime = 150;
      inaccuracy = 50;
      fireType = "auto";
      range = 60;
    }
    if (name.equals("DMR")){
      fireDelay = 20;
      shotSpeed = 11;
      damage = 9;
      magazine = 10;
      reloadTime = 200;
      inaccuracy = 20;
      fireType = "semiauto";
      range = 80;
    }
    if (name.equals("slugshot")){
      fireDelay = 40;
      shotSpeed = 7;
      damage = 18;
      magazine = 6;
      reloadTime = 130;
      inaccuracy = 5;
      fireType = "semiauto";
      range = 15;
    }
    if (name.equals("minigun")){
      fireDelay = 5;
      shotSpeed = 11;
      damage = 2;
      magazine = 200;
      reloadTime = 600;
      inaccuracy = 85;
      fireType = "auto";
      range = 80;
    }
    if (name.equals("revolver")){
      fireDelay = 80;
      shotSpeed = 10;
      damage = 13;
      magazine = 6;
      reloadTime = 120;
      inaccuracy = 10;
      fireType = "semiauto";
      range = 100;
    }
    if (name.equals("pump shotgun")){
      fireDelay = 75;
      shotSpeed = 15;
      damage = 3;
      magazine = 8;
      reloadTime = 200;
      inaccuracy = 100;
      fireType = "semiauto";
      shotsFired = 8;
      range = 15;
    }
    if (name.equals("tac shotgun")){
      fireDelay = 35;
      shotSpeed = 15;
      damage = 2;
      magazine = 10;
      reloadTime = 150;
      inaccuracy = 150;
      fireType = "auto";
      shotsFired = 10;
      range = 10;
    }
    if (name.equals("mauler")){
      fireDelay = 10;
      shotSpeed = 20;
      damage = 1;
      magazine = 15;
      reloadTime = 100;
      inaccuracy = 150;
      fireType = "auto";
      shotsFired = 10;
      range = 10;
    }
    ammo = magazine;
  }

  public String getName(){
    return name;
  }
}
