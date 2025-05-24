package mazeinterface.mazecontrol;

import game.Main;
import java.awt.*;
import javax.swing.*;

public class InfoPanel extends JPanel {
    private JLabel floorLabel;
    private JLabel stepsToRegenLabel;
    private JLabel stepsRemainingLabel;
    private JLabel buffProbabilityLabel;

    private int floor = 1;
    private int numsStepUntilRegenerate = 0;
    private int stepsRemaining = 0;
    private int receiveBuffProbability = 0;

    private Image backgroundImage;

    public InfoPanel() {
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        setPreferredSize(new Dimension(380, screenHeight / 3));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        try {
            backgroundImage = new ImageIcon(getClass().getResource("/mazeai/Icon/SkillCard.jpg")).getImage();
        } catch (Exception e) {
            System.err.println("Không thể tải ảnh nền InfoPanel.");
        }

        Font labelFont = new Font("SansSerif", Font.BOLD, 20);
        Color textColor = Color.WHITE;

        // Tạo các label
        floorLabel = createLabel("Tầng hiện tại: " + floor, labelFont, textColor);
        floorLabel.setAlignmentX(CENTER_ALIGNMENT);

        stepsToRegenLabel = createLabel("Số bước trước khi reset: " + numsStepUntilRegenerate, labelFont, textColor);
        stepsToRegenLabel.setAlignmentX(CENTER_ALIGNMENT);

        stepsRemainingLabel = createLabel("Số bước còn lại (tổng) " + stepsRemaining, labelFont, textColor);
        stepsRemainingLabel.setAlignmentX(CENTER_ALIGNMENT);

        buffProbabilityLabel = createLabel("Tỉ lệ nhận buff: " + receiveBuffProbability + "%", labelFont, textColor);
        buffProbabilityLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Tạo contentBox để bọc label vào giữa
        JPanel contentBox = new JPanel();
        contentBox.setOpaque(false);
        contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));

        contentBox.add(floorLabel);
        contentBox.add(Box.createVerticalStrut(20));
        contentBox.add(stepsToRegenLabel);
        contentBox.add(Box.createVerticalStrut(20));
        contentBox.add(stepsRemainingLabel);
        contentBox.add(Box.createVerticalStrut(20));
        contentBox.add(buffProbabilityLabel);

        // Thêm glue để căn giữa dọc
        add(Box.createVerticalGlue());
        add(contentBox);
        add(Box.createVerticalGlue());
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        super.paintComponent(g);
    }

    private JLabel createLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int value) {
        this.floor = value;
        floorLabel.setText("Tầng hiện tại: " + value);
        Main.setVariableValue(Main.GameStateVariable.FLOOR, value);
    }

    public int getNumsStepUntilRegenerate() {
        return numsStepUntilRegenerate;
    }

    public void setNumsStepUntilRegenerate(int value) {
        this.numsStepUntilRegenerate = value;
        stepsToRegenLabel.setText("Số bước trước khi reset: " + value);
        Main.setVariableValue(Main.GameStateVariable.NUMS_STEP_UNTIL_REGENERATE, value);
    }

    public int getStepsRemaining() {
        return stepsRemaining;
    }

    public void setStepsRemaining(int value) {
        this.stepsRemaining = value;
        stepsRemainingLabel.setText("Số bước còn lại (tổng) " + value);
        Main.setVariableValue(Main.GameStateVariable.STEPS_REMAINING, value);
    }

    public int getReceiveBuffProbability() {
        return receiveBuffProbability;
    }

    public void setReceiveBuffProbability(int value) {
        this.receiveBuffProbability = value;
        buffProbabilityLabel.setText("Tỉ lệ nhận buff: " + value + "%");
        Main.setVariableValue(Main.GameStateVariable.RECEIVE_BUFF_PROBABILITY, value);
    }
}
