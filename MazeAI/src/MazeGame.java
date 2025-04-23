import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import mazeai.MazePanel;

public class MazeGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int rows = 30;
            int cols = 30;
            int lightSize = 3;

            new mazeai.MazeInterface(rows, cols, lightSize);
        });
    }
}