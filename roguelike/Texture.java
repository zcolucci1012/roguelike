

import java.awt.image.BufferedImage;

public class Texture {
  //inititalizes spritesheet classes
  private SpriteSheet ps, bs, es, ws, bws, pus, cs, ss;

  //initializes spritesheet images
  private BufferedImage player_sheet = null;
  private BufferedImage block_sheet = null;
  private BufferedImage enemy_sheet = null;
  private BufferedImage weapon_sheet = null;
  private BufferedImage blank_weapon_sheet = null;
  private BufferedImage powerup_sheet = null;
  private BufferedImage core_item_sheet = null;
  private BufferedImage sound_button_sheet = null;

  //initialize image lists
  public BufferedImage[] player = new BufferedImage[4];
  public BufferedImage[] block = new BufferedImage[16];
  public BufferedImage[] enemy = new BufferedImage[8];
  public BufferedImage[] weapon = new BufferedImage[13];
  public BufferedImage[] blank_weapon = new BufferedImage[13];
  public BufferedImage[] powerup = new BufferedImage[6];
  public BufferedImage[] core_item = new BufferedImage[2];
  public BufferedImage[] sound_button = new BufferedImage[4];

  public Texture(){
    BufferedImageLoader loader = new BufferedImageLoader();
    //loads images
    try {
      block_sheet = loader.loadImage("assets/block_sheet.png");
      player_sheet = loader.loadImage("assets/player_sheet.png");
      enemy_sheet = loader.loadImage("assets/enemy_sheet.png");
      weapon_sheet = loader.loadImage("assets/weapon_sheet.png");
      blank_weapon_sheet = loader.loadImage("assets/blank_weapon_sheet.png");
      powerup_sheet = loader.loadImage("assets/powerup_sheet.png");
      core_item_sheet = loader.loadImage("assets/core_item_sheet.png");
      sound_button_sheet = loader.loadImage("assets/sound_button_sheet.png");
    }catch (Exception e){
      e.printStackTrace();
    }
    //create spritesheet classes based off of images
    ps = new SpriteSheet(player_sheet);
    bs = new SpriteSheet(block_sheet);
    es = new SpriteSheet(enemy_sheet);
    ws = new SpriteSheet(weapon_sheet);
    bws = new SpriteSheet(blank_weapon_sheet);
    pus = new SpriteSheet(powerup_sheet);
    cs = new SpriteSheet(core_item_sheet);
    ss = new SpriteSheet(sound_button_sheet);

    getTextures(); //initializes lists of images
  }

  /*gets subimages of spritesheet and adds them to a list of textures */
  private void getTextures(){
    for(int i = 0; i < player.length; i++){
        player[i] = ps.grabImage(i+1, 1, 32, 32);
    }

    for(int i = 0; i < block.length; i++){
        block[i] = bs.grabImage(1+i%4, 1+i/4, 33, 32);
    }

    enemy[0] = es.grabImage(1, 1, 28, 28); //look right
    enemy[1] = es.grabImage(2, 1, 28, 28); //look left
    enemy[2] = es.grabImage(1, 2, 28, 28); //turret
    enemy[3] = es.grabImage(2, 2, 28, 28); //base
    enemy[4] = es.grabImage(1, 4, 28, 28); //tracker turret
    enemy[5] = es.grabImage(2, 4, 28, 28); //tracker base
    enemy[6] = es.grabImage(1, 6, 28, 28, 24, 24); //chicken left
    enemy[7] = es.grabImage(2, 6, 28, 28, 24, 24); //chicken right

    for(int i = 0; i < weapon.length; i++){
        weapon[i] = ws.grabImage(1+i%5, 1+i/5, 24, 24);
    }

    for(int i = 0; i < blank_weapon.length; i++){
        blank_weapon[i] = bws.grabImage(1+i%5, 1+i/5, 24, 24);
    }

    for(int i = 0; i < powerup.length; i++){
        powerup[i] = pus.grabImage(1+i%5, 1+i/5, 24, 24);
    }

    core_item[0] = cs.grabImage(1, 1, 24, 24);
    core_item[1] = cs.grabImage(2, 1, 24, 24);

    for (int i = 0; i < sound_button.length; i++){
      sound_button[i] = ss.grabImage(1+i%2, 1+i/2, 24, 24);
    }
  }
}
