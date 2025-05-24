package mazeinterface.mazedialog; // Khai báo package chứa lớp MazeIntro

import javax.swing.*; // Thư viện Swing cho giao diện người dùng

import game.GameVariable;
import mazeinterface.mazecontrol.CharacterImage;
import mazeinterface.mazecontrol.ImageButton;
import mazeinterface.mazecontrol.TypingTextArea;
import mazeobject.Command;

import java.awt.*; // Thư viện đồ họa
import java.awt.event.*; // Xử lý sự kiện
import java.io.*; // Đọc/ghi file

import java.util.ArrayList;
import java.util.List;

// 
public class ConversationDialog extends JDialog {
    /**
     * Lớp này dùng để lưu thông tin lời thoại <p>
     * Nó bao gồm tên nhân vật, tên hiển thị và nội dung lời thoại <p>
     * Nó cũng bao gồm ảnh đại diện của nhân vật <p>
     * Nó được sử dụng để hiển thị lời thoại trong ContentTextArea <p>
     */
    private class Dialogue extends Command.Dialogue
    {
        public Dialogue(String character, String displayName, String content) {
            super(character, displayName, content);
        }

        public void run() {
            typingTextArea.load(this, 30); // Tải nội dung lời thoại
            commandIndex++; // Tăng chỉ số câu thoại

            // Kiểm tra nếu kkhông cùng 1 người nói
            if (!characterImages[characterImageIndex].character.equals(this.character)) {
                // Nếu không cùng người nói thì làm mờ người trước đó
                characterImages[characterImageIndex].dimmed();
                characterImageIndex = 1 - characterImageIndex; // Đổi chỉ số nhân vật
                characterImages[characterImageIndex].load(this.character, this.avatar); // Tải ảnh nhân vật mới
                characterImages[characterImageIndex].highlight(); // Làm sáng người nói
            }
        }
    }

    /**
     * Lớp này dùng để lưu thông tin nhập liệu <p>
     * Nó bao gồm văn bản hiển thị và tên biến <p>
     * Dùng để thay đổi giá trị của biến trong game <p>
     */
    private class Input extends Command.Input
    {   
        public Input(String displayText, String variableName) {
            super(displayText, variableName);
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
    private class Select extends Command.Select
    {
        public Select(String displayText, Option[] options) {
            super(displayText, options); // Gọi constructor của lớp cha
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
    private class Jump extends Command.Jump
    {
        public Jump(int nextCommandIndex) {
            super(nextCommandIndex); // Gọi constructor của lớp cha
        }

        public void run() {
            commandIndex = nextCommandIndex; // Đổi chỉ số câu thoại
            processNextCommand(); // Hiện câu thoại tiếp theo
        }
    }

    private JFrame parent; // Cửa sổ cha

    private CharacterImage characterImages[] = new CharacterImage[2]; // Mảng chứa ảnh nhân vật
    private int characterImageIndex = 1; // Chỉ số nhân vật hiện tại
    
    private TypingTextArea typingTextArea; // Khu vực hiển thị lời thoại

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
        characterImages[0] = new CharacterImage();
        characterImages[0].setBounds(50, 100, 400, 600);
        getContentPane().add(characterImages[0]);

        // Gán ảnh nhân vật bên phải
        characterImages[1] = new CharacterImage();
        characterImages[1].setBounds(getWidth() - 450, 100, 400, 600); // Set kích thước
        getContentPane().add(characterImages[1]);

        // Tạo panel chứa lời thoại
        typingTextArea = new TypingTextArea();
        typingTextArea.setBounds(50, getHeight() - 170, getWidth() - 100, 150); // Set kích thước
        getContentPane().add(typingTextArea);

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
        if (typingTextArea.isTyping()) {
            typingTextArea.skipTyping(); // Bỏ qua hiệu ứng gõ chữ
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
