package games4jeffpackage;

import java.awt.image.BufferedImage;

public class Texture {
  private SpriteSheet ps, bs, es, ws, bws, pus;
  private BufferedImage player_sheet = null;
  private BufferedImage block_sheet = null;
  private BufferedImage enemy_sheet = null;
  private BufferedImage weapon_sheet = null;
  private BufferedImage blank_weapon_sheet = null;
  private BufferedImage powerup_sheet = null;

  public BufferedImage[] player = new BufferedImage[4];
  public BufferedImage[] block = new BufferedImage[8];
  public BufferedImage[] enemy = new BufferedImage[2];
  public BufferedImage[] weapon = new BufferedImage[11];
  public BufferedImage[] blank_weapon = new BufferedImage[11];
  public BufferedImage[] powerup = new BufferedImage[2];

  public Texture(){
    BufferedImageLoader loader = new BufferedImageLoader();
    try {
      block_sheet = loader.loadImage("assets/block_sheet.png");
      player_sheet = loader.loadImage("assets/player_sheet.png");
      enemy_sheet = loader.loadImage("assets/enemy_sheet.png");
      weapon_sheet = loader.loadImage("assets/weapon_sheet.png");
      blank_weapon_sheet = loader.loadImage("assets/blank_weapon_sheet.png");
      powerup_sheet = loader.loadImage("assets/powerup_sheet.png");
    }catch (Exception e){
      e.printStackTrace();
    }
    ps = new SpriteSheet(player_sheet);
    bs = new SpriteSheet(block_sheet);
    es = new SpriteSheet(enemy_sheet);
    ws = new SpriteSheet(weapon_sheet);
    bws = new SpriteSheet(blank_weapon_sheet);
    pus = new SpriteSheet(powerup_sheet);

    getTextures();
  }

  private void getTextures(){
    player[0] = ps.grabImage(1, 1, 32, 32); //right view
    player[1] = ps.grabImage(2, 1, 32, 32); //front view
    player[2] = ps.grabImage(3, 1, 32, 32); //left view
    player[3] = ps.grabImage(4, 1, 32, 32); //back view

    block[0] = bs.grabImage(1, 1, 33, 32); //brown 1
    block[1] = bs.grabImage(2, 1, 33, 32); //brown 2
    block[2] = bs.grabImage(3, 1, 33, 32); //brown 3
    block[3] = bs.grabImage(4, 1, 33, 32); //brown 4
    block[4] = bs.grabImage(1, 2, 33, 32); //grey 1
    block[5] = bs.grabImage(2, 2, 33, 32); //grey 2
    block[6] = bs.grabImage(3, 2, 33, 32); //grey 3
    block[7] = bs.grabImage(4, 2, 33, 32); //grey 4

    enemy[0] = es.grabImage(1, 1, 28, 28); //look right
    enemy[1] = es.grabImage(2, 1, 28, 28); //look left

    weapon[0] = ws.grabImage(1, 1, 24, 24); //pistol
    weapon[1] = ws.grabImage(2, 1, 24, 24); //smg
    weapon[2] = ws.grabImage(3, 1, 24, 24); //sniper
    weapon[3] = ws.grabImage(4, 1, 24, 24); //assault rifle
    weapon[4] = ws.grabImage(5, 1, 24, 24); //dmr
    weapon[5] = ws.grabImage(1, 2, 24, 24); //slugshot
    weapon[6] = ws.grabImage(2, 2, 24, 24); //minigun
    weapon[7] = ws.grabImage(3, 2, 24, 24); //revolver
    weapon[8] = ws.grabImage(4, 2, 24, 24); //pump shotgun
    weapon[9] = ws.grabImage(5, 2, 24, 24); //tac shotgun
    weapon[10] = ws.grabImage(1, 3, 24, 24); //mauler

    blank_weapon[0] = bws.grabImage(1, 1, 24, 24); //pistol
    blank_weapon[1] = bws.grabImage(2, 1, 24, 24); //smg
    blank_weapon[2] = bws.grabImage(3, 1, 24, 24); //sniper
    blank_weapon[3] = bws.grabImage(4, 1, 24, 24); //assault rifle
    blank_weapon[4] = bws.grabImage(5, 1, 24, 24); //dmr
    blank_weapon[5] = bws.grabImage(1, 2, 24, 24); //slugshot
    blank_weapon[6] = bws.grabImage(2, 2, 24, 24); //minigun
    blank_weapon[7] = bws.grabImage(3, 2, 24, 24); //revolver
    blank_weapon[8] = bws.grabImage(4, 2, 24, 24); //pump shotgun
    blank_weapon[9] = bws.grabImage(5, 2, 24, 24); //tac shotgun
    blank_weapon[10] = bws.grabImage(1, 3, 24, 24); //mauler

    powerup[0] = pus.grabImage(1, 1, 24, 24); //health pack
    powerup[1] = pus.grabImage(2, 1, 24, 24); //damage boost
  }
}
