package game;

import javax.swing.SwingUtilities;
import mazeinterface.MainForm;
import mazeinterface.mazedialog.LevelSelector;

public class CheckLevelSelector {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LevelSelector(new MainForm()).setVisible(true);
        });
    }
}
