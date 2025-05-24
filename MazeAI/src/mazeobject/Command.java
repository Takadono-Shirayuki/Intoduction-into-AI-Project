package mazeobject;

import javax.swing.ImageIcon;
import game.GameVariable;

public abstract class Command {
        public String commandType;

        public abstract void run(); // Phương thức chạy lệnh
    
    /**
     * Lớp này dùng để lưu thông tin lời thoại <p>
     * Nó bao gồm tên nhân vật, tên hiển thị và nội dung lời thoại <p>
     * Nó cũng bao gồm ảnh đại diện của nhân vật <p>
     * Nó được sử dụng để hiển thị lời thoại trong ContentTextArea <p>
     */
    public static class Dialogue extends Command
    {
        // Đường dẫn tài nguyên
        protected static final String IMAGE_PATH = "/mazeai/MazeImage/";

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
            
        }
    }

    /**
     * Lớp này dùng để lưu thông tin nhập liệu <p>
     * Nó bao gồm văn bản hiển thị và tên biến <p>
     * Dùng để thay đổi giá trị của biến trong game <p>
     */
    public static class Input extends Command
    {
        public String displayText;
        public String variableName;
        
        public Input(String displayText, String variableName) {
            this.commandType = "Input"; // Loại lệnh
            this.displayText = displayText; // Văn bản hiển thị
            this.variableName = variableName; // Tên biến cần thay đổi
        }
        
        public void run() {
        }
    }

    /**
     * Lớp này dùng để lưu thông tin lựa chọn <p>
     * Có thể thực hiện một lệnh lựa chọn và thay đổi gía trị một biến của game <p>
     */
    public static class Select extends Command
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
        }
    }

    /**
     * Lớp này dùng để nhảy đến câu thoại khác <p>
     * Nó bao gồm chỉ số lệnh tiếp theo <p>
     */
    public static class Jump extends Command
    {
        public int nextCommandIndex;
        public Jump(int nextCommandIndex) {
            this.commandType = "Jump"; // Loại lệnh
            this.nextCommandIndex = nextCommandIndex; // Chỉ số lệnh tiếp theo
        }

        public void run() {
        }
    }
}