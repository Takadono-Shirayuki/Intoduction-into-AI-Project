package mazeinterface.mazedialog; // Khai báo package chứa lớp MazeIntro

import javax.swing.*; // Thư viện Swing cho giao diện người dùng

import game.GameVariable;
import mazeinterface.mazecontrol.ImageButton;

import java.awt.*; // Thư viện đồ họa
import java.awt.event.*; // Xử lý sự kiện
import java.io.*; // Đọc/ghi file

import java.util.ArrayList;
import java.util.List;

// 
public class ConversationDialog extends JDialog {
    /**
     * Lớp này dùng để đọc lệnh từ file script <p>
     * Các lệnh bao gồm: <p>
     * - Dialogue: Lời thoại của nhân vật <p>
     * - Input: Nhập liệu từ người dùng <p>
     * - Select: Lựa chọn từ người dùng <p>
     * - Jump: Nhảy đến câu thoại khác <p>
     * Các lệnh này được lưu trong file script và được đọc vào khi khởi tạo <p>
     * Các lệnh này được thực hiện theo thứ tự từ trên xuống dưới <p>
     */
    public abstract class Command
    {
        public String commandType;

        public abstract void run(); // Phương thức chạy lệnh
    }

    /**
     * Lớp này dùng để lưu thông tin lời thoại <p>
     * Nó bao gồm tên nhân vật, tên hiển thị và nội dung lời thoại <p>
     * Nó cũng bao gồm ảnh đại diện của nhân vật <p>
     * Nó được sử dụng để hiển thị lời thoại trong ContentTextArea <p>
     */
    private class Dialogue extends Command
    {
        // Đường dẫn tài nguyên
        private static final String IMAGE_PATH = "/mazeai/MazeImage/";

        public String character;
        public String displayName;
        public String content;
        public ImageIcon avatar;
        public Dialogue(String charecter, String displayName, String content) {
            this.commandType = "Dialogue"; // Loại lệnh
            this.character = charecter;
            this.displayName = displayName;
            this.content = content;
            this.avatar = new ImageIcon(getClass().getResource(IMAGE_PATH + GameVariable.format(charecter) + ".png"));
        }

        public void run() {
            contentTextArea.load(this); // Tải nội dung lời thoại
            commandIndex++; // Tăng chỉ số câu thoại

            // Kiểm tra nếu kkhông cùng 1 người nói
            if (!characterImages[charecterImageIndex].character.equals(this.character)) {
                // Nếu không cùng người nói thì làm mờ người trước đó
                characterImages[charecterImageIndex].dimmed();
                charecterImageIndex = 1 - charecterImageIndex; // Đổi chỉ số nhân vật
                characterImages[charecterImageIndex].load(this.character, this.avatar); // Tải ảnh nhân vật mới
                characterImages[charecterImageIndex].highlight(); // Làm sáng người nói
            }
        }
    }

    /**
     * Lớp này dùng để lưu thông tin nhập liệu <p>
     * Nó bao gồm văn bản hiển thị và tên biến <p>
     * Dùng để thay đổi giá trị của biến trong game <p>
     */
    private class Input extends Command
    {
        public String displayText;
        public String variableName;
        
        public Input(String displayText, String variableName) {
            this.commandType = "Input"; // Loại lệnh
            this.displayText = displayText; // Văn bản hiển thị
            this.variableName = variableName; // Tên biến cần thay đổi
        }
        
        public void run() {
            // Hiện hộp thoại nhập liệu
            String input = new InputDialog(parent, GameVariable.format(displayText), new Dimension(600, 50)).returnValue; // Lấy giá trị nhập vào
            if (input != null) {
                GameVariable.setVariable(variableName, input); // Thay đổi giá trị biến trong game
            }
            commandIndex++; // Tăng chỉ số câu thoại
            processNextCommand(); // Hiện câu thoại tiếp theo
        }
    }

