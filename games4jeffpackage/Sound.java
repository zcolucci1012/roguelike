

import java.io.File;
import java.util.HashMap;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
  private static HashMap <String, File> files = new HashMap <String, File>();
  private static Clip background;
  private static Clip effect;
  private double backgroundVolume = 0.05;
  private double effectVolume = 1;

  public Sound(){
    //put files into a map with its title and file associated with it
    files.put("background", new File("sounds/background.wav"));
    files.put("background2", new File("sounds/background2.wav"));
    files.put("background3", new File("sounds/background3.wav"));
    files.put("background4", new File("sounds/background4.wav"));
    files.put("shotgun", new File("sounds/shotgun.wav"));
    files.put("sniper", new File ("sounds/sniper.wav"));
    files.put("smg", new File ("sounds/smg.wav"));
    files.put("shotgun2", new File("sounds/shotgun2.wav"));
    files.put("dmr", new File("sounds/dmr.wav"));
    files.put("assault rifle", new File("sounds/assault_rifle.wav"));
    files.put("pistol", new File("sounds/pistol.wav"));
    files.put("revolver", new File("sounds/revolver.wav"));
    files.put("minigun", new File("sounds/minigun.wav"));
  }

  /*begin looping the background music*/
  public void loop(String path){
    try{
      if (background != null) background.stop(); //stop the old background music
      background = AudioSystem.getClip();
      background.open(AudioSystem.getAudioInputStream(files.get(path))); //open new background music
      FloatControl gainControl = (FloatControl) background.getControl(FloatControl.Type.MASTER_GAIN);
      background.loop(Clip.LOOP_CONTINUOUSLY); //loop audio continuously
      float dB = (float) (Math.log(backgroundVolume) / Math.log(10.0) * 20.0); //control volume
      gainControl.setValue(dB);
      //Thread.sleep(clip.getMicrosecondLength()/1000);
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public void updateBackgroundVolume(double volume){
    FloatControl gainControl = (FloatControl) background.getControl(FloatControl.Type.MASTER_GAIN);
    backgroundVolume = volume;
    float dB = (float) (Math.log(backgroundVolume) / Math.log(10.0) * 20.0); //control volume
    gainControl.setValue(dB);
  }

  public void updateEffectVolume(double volume){
    effectVolume = volume; // number between 0 and 1 (loudest)
  }

  public void toggleEffectMute(){
    if (effectVolume != 0) effectVolume = 0;
    else effectVolume = 1;
  }

  public void toggleBackgroundMute(){
    if (backgroundVolume != 0) backgroundVolume = 0;
    else backgroundVolume = 0.05;
    FloatControl gainControl = (FloatControl) background.getControl(FloatControl.Type.MASTER_GAIN);
    float dB = (float) (Math.log(backgroundVolume) / Math.log(10.0) * 20.0); //control volume
    gainControl.setValue(dB);
  }

  /*play a sound given its title*/
  public void play(String path){
    try{
      effect = AudioSystem.getClip();
      effect.open(AudioSystem.getAudioInputStream(files.get(path))); //get sound
      FloatControl gainControl = (FloatControl) effect.getControl(FloatControl.Type.MASTER_GAIN);
      effect.start(); //play sound
      float dB = (float) (Math.log(effectVolume) / Math.log(10.0) * 20.0); //control volume
      gainControl.setValue(dB);
      //Thread.sleep(clip.getMicrosecondLength()/1000);
    }catch (Exception e){
      e.printStackTrace();
    }
  }
}
