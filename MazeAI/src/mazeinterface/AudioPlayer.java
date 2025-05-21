package mazeinterface;

import javax.sound.sampled.*;
import java.net.URL;
import java.util.List;

public class AudioPlayer {
    private static Clip clip;
    private static boolean looping = false;
    private static List<String> currentPlaylist;
    private static int currentIndex = 0;
    private static Thread loopThread;
    static final String BACKGROUND_MUSIC_PATH_GAMEFROM = "/mazeai/MazeSound/MazeSound.wav";  // Đường dẫn nhạc nền
    static final String BACKGROUND_MUSIC_PATH_MAINFROM = "/mazeai/MazeSound/MazeSoundMainForm.wav"; 
    // Phát một file âm thanh blocking
    private static void playSingleSoundBlocking(String resourcePath) {
        try {
            stopClip();  // Dừng clip hiện tại nếu có

            URL soundURL = AudioPlayer.class.getResource(resourcePath);
            if (soundURL == null) {
                System.err.println("Không tìm thấy file: " + resourcePath);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

            // Chờ clip chạy xong
            while (!clip.isRunning()) Thread.sleep(10);
            while (clip.isRunning()) Thread.sleep(100);

            clip.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Phát playlist, có thể lặp vô hạn
    public static void playPlaylist(List<String> playlist, boolean loop) {
        stopSound(); // Dừng nếu đang phát gì đó

        looping = loop;
        currentPlaylist = playlist;
        currentIndex = 0;

        loopThread = new Thread(() -> {
            try {
                do {
                    for (String path : currentPlaylist) {
                        playSingleSoundBlocking(path);
                        if (!looping) break;
                    }
                } while (looping);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        loopThread.start();
    }

    // Phát một file duy nhất, không block, không lặp
    public static void playSingleSound(String resourcePath) {
    stopSound();

    try {
        URL soundURL = AudioPlayer.class.getResource(resourcePath);
        if (soundURL == null) return;

        AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
        clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY); // <-- Lặp vô hạn
        clip.start();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    // Dừng phát
    public static void stopSound() {
        looping = false;

        if (loopThread != null && loopThread.isAlive()) {
            loopThread.interrupt();
        }

        stopClip();
    }

    private static void stopClip() {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.close();
            clip = null;
        }
    }

    // Tự động dừng nhạc khi thoát chương trình
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(AudioPlayer::stopSound));
    }
}
