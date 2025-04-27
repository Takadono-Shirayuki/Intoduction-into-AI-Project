package japy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import mazenv.Pair;

public class JaPy {
    public class State {
        /**
         * Chương trình chưa được khởi động
         */
        public static final int NOT_STARTED = -1;
        /**
         * Chương trình đang chạy
         */
        public static final int RUNNING = 0;
        /**
         * Chương trình đã bị huỷ bỏ
         */
        public static final int TERMINATED = 1;
        /**
         * Chương trình không phản hồi
         */
        public static final int NOT_RESPONDING = 2;
        /**
         * Chương trình đã hoàn thành
         */
        public static final int COMPLETED = 3;
    }
    public class Error {
        /**
         * Không có lỗi
         */
        public static final int NO_ERROR = 0;
        /**
         * Không tìm thấy file python
         */
        public static final int FILE_NOT_FOUND = 1;
        /**
         * Tham số đầu vào không hợp lệ
         */
        public static final int INVALID_ARGUMENT = 2;
    }
    private String pythonScriptFilePath;
    private ProcessBuilder processBuilder;
    private Process process;
    private BufferedReader reader;
    private BufferedWriter writer;
    private int state = State.NOT_STARTED;
    private int error = Error.NO_ERROR;

    /**
     * Khởi tạo đối tượng JaPy với đường dẫn đến file python
     * @param pythonScriptFilePath Đường dẫn đến file python
     * @param createProcess Nếu true, sẽ tự động tạo process python
     */
    public JaPy(String pythonScriptFilePath, Boolean createProcess) {
        this.pythonScriptFilePath = pythonScriptFilePath;
        if (createProcess) {
            createPythonProcess();
        }
        state = State.RUNNING;
    }

    /**
     * Khởi tạo đối tượng JaPy với đường dẫn đến file python, tự động tạo process python
     * @param pythonScriptFilePath Đường dẫn đến file python
     */
    public JaPy(String pythonScriptFilePath) {
        this(pythonScriptFilePath, true);
    }

    /**
     * Tạo process python
     * @return true nếu tạo process thành công, false nếu không thành công
     */
    public Boolean createPythonProcess() {
        processBuilder = new ProcessBuilder("python", pythonScriptFilePath);
        try
        {
            process = processBuilder.start();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            return true;
        } catch (IOException e) {
            state = State.NOT_STARTED;
            error = Error.FILE_NOT_FOUND;
            return false;
        }
    }

    /**
     * Chạy script python với đầu vào là input
     * @param input Đầu vào cho script python
     * @return Giá trị trả về là một cặp (output, success), trong đó output là đầu ra của script python và success là true nếu chạy thành công, false nếu không thành công
     */
    public Pair<String, Boolean> runPythonScript(String input) {
        if (process == null) {
            createPythonProcess();
        }
        if (sendInput(input))
            return new Pair<>(null, false); 
        return getOutput();
    }

    /**
     * Gửi đầu vào cho script python
     * @param input Đầu vào cho script python
     * @return true nếu gửi thành công, false nếu không thành công
     */
    public Boolean sendInput(String input) {
        if (state == State.RUNNING) {
            try {
                writer.write(input);
                writer.newLine();
                writer.flush();
                return true;
            } catch (IOException e) {
                state = State.NOT_RESPONDING;
                error = Error.NO_ERROR;
                return false;
            }
        } 
        else 
            return false;
    }

    /**
     * Nhận đầu ra từ script python
     * @return Giá trị trả về là một cặp (output, success), trong đó output là đầu ra của script python và success là true nếu nhận thành công, false nếu không thành công
     */
    public Pair<String, Boolean> getOutput() {
        if (state == State.RUNNING) {
            try {
                String output = reader.readLine();
                if (output == "Invalid input")
                {
                    error = Error.INVALID_ARGUMENT;
                    return new Pair<>(null, false); // Không có đầu ra
                }
                if (!process.isAlive())
                {
                    state = State.COMPLETED;
                    error = Error.NO_ERROR;
                }
                return new Pair<>(output, true); // Trả về đầu ra và trạng thái thành công
            }
            catch (IOException e) {
                state = State.NOT_RESPONDING;
                error = Error.NO_ERROR;
                return new Pair<>(null, false); // Không có đầu ra
            }
        }
        return new Pair<>(null, false); // Không có đầu ra
    }

    /**
     * Đóng process python
     */
    public void close() {
        state = State.TERMINATED;
        error = Error.NO_ERROR;
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (process != null) {
            process.destroy();
        }
    }

    /**
     * Lấy trạng thái của process python
     * @return Trạng thái của process python
     */
    public int getState() {
        return state;
    }

    /**
     * Lấy trạng thái lỗi của process python
     * @return Trạng thái lỗi của process python
     */
    public int getError() {
        return error;
    }

    /** 
     * Đặt đường dẫn đến file python
     * @param pythonScriptFilePath Đường dẫn đến file python
     */
    public void setPythonScriptFilePath(String pythonScriptFilePath) {
        this.pythonScriptFilePath = pythonScriptFilePath;
    }

    /**
     * Khởi động lại process python
     * @return true nếu khởi động lại thành công, false nếu không thành công
     */
    public Boolean RestartPythonProcess() {
        close();
        return createPythonProcess();
    }
}
