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
  private boolean auto;
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

  public boolean getAuto(){
    return auto;
  }

  public void toggleAuto(){
    this.auto = !auto;
  }

  private void makeWeapons(){
    if (name.equals("pistol")){
      fireDelay = 25;
      shotSpeed = 5;
      damage = 5;
      magazine = 8;
      reloadTime = 100;
      inaccuracy = 50;
      auto = false;
    }
    if (name.equals("smg")){
      fireDelay = 7;
      shotSpeed = 5;
      damage = 2;
      magazine = 30;
      reloadTime = 200;
      inaccuracy = 100;
      auto = true;
    }
    if (name.equals("sniper")){
      fireDelay = 100;
      shotSpeed = 15;
      damage = 20;
      magazine = 5;
      reloadTime = 250;
      inaccuracy = 5;
      auto = false;
    }
    if (name.equals("assault rifle")){
      fireDelay = 12;
      shotSpeed = 8;
      damage = 3;
      magazine = 24;
      reloadTime = 150;
      inaccuracy = 50;
      auto = true;
    }
    if (name.equals("designated marksman rifle")){
      fireDelay = 20;
      shotSpeed = 11;
      damage = 9;
      magazine = 10;
      reloadTime = 100;
      inaccuracy = 20;
      auto = false;
    }
    ammo = magazine;
  }

  public String getName(){
    return name;
  }
}
