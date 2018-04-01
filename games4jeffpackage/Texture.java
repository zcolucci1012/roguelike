package games4jeffpackage;

import java.awt.image.BufferedImage;

public class Texture {
  private SpriteSheet ps, bs, es;
  private BufferedImage player_sheet = null;
  private BufferedImage block_sheet = null;
  private BufferedImage enemy_sheet = null;

  public BufferedImage[] player = new BufferedImage[3];
  public BufferedImage[] block = new BufferedImage[4];
  public BufferedImage[] enemy = new BufferedImage[2];

  public Texture(){
    BufferedImageLoader loader = new BufferedImageLoader();
    try {
      block_sheet = loader.loadImage("block_sheet.png");
      player_sheet = loader.loadImage("player_sheet.png");
      enemy_sheet = loader.loadImage("enemy_sheet.png");
    }catch (Exception e){
      e.printStackTrace();
    }
    ps = new SpriteSheet(player_sheet);
    bs = new SpriteSheet(block_sheet);
    es = new SpriteSheet(enemy_sheet);

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
  }
}
