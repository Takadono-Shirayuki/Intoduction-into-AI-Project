package game;

import javax.swing.*;
import mazeinterface.GameForm;

public class MazeGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int mazeSize = 30;  // Cố định kích thước mê cung 30x30

            // Khởi tạo giao diện với mazeSize và lightSize người dùng nhập
            new GameForm(mazeSize);
        });
    }
}
