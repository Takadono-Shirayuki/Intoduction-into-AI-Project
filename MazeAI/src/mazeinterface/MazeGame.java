package mazeinterface;
import javax.swing.*;

public class MazeGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int mazeSize = 30;  // Changed from rows/cols to single size since maze is square
            
            // Initialize the interface with the maze environment
            new mazeinterface.MazeInterface(mazeSize);
        });
    }
}