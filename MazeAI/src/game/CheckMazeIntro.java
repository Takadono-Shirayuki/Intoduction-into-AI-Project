package game;

import javax.swing.SwingUtilities;

import mazeinterface.MainForm;
import mazeinterface.mazedialog.ConversationDialog;

public class CheckMazeIntro {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ConversationDialog(new MainForm(), "/mazeai/MazeDialogues/intro_dialog.txt");
        });
    }
}