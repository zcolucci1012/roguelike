package games4jeffpackage;

import java.awt.image.BufferedImage;

public class Texture {
  private SpriteSheet ps, bs, es, ws;
  private BufferedImage player_sheet = null;
  private BufferedImage block_sheet = null;
  private BufferedImage enemy_sheet = null;
  private BufferedImage weapon_sheet = null;

  public BufferedImage[] player = new BufferedImage[3];
  public BufferedImage[] block = new BufferedImage[4];
  public BufferedImage[] enemy = new BufferedImage[2];
  public BufferedImage[] weapon = new BufferedImage[11];

  public Texture(){
    BufferedImageLoader loader = new BufferedImageLoader();
    try {
      block_sheet = loader.loadImage("block_sheet.png");
      player_sheet = loader.loadImage("player_sheet.png");
      enemy_sheet = loader.loadImage("enemy_sheet.png");
      weapon_sheet = loader.loadImage("weapon_sheet.png");
    }catch (Exception e){
      e.printStackTrace();
    }
    ps = new SpriteSheet(player_sheet);
    bs = new SpriteSheet(block_sheet);
    es = new SpriteSheet(enemy_sheet);
    ws = new SpriteSheet(weapon_sheet);

    getTextures();
  }

  private void getTextures(){
    player[0] = ps.grabImage(1, 1, 32, 32); //right view
    player[1] = ps.grabImage(2, 1, 32, 32); //front view
    player[2] = ps.grabImage(3, 1, 32, 32); //left view

    block[0] = bs.grabImage(1, 1, 33, 32); //brown 1
    block[1] = bs.grabImage(2, 1, 33, 32); //brown 2
    block[2] = bs.grabImage(3, 1, 33, 32); //brown 3
    block[3] = bs.grabImage(4, 1, 33, 32); //brown 4

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
  }
}
