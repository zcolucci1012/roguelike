package games4jeffpackage;

import java.io.File;
import java.util.HashMap;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
  private static HashMap <String, File> files = new HashMap <String, File>();

  public Sound(){
    files.put("background", new File("background.wav"));
    files.put("background2", new File("background2.wav"));
    files.put("shotgun", new File("shotgun.wav"));
    files.put("sniper", new File ("sniper.wav"));
    files.put("smg", new File ("smg.wav"));
  }

  public static void loop(String path, double volume){
    try{
      Clip clip = AudioSystem.getClip();
      clip.open(AudioSystem.getAudioInputStream(files.get(path)));
      FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      clip.loop(Clip.LOOP_CONTINUOUSLY);
      double gain = volume; // number between 0 and 1 (loudest)
      float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
      gainControl.setValue(dB);
      //Thread.sleep(clip.getMicrosecondLength()/1000);
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public static void play(String path, double volume){
    try{
      Clip clip = AudioSystem.getClip();
      clip.open(AudioSystem.getAudioInputStream(files.get(path)));
      FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      clip.start();
      double gain = volume; // number between 0 and 1 (loudest)
      float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
      gainControl.setValue(dB);
      //Thread.sleep(clip.getMicrosecondLength()/1000);
    }catch (Exception e){
      e.printStackTrace();
    }
  }
}
