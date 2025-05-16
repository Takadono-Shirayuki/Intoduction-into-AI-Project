package mazeinterface.mazedialog;

import mazeinterface.mazecontrol.ColorShiftButton;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;

public class LevelSelector extends JDialog {

    private File[] scriptFiles;
    private int selectedIndex = -1;
    private JButton[] levelButtons;
    private boolean exitOnClose = true;

    public LevelSelector(JFrame parent) {
        super(parent, true);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 180));
        setSize(parent.getWidth(), parent.getHeight());
        setLocationRelativeTo(parent);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (exitOnClose) System.exit(0);
            }
        });

        JPanel contentPanel = new JPanel(null);
        contentPanel.setOpaque(false);
        setContentPane(contentPanel);

        // Load scripts
        File scriptDir = Paths.get(System.getProperty("user.dir"), "scripts").toFile();
        scriptFiles = scriptDir.listFiles((dir, name) -> name.endsWith(".py"));

        // Load button labels from text file
        List<String> levelNames = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/mazeai/Document/levelnames.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) levelNames.add(line.trim());
            }
        } catch (IOException e) {
            showError("Lỗi đọc file levelnames.txt: " + e.getMessage());
            return;
        }

        int total = Math.min(scriptFiles.length, levelNames.size());
        levelButtons = new JButton[total];

        Font btnFont = new Font("SansSerif", Font.BOLD, 20);
        Color btnStart = new Color(30, 30, 30);
        Color btnEnd = new Color(70, 70, 70);
        Dimension btnSize = new Dimension(300, 45);

        // Create a center panel that holds all elements centered
        int centerPanelWidth = 400;
        int centerPanelHeight = total * 60 + 130;
        JPanel centerPanel = new JPanel(null);
        centerPanel.setOpaque(false);
        centerPanel.setBounds(
            (getWidth() - centerPanelWidth) / 2,
            (getHeight() - centerPanelHeight) / 2,
            centerPanelWidth,
            centerPanelHeight
        );

        JLabel titleLabel = new JLabel("Bạn sẽ bước vào lối nào?");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBounds((centerPanelWidth - 400) / 2, 0, 400, 50);
        centerPanel.add(titleLabel);

        for (int i = 0; i < total; i++) {
            ColorShiftButton levelBtn = new ColorShiftButton(
                levelNames.get(i),
                btnFont,
                Color.WHITE,
                btnStart,
                btnEnd,
                btnSize
            );
            int idx = i;
            levelBtn.addActionListener(e -> {
                selectedIndex = idx;
                updateButtonStyles();
            });
            levelBtn.setBounds((centerPanelWidth - btnSize.width) / 2, 60 + i * 55, btnSize.width, btnSize.height);
            levelButtons[i] = levelBtn;
            centerPanel.add(levelBtn);
        }

        ColorShiftButton runButton = new ColorShiftButton("▶ Bắt đầu hành trình",
                btnFont,
                Color.WHITE,
                new Color(138, 43, 226),
                new Color(180, 80, 250),
                new Dimension(250, 45));

        runButton.setBounds((centerPanelWidth - 250) / 2, 60 + total * 55 + 10, 250, 45);
        runButton.addActionListener(e -> {
            runSelectedScript();
            exitOnClose = false;
            dispose();
        });
        centerPanel.add(runButton);

        selectedIndex = 0;
        updateButtonStyles();

        contentPanel.add(centerPanel);
        // Gỡ bỏ setVisible khỏi constructor
    }

    private void updateButtonStyles() {
        for (int i = 0; i < levelButtons.length; i++) {
            JButton btn = levelButtons[i];
            if (i == selectedIndex) {
                btn.setForeground(Color.YELLOW);
            } else {
                btn.setForeground(Color.WHITE);
            }
            btn.repaint();
        }
    }

    private void runSelectedScript() {
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Hãy chọn một mức.");
            return;
        }

        File selectedFile = scriptFiles[selectedIndex];
        try {
            new ProcessBuilder("python", selectedFile.getAbsolutePath()).inheritIO().start();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi chạy file: " + ex.getMessage());
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
        exitOnClose = false;
        dispose();
    }
}