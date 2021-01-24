 

import java.awt.image.BufferedImage;

public class SpriteSheet {
  private BufferedImage image;

  public SpriteSheet(BufferedImage image){
    this.image = image;
  }

  /*
    takes in the column and row of the image,
    locates image based on width and height of sprite entered
  */
  public BufferedImage grabImage(int col, int row, int width, int height){
    BufferedImage img = image.getSubimage((col-1)*width, (row-1)*height, width, height);
    return img;
  }

  /*
    does the same as above method but specifies the specific image in question's width and height
    if different from others in the sheet
  */
  public BufferedImage grabImage(int col, int row, int colWidth, int rowHeight, int width, int height){
    BufferedImage img = image.getSubimage((col-1)*colWidth, (row-1)*rowHeight, width, height);
    return img;
  }
}