    /**
     * Lớp này dùng để lưu thông tin lựa chọn <p>
     * Có thể thực hiện một lệnh lựa chọn và thay đổi gía trị một biến của game <p>
     */
    private class Select extends Command
    {
        /**
         * Lớp này dùng để lưu thông tin tùy chọn <p>
         * Nó bao gồm văn bản hiển thị, tên biến, giá trị biến và chỉ số lệnh tiếp theo <p>
         */
        public static class Option
        {
            public final String separator = ">>"; // Dấu phân cách
            public String displayText;
            public int nextCommandIndex;
            public String variableName;
            public String variableValue;
            public Option(String option){
                String[] parts = option.split(separator); // Tách chuỗi theo dấu phân cách
                this.displayText = parts[0]; // Văn bản hiển thị
                this.nextCommandIndex = Integer.parseInt(parts[1]); // Chỉ số lệnh tiếp theo
                this.variableName = parts[2]; // Tên biến
                this.variableValue = parts[3]; // Giá trị biến
            }
        }

        public String displayText;
        public Option[] options;

        public Select(String displayText, Option[] options) {
            this.commandType = "Select"; // Loại lệnh
            this.displayText = displayText; // Văn bản hiển thị
            this.options = options; // Danh sách tùy chọn
        }

        public void run() {
            // Hiện hộp thoại lựa chọn
            String selections[] = new String[options.length]; // Tạo mảng chứa các tùy chọn
            for (int i = 0; i < options.length; i++) {
                selections[i] = GameVariable.format(options[i].displayText); // Lấy văn bản hiển thị
            }

            int selectedOption = new SelectDialog(parent, GameVariable.format(displayText), selections, new Dimension(600, 50)).returnValue; // Lấy giá trị lựa chọn
            if (selectedOption != -1) {
                Option selected = options[selectedOption]; // Lấy tùy chọn đã chọn
                GameVariable.setVariable(selected.variableName, selected.variableValue); // Thay đổi giá trị biến trong game
                if (selected.nextCommandIndex != -1) {
                    commandIndex = selected.nextCommandIndex; // Đổi chỉ số câu thoại
                } else {
                    commandIndex++; // Tăng chỉ số câu thoại
                }
            }
            processNextCommand(); // Hiện câu thoại tiếp theo
        }
    }

    /**
     * Lớp này dùng để nhảy đến câu thoại khác <p>
     * Nó bao gồm chỉ số lệnh tiếp theo <p>
     */
    private class Jump extends Command
    {
        public int nextCommandIndex;
        public Jump(int nextCommandIndex) {
            this.commandType = "Jump"; // Loại lệnh
            this.nextCommandIndex = nextCommandIndex; // Chỉ số lệnh tiếp theo
        }

        public void run() {
            commandIndex = nextCommandIndex; // Đổi chỉ số câu thoại
            processNextCommand(); // Hiện câu thoại tiếp theo
        }
    }

    /**
     * Lớp này dùng để hiển thị nội dung lời thoại <p>
     */
    private class ContentTextArea extends JPanel {
        private JLabel displayNameLabel; // Nhãn hiển thị tên người nói
        private JTextArea contentTextArea; // Khu vực hiển thị nội dung lời thoại
        private java.util.Timer typingTimer; // Bộ đếm thời gian cho hiệu ứng gõ chữ

        private String content = ""; // Nội dung lời thoại
        public ContentTextArea() {
            setLayout(null);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setBackground(new Color(0, 0, 0, 0)); // Màu nền trong suốt

            // Nhãn hiển thị tên người nói
            displayNameLabel = new JLabel("");
            displayNameLabel.setFont(new Font("Serif", Font.BOLD, 28));
            displayNameLabel.setHorizontalAlignment(SwingConstants.LEFT);
            displayNameLabel.setForeground(Color.YELLOW);
            add(displayNameLabel);

            // Khu vực hiển thị lời thoại
            contentTextArea = new JTextArea();
            contentTextArea.setLineWrap(true);
            contentTextArea.setWrapStyleWord(true);
            contentTextArea.setFont(new Font("Serif", Font.PLAIN, 26));
            contentTextArea.setForeground(Color.WHITE);
            contentTextArea.setEditable(false);
            contentTextArea.setBackground(Color.DARK_GRAY);
            contentTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            add(contentTextArea);
        }

