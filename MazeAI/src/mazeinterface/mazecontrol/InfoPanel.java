package mazeinterface.mazecontrol;

import javax.swing.*;
import java.awt.*;
import game.Main;

/**
 * {@code InfoPanel} là bảng hiển thị thông tin trạng thái người chơi trong mê cung.
 * Gồm các thông tin:
 * <ul>
 *   <li>Tầng hiện tại</li>
 *   <li>Số bước còn lại đến khi tái tạo</li>
 *   <li>Tổng số bước còn lại</li>
 *   <li>Tỉ lệ nhận buff</li>
 * </ul>
 * Panel này sử dụng ảnh nền, giới hạn chiều cao bằng nửa màn hình và đồng bộ dữ liệu với {@code Main}.
 */
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

    /**
     * Tạo bảng thông tin người chơi với ảnh nền và chiều cao bằng 1/2 màn hình.
     */
    public InfoPanel() {
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        setPreferredSize(new Dimension(280, screenHeight / 2));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        try {
            backgroundImage = new ImageIcon(getClass().getResource("/mazeai/MazeImage/InfoPanelBackground.jpg")).getImage();
        } catch (Exception e) {
            System.err.println("Không thể tải ảnh nền InfoPanel.");
        }

        Font labelFont = new Font("SansSerif", Font.BOLD, 20);
        Color textColor = Color.WHITE;

        floorLabel = createLabel("Tầng hiện tại: " + floor, labelFont, textColor);
        stepsToRegenLabel = createLabel("Số bước còn lại: " + numsStepUntilRegenerate, labelFont, textColor);
        stepsRemainingLabel = createLabel("<html>Số bước còn lại<br>(tổng): " + stepsRemaining + "</html>", labelFont, textColor);
        buffProbabilityLabel = createLabel("Tỉ lệ nhận buff: " + receiveBuffProbability + "%", labelFont, textColor);

        add(floorLabel);
        add(Box.createVerticalStrut(20));
        add(stepsToRegenLabel);
        add(Box.createVerticalStrut(20));
        add(stepsRemainingLabel);
        add(Box.createVerticalStrut(20));
        add(buffProbabilityLabel);
    }

    /**
     * Vẽ ảnh nền (nếu có) được resize theo kích thước panel.
     *
     * @param g đối tượng đồ họa
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        super.paintComponent(g);
    }

    /**
     * Tạo JLabel chuẩn với định dạng font và màu.
     *
     * @param text nội dung hiển thị
     * @param font font chữ
     * @param color màu chữ
     * @return JLabel được định dạng
     */
    private JLabel createLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    /**
     * Lấy tầng hiện tại.
     * @return số tầng
     */
    public int getFloor() {
        return floor;
    }

    /**
     * Cập nhật tầng hiện tại và đồng bộ với {@code Main}.
     * @param value số tầng mới
     */
    public void setFloor(int value) {
        this.floor = value;
        floorLabel.setText("Tầng hiện tại: " + value);
        Main.setVariableValue(Main.GameStateVariable.FLOOR, value);
    }

    /**
     * Lấy số bước còn lại trước khi tái tạo.
     * @return số bước còn lại
     */
    public int getNumsStepUntilRegenerate() {
        return numsStepUntilRegenerate;
    }

    /**
     * Cập nhật số bước còn lại trước khi tái tạo và đồng bộ với {@code Main}.
     * @param value số bước
     */
    public void setNumsStepUntilRegenerate(int value) {
        this.numsStepUntilRegenerate = value;
        stepsToRegenLabel.setText("Số bước còn lại: " + value);
        Main.setVariableValue(Main.GameStateVariable.NUMS_STEP_UNTIL_REGENERATE, value);
    }

    /**
     * Lấy tổng số bước còn lại.
     * @return số bước còn lại
     */
    public int getStepsRemaining() {
        return stepsRemaining;
    }

    /**
     * Cập nhật tổng số bước còn lại và đồng bộ với {@code Main}.
     * @param value số bước
     */
    public void setStepsRemaining(int value) {
        this.stepsRemaining = value;
        stepsRemainingLabel.setText("<html>Số bước còn lại<br>(tổng): " + value + "</html>");
        Main.setVariableValue(Main.GameStateVariable.STEPS_REMAINING, value);
    }

    /**
     * Lấy tỉ lệ nhận buff hiện tại.
     * @return phần trăm nhận buff
     */
    public int getReceiveBuffProbability() {
        return receiveBuffProbability;
    }

    /**
     * Cập nhật tỉ lệ nhận buff và đồng bộ với {@code Main}.
     * @param value tỉ lệ phần trăm (0–100)
     */
    public void setReceiveBuffProbability(int value) {
        this.receiveBuffProbability = value;
        buffProbabilityLabel.setText("Tỉ lệ nhận buff: " + value + "%");
        Main.setVariableValue(Main.GameStateVariable.RECEIVE_BUFF_PROBABILITY, value);
    }
}
