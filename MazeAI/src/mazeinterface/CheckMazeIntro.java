package mazeinterface;

import javax.swing.SwingUtilities;

public class CheckMazeIntro {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MazeIntro intro = new MazeIntro(null);
            intro.setVisible(true);
        });
    }
}