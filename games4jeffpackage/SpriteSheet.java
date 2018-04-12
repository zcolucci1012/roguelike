package games4jeffpackage;

import java.awt.image.BufferedImage;

public class SpriteSheet {
  private BufferedImage image;

  public SpriteSheet(BufferedImage image){
    this.image = image;
  }

  public BufferedImage grabImage(int col, int row, int width, int height){
    BufferedImage img = image.getSubimage((col-1)*width, (row-1)*height, width, height);
    return img;
  }

  public BufferedImage grabImage(int col, int row, int colWidth, int rowHeight, int width, int height){
    BufferedImage img = image.getSubimage((col-1)*colWidth, (row-1)*rowHeight, width, height);
    return img;
  }
}
