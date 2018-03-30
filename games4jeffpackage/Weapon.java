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

  private void makeWeapons(){
    if (name.equals("pistol")){
      fireDelay = 25;
      shotSpeed = 5;
      damage = 5;
      magazine = 8;
      reloadTime = 150;
    }
    if (name.equals("smg")){
      fireDelay = 7;
      shotSpeed = 5;
      damage = 2;
      magazine = 30;
      reloadTime = 200;
    }
    ammo = magazine;
  }

  public String getName(){
    return name;
  }
}
