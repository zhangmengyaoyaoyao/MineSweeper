import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SoundEffects {
    // Constant for the directory where sound files are located
    private static final String SOUND_DIRECTORY = "src/resources/sounds/";
    // Logger instance for logging errors and messages
    private static final Logger LOGGER = Logger.getLogger(SoundEffects.class.getName());

    public static void playSound(String soundFileName) {
        //Log in the sound file
        File soundFile = new File(SOUND_DIRECTORY + soundFileName);
        try {
            //Read the data from the sound file
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
            
            // Add a listener to the clip to close it after playback is finished
            clip.addLineListener(event -> {
              if (event.getType() == LineEvent.Type.STOP) {//Check if the audio stop
                  clip.close();
              }
            });
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
             // Log errors
             LOGGER.log(Level.SEVERE, "Error playing sound: " + soundFileName, e);
        }
    }

    // click sound effect function
    public static void clickSound() {
        playSound("click.wav");
    }

    // win sound effect function
    public static void winSound() {
        playSound("win.wav");
    }

    // lose sound effect function
    public static void loseSound() {
        playSound("bang.wav");
        playSound("lose.wav");
    }
}
