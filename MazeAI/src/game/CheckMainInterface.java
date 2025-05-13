package game;

import mazeinterface.MainForm;
import mazeinterface.mazedialog.ShadowOverlay;

public class CheckMainInterface {
    public static void main(String[] args) {
        new ShadowOverlay(new MainForm(), 1000, 1000, ShadowOverlay.MIST_RISE).setVisible(true); // Tạo một lớp phủ mờ dần
    }
}