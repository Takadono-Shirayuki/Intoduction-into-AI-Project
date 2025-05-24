package game;

import javax.swing.SwingUtilities;
import mazeinterface.MainForm;
import mazeinterface.mazedialog.ChooseCompanionDialog;

public class CheckLevelSelector {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChooseCompanionDialog(new MainForm());
        });
    }
}