        /**
         * Phương thức này dùng để tải dữ liệu mới cho lời thoại <p>
         * @param dialogue Đối tượng Dialogue chứa thông tin lời thoại
         */
        public void load(Dialogue dialogue) {
            displayNameLabel.setText(GameVariable.format(dialogue.displayName)); // Hiển thị tên người nói
            contentTextArea.setText(""); // Xóa nội dung cũ
            
            content = GameVariable.format(dialogue.content); // Lưu nội dung lời thoại

            // Tạo hiệu ứng gõ chữ
            typingTimer = new java.util.Timer();
            typingTimer.schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    if (contentTextArea.getText().length() < dialogue.content.length()) {
                        contentTextArea.append(String.valueOf(dialogue.content.charAt(contentTextArea.getText().length()))); // Thêm ký tự vào nội dung
                    } else {
                        cancel(); // Dừng lại khi đã hết nội dung
                    }
                }
            }, 0, 20); // Thời gian giữa các ký tự
        }

        /**
         * Bỏ qua hiệu ứng gõ chữ <p>
         */
        public void skipTyping() {
            if (typingTimer != null) {
                typingTimer.cancel(); // Dừng bộ đếm thời gian
            }
            contentTextArea.setText(content); // Bỏ qua hiệu ứng gõ chữ
        }

        /**
         * Kiểm tra xem còn ký tự nào chưa gõ không <p>
         * @return true nếu còn ký tự chưa gõ, false nếu đã gõ hết
         */
        public boolean isTyping() {
            return contentTextArea.getText().length() < content.length(); // Kiểm tra xem còn ký tự nào chưa gõ không
        }

        @Override
        public void setBounds(int x, int y, int width, int height) {
            super.setBounds(x, y, width, height);
            displayNameLabel.setBounds(30, 10, width - 20, 40);
            contentTextArea.setBounds(10, 50, width - 20, height - 60);
        }
    }

    /**
     * Lớp này dùng để hiển thị ảnh nhân vật <p>
     * Có phương thức để làm sáng và làm mờ ảnh <p>
     */
    private class CharecterImage extends JLabel
    {
        public String character = "";
        private ImageIcon avatar;
        private ImageIcon dimmedAvatar;
        public CharecterImage() {
            super(); // Hiển thị ảnh nhân vật
        }

        /** 
         * Làm sáng ảnh nhân vật <p>
         */
        public void highlight() {
            setIcon(avatar); // Hiển thị ảnh nhân vật
        }

        /**
         * Làm mờ ảnh nhân vật <p>
         */
        public void dimmed() {
            setIcon(dimmedAvatar); // Hiển thị ảnh nhân vật mờ
        }

        /**
         * Tải dữ liệu mới cho nhân vật <p>
         * @param character Tên người nói
         * @param avatar Ảnh nhân vật
         */
        public void load(String character, ImageIcon avatar) {
            this.character = character; // Gán tên người nói
            this.avatar = avatar; // Gán ảnh nhân vật
            this.dimmedAvatar = new ImageIcon(GrayFilter.createDisabledImage(avatar.getImage())); // Tạo ảnh mờ
        }
    }
    private JFrame parent; // Cửa sổ cha

    private CharecterImage characterImages[] = new CharecterImage[2]; // Mảng chứa ảnh nhân vật
    private int charecterImageIndex = 1; // Chỉ số nhân vật hiện tại
    
    private ContentTextArea contentTextArea; // Khu vực hiển thị lời thoại

    private List<Command> commands = new ArrayList<>(); // Danh sách các lệnh
    private int commandIndex = 0; // Câu thoại hiện tại

    private Boolean exitOnClose = true; // Biến kiểm tra có thoát game khi đóng cửa sổ hay không

    /**
     * Constructor của lớp ConversationDialog <p>
     * Tạo một cửa sổ hội thoại với các nhân vật và lời thoại <p>
     * @param parent Cửa sổ cha
     * @param dialogFilePath Đường dẫn đến file chứa lời thoại
     */
    public ConversationDialog(JFrame parent, String dialogFilePath) {
        super(parent, "", false); // Tạo dialog modal
        this.parent = parent; // Gán cửa sổ cha
        setUndecorated(true); // Không viền
        setSize(parent.getWidth(), parent.getHeight()); // Kích thước bằng kích thước cha
        setBackground(new Color(0, 0, 0, 180)); // Màu nền mờ
        setLayout(null); // Tự set layout thủ công

        loadDialogues(dialogFilePath); // Tải lời thoại từ file

        // Gán ảnh nhân vật bên trái
        characterImages[0] = new CharecterImage();
        characterImages[0].setBounds(50, 100, 400, 600);
        getContentPane().add(characterImages[0]);

        // Gán ảnh nhân vật bên phải
        characterImages[1] = new CharecterImage();
        characterImages[1].setBounds(getWidth() - 450, 100, 400, 600); // Set kích thước
        getContentPane().add(characterImages[1]);

        // Tạo panel chứa lời thoại
        contentTextArea = new ContentTextArea();
        contentTextArea.setBounds(50, getHeight() - 170, getWidth() - 100, 150); // Set kích thước
        getContentPane().add(contentTextArea);

        // Nút bỏ qua đoạn giới thiệu
        JButton skipButton = new ImageButton(GameVariable.SPOOKY_IMAGE_PATH, "Bỏ qua", new Font("Serif", Font.BOLD, 20), Color.WHITE, new Dimension(100, 40));
        skipButton.setBounds(getWidth() - 120, 20, 100, 40);
        skipButton.addActionListener(e -> skip());
        getContentPane().add(skipButton);
        
        // Xử lý Alt+F4 (đóng hoàn toàn)
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (exitOnClose) {
                    System.exit(0);
                }
            }
        });

        // Click chuột cũng chuyển thoại
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                processNextCommand();
            }
        });
        setVisible(true); // Hiển thị cửa sổ
        processNextCommand(); // Hiển thị câu thoại đầu tiên
    }

    /**
     * Phương thức này dùng để tải các câu thoại từ file <p>
     * @param dialogFilePath Đường dẫn đến file chứa câu thoại
     */
    private void loadDialogues(String dialogFilePath) {
        InputStream is = getClass().getResourceAsStream(dialogFilePath);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split("\\|");
                    switch (parts[0]) {
                        case "Dialogue": // Câu thoại
                            Dialogue dialogue = new Dialogue(parts[1], parts[2], parts[3]); // Tạo đối tượng Dialogue
                            commands.add(dialogue); // Thêm vào danh sách
                            break;
                        case "Input": // Nhập liệu
                            Input input = new Input(parts[1], parts[2]); // Tạo đối tượng Input
                            commands.add(input); // Thêm vào danh sách
                            break;
                        case "Select": // Lựa chọn
                            Select.Option options[] = new Select.Option[parts.length - 2]; // Tạo mảng tùy chọn
                            for (int i = 0; i < options.length; i++) { 
                                options[i] = new Select.Option(parts[i + 2]); // Tạo đối tượng Option
                            }
                            Select select = new Select(parts[1], options); // Tạo đối tượng Select
                            commands.add(select); // Thêm vào danh sách
                            break;
                        case "Jump": // Nhảy đến câu thoại khác
                            Jump jump = new Jump(Integer.parseInt(parts[1])); // Tạo đối tượng Jump
                            commands.add(jump); // Thêm vào danh sách
                            break;
                        default:
                            break;
                    } 
                }
            } 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Phương thức để hiện câu thoại tiếp theo <p>
     * Nếu đang gõ chữ thì bỏ qua hiệu ứng gõ chữ <p>
     * Nếu không còn câu thoại nào thì đóng cửa sổ
     */
    private void processNextCommand() {
        if (contentTextArea.isTyping()) {
            contentTextArea.skipTyping(); // Bỏ qua hiệu ứng gõ chữ
        }
        else 
        {
            if (commandIndex < commands.size()) {
                commands.get(commandIndex).run(); // Chạy lệnh
            }
            else
            {
                exitOnClose = false; // Không thoát game khi đóng cửa sổ
                dispose(); // Đóng cửa sổ
            }
        }
    }

    /**
     * Phương thức này được gọi khi người dùng bỏ qua hội thoại <p>
     * Nó sẽ không thoát game mà chỉ đóng cửa sổ hội thoại
     */
    private void skip() {
        exitOnClose = false; // Không thoát game khi đóng cửa sổ
        dispose(); // Đóng cửa sổ
    }
}
